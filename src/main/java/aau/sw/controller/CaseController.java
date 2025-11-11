package aau.sw.controller;


import aau.sw.dto.CaseReq;
import aau.sw.model.Case;
import aau.sw.repository.CaseRepository;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import org.springframework.web.bind.annotation.PathVariable;
import aau.sw.aspect.LogExecution;
import aau.sw.service.CaseService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import java.util.List;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    private final CaseService caseService;

    public CaseController(CaseService caseService){
        this.caseService = caseService;
    }

    @Autowired
    private CaseRepository caseRepository;


    @PostMapping
    public ResponseEntity<Case> createCase(@Valid @RequestBody CaseReq req) {
        var c = new Case();
        c.setTitle(req.title());
        c.setStatus(req.status());
        Case created = caseService.createCase(c);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
    }

    @GetMapping
    public List<Case> getCases() {
        return caseRepository.findAll();

    }

    @GetMapping("/{id}")
    public ResponseEntity<Case> getCasebyId(@PathVariable String id) {
        return caseRepository.findById(id)
                .map(Case -> ResponseEntity.ok().body(Case))
                .orElse(ResponseEntity.notFound().build());
    }

    @GetMapping
    public List<Case> getCases(@RequestParam(required = false) String assetId) {
        if (assetId != null && !assetId.isEmpty()) {
            return caseRepository.findByAssetId(assetId);
        }
        return caseRepository.findAll();
    }

    @PutMapping("/{id}")
    @LogExecution("Updated case: ")
    public String updateCase(@PathVariable String id, @RequestBody String name) {
        var entity = caseRepository.findById(id).orElse(null);
        if (entity == null) {
            return "Case not found";
        }
        entity.setDescription(name);
        caseRepository.save(entity);
        return "Case updated";
    }

    @DeleteMapping("/{id}")
    @LogExecution("Deleted case:")
    public ResponseEntity<Void> deleteCases(@PathVariable String id) {
        if (!caseRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        caseRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}