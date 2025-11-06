package aau.sw.service;

import org.springframework.stereotype.Service;

import aau.sw.model.Case;
import aau.sw.repository.CaseRepository;

@Service
public class CaseService {
    private final CaseRepository caseRepository;
    private final AuditableService auditableService;

    public CaseService(CaseRepository caseRepository, AuditableService auditableService) {
        this.caseRepository = caseRepository;
        this.auditableService = auditableService;
    }
    
    public Case createCase(Case newCase){
        auditableService.setCreatedBy(newCase);
        return caseRepository.save(newCase);
    }
}
