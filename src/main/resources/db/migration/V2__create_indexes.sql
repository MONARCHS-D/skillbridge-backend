
CREATE INDEX idx_academicians_institution_id ON academicians(institution_id);
CREATE INDEX idx_students_institution_id ON students(institution_id);
CREATE INDEX idx_postings_company_id ON postings(company_id);
CREATE INDEX idx_applications_student_id ON applications(student_id);
CREATE INDEX idx_applications_posting_id ON applications(posting_id);
CREATE INDEX idx_assessments_student_id ON assessments(student_id);
CREATE INDEX idx_activities_host_company_id ON activities(host_company_id);
CREATE INDEX idx_activities_host_institution_id ON activities(host_institution_id);
CREATE INDEX idx_activities_registration_user_id ON activities_registration(user_id);
CREATE INDEX idx_mentorship_mentor_user_id ON mentorship(mentor_user_id);
CREATE INDEX idx_mentorship_mentee_user_id ON mentorship(mentee_user_id);
CREATE INDEX idx_mentorship_activities_id ON mentorship(activities_id);
CREATE INDEX idx_mentorship_feedback_mentorship_id ON mentorship_feedback(mentorship_id);
CREATE INDEX idx_internship_progress_application_id ON internship_progress(application_id);
CREATE INDEX idx_recommendation_logs_user_id ON recommendation_logs(user_id);
CREATE INDEX idx_notifications_user_id ON notifications(user_id);

CREATE INDEX idx_students_skills_gin ON students USING GIN (skills);
CREATE INDEX idx_postings_required_skills_gin ON postings USING GIN (required_skills);

CREATE INDEX idx_students_embedding_hnsw
    ON students USING hnsw (embedding_vector vector_cosine_ops);

CREATE INDEX idx_postings_embedding_hnsw
    ON postings USING hnsw (embedding_vector vector_cosine_ops);

ANALYZE students;
ANALYZE postings;
