from fastapi import FastAPI, HTTPException
from pydantic import BaseModel
from sentence_transformers import SentenceTransformer
import numpy as np
import requests
import io
import PyPDF2
from typing import List, Dict, Any

app = FastAPI(title="SkillBridge Recommender")

# Loaded once at startup — all-MiniLM-L6-v2 produces 384-dim embeddings,
# matching your Postgres vector(384) columns exactly.
MODEL_NAME = "sentence-transformers/all-MiniLM-L6-v2"
model = SentenceTransformer(MODEL_NAME)

CANONICAL_SKILLS = [
    "java", "spring", "python", "sql", "react", "docker",
    "kubernetes", "node", "aws", "ml", "data"
]


class EmbedRequest(BaseModel):
    text: str


class ResumeUrlRequest(BaseModel):
    resumeUrl: str


class RecommendRequest(BaseModel):
    profile: Dict[str, Any]
    candidatePostings: List[Dict[str, Any]]


def extract_text_from_pdf_bytes(content: bytes) -> str:
    try:
        reader = PyPDF2.PdfReader(io.BytesIO(content))
        text = ""
        for page in reader.pages:
            text += page.extract_text() or ""
        return text
    except Exception:
        # Fall back to raw decode if it isn't a real PDF
        return content.decode(errors="ignore")


@app.get("/health")
async def health():
    return {"status": "ok"}


@app.post("/api/recommender/embed")
async def embed_text(req: EmbedRequest):
    emb = model.encode(req.text, convert_to_numpy=True, normalize_embeddings=True)
    return {"embedding": emb.tolist()}


@app.post("/api/recommender/parse_resume")
async def parse_resume(req: ResumeUrlRequest):
    # FIX: resume comes in as a URL (matches your StudentRegistrationDto.resumeUrl),
    # not a multipart upload — so we fetch it ourselves instead of expecting a file.
    try:
        response = requests.get(req.resumeUrl, timeout=10)
        response.raise_for_status()
    except requests.RequestException as e:
        raise HTTPException(status_code=400, detail=f"Could not download resume: {e}")

    content_type = response.headers.get("Content-Type", "")
    if "pdf" in content_type.lower() or req.resumeUrl.lower().endswith(".pdf"):
        text = extract_text_from_pdf_bytes(response.content)
    else:
        text = response.content.decode(errors="ignore")

    lower = text.lower()
    found_skills = [s.capitalize() for s in CANONICAL_SKILLS if s in lower]

    emb = model.encode(text, convert_to_numpy=True, normalize_embeddings=True)

    return {
        "skills": found_skills,
        "embedding": emb.tolist(),
        "text_snippet": text[:200],
    }


@app.post("/api/recommender/recommend")
async def recommend(req: RecommendRequest):
    if req.profile.get("embedding"):
        profile_emb = np.array(req.profile["embedding"], dtype=np.float32)
    else:
        txt = req.profile.get("text", "")
        if isinstance(req.profile.get("skills"), list):
            txt = txt + " " + " ".join(req.profile.get("skills"))
        profile_emb = model.encode(txt, convert_to_numpy=True, normalize_embeddings=True)

    results = []
    for p in req.candidatePostings:
        if p.get("embedding"):
            posting_emb = np.array(p["embedding"], dtype=np.float32)
        else:
            posting_text = (
                p.get("title", "") + " " +
                p.get("description", "") + " " +
                " ".join(p.get("required_skills", []))
            )
            posting_emb = model.encode(posting_text, convert_to_numpy=True, normalize_embeddings=True)

        # Vectors are normalized, so dot product == cosine similarity
        score = float(np.dot(profile_emb, posting_emb))
        matched = list(set(req.profile.get("skills", [])) & set(p.get("required_skills", [])))
        missing = [s for s in p.get("required_skills", []) if s not in req.profile.get("skills", [])]

        results.append({
            "postingId": p.get("id"),
            "score": score,
            "matched": matched,
            "missing": missing,
            "explain": f"{len(matched)}/{max(1, len(p.get('required_skills', [])))} skills matched",
        })

    results.sort(key=lambda r: r["score"], reverse=True)
    return results