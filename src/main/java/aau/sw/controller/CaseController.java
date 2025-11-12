package aau.sw.controller;

import aau.sw.aspect.LogExecution;
import aau.sw.model.Case;
import aau.sw.model.Comment;
import aau.sw.repository.CaseRepository;
import aau.sw.service.AuditableService;
import aau.sw.service.CaseService;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.cache.concurrent.ConcurrentMapCacheManager;
import org.springframework.web.bind.annotation.*;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.util.Date;
import java.util.List;


@RestController
@RequestMapping("/api/cases")
public class CaseController {

    private final CaseService caseService;

    @Autowired
    private AuditableService auditableService;

    public CaseController(CaseService caseService){
        this.caseService = caseService;
    }

    @Autowired
    private CaseRepository caseRepository;


    @PostMapping
    @LogExecution("Created new case")
    public ResponseEntity<Case> createCase(@RequestBody Case newCase) {
        Case created = caseService.createCase(newCase);
        return ResponseEntity.status(HttpStatus.CREATED).body(created);
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

    @PutMapping("/{id}/comment")
    @LogExecution("Added comment to case: ")
    public ResponseEntity<String> addComment(@PathVariable String id, @RequestBody Comment newComment) {
        var entity = caseRepository.findById(id).orElse(null);
        if (entity == null) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Case not found");
        }
        newComment.setCreatedAt(new Date());
        auditableService.setCreatedBy(newComment);
        entity.getComments().add(newComment);

        caseRepository.save(entity);

        return ResponseEntity.ok("Comment added successfully");
    }
}