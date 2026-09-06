package com.monarchs.SkillBridge.serviceimpl;

import com.monarchs.SkillBridge.projection.InstitutionDropdownView;
import com.monarchs.SkillBridge.repository.InstitutionRepository;
import com.monarchs.SkillBridge.service.InstitutionService;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class InstitutionServiceImpl implements InstitutionService {

    private final InstitutionRepository institutionRepo;

    public InstitutionServiceImpl(InstitutionRepository institutionRepo) {
        this.institutionRepo = institutionRepo;
    }

    @Override
    public List<InstitutionDropdownView> getAllInstitutionService() {
        return institutionRepo.findAllInstituteAsc();
    }
}
