package com.monarchs.SkillBridge.serviceimpl;

import com.monarchs.SkillBridge.dto.PostingDto;
import com.monarchs.SkillBridge.entities.Company;
import com.monarchs.SkillBridge.entities.Posting;
import com.monarchs.SkillBridge.repository.CompanyRepository;
import com.monarchs.SkillBridge.repository.EmbeddingVectorRepository;
import com.monarchs.SkillBridge.repository.PostingRepository;
import com.monarchs.SkillBridge.service.PostingService;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

@Service
public class PostingServiceImpl implements PostingService {

    private final PostingRepository postingRepo;

    private final CompanyRepository companyRepo;

    private final EmbeddingVectorRepository embeddingVectorRepo;

    private final EmbeddingService embeddingService;

    public PostingServiceImpl(PostingRepository postingRepo, CompanyRepository companyRepo, EmbeddingVectorRepository embeddingVectorRepo, EmbeddingService embeddingService) {
        this.postingRepo = postingRepo;
        this.companyRepo = companyRepo;
        this.embeddingVectorRepo = embeddingVectorRepo;
        this.embeddingService = embeddingService;
    }

    @Transactional
    @Override
    public String addPostingService(Long id, PostingDto dto) {
        Company company=companyRepo.findByUserId((id))
                .orElseThrow(()->new RuntimeException("Company not found"));

        Posting posting=Posting.builder()
                .company(company)
                .title(dto.getTitle())
                .description(dto.getDescription())
                .location(dto.getLocation())
                .stipend(dto.getStipend())
                .employmentType(dto.getEmploymentType())
                .requiredSkills(dto.getRequiredSkills())
                .metadata(dto.getMetadata())
                .deadline(dto.getDeadline())
                .build();
        postingRepo.save(posting);

        String profileText= embeddingService.buildPostingProfileText(
                posting.getTitle(),
                posting.getDescription(),
                posting.getLocation(),
                posting.getIsRemote(),
                posting.getStipend(),
                posting.getEmploymentType(),
                posting.getRequiredSkills(),
                posting.getMetadata()
        );

        float[] embedding= embeddingService.generateEmbedding(profileText);
        embeddingVectorRepo.savePostingEmbedding(posting.getId(),embedding);

        return "Job Posting Successful";
    }
}
