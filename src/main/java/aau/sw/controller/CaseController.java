package aau.sw.controller;

import aau.sw.dto.CaseReq;
import aau.sw.dto.RegisterReq;
import aau.sw.model.Case;
import aau.sw.repository.CaseRepository;
import jakarta.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.net.URI;
import java.time.LocalDateTime;
import java.util.List;
import org.springframework.web.bind.annotation.PutMapping;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    @Autowired
    private CaseRepository caseRepository;

    @PostMapping
    public Case createCase(@RequestBody Case newCase) {
        return caseRepository.save(newCase);
    }

    @PostMapping("/case")
    public ResponseEntity<?> createCase(@Valid @RequestBody CaseReq req) {
        var c = new Case();
        c.setTitle(req.title());
        c.setStatus(req.status());
        caseRepository.save(c);
        return ResponseEntity.status(HttpStatus.CREATED).body(c);
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

    @PutMapping("/{id}")
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
    public ResponseEntity<Void> deleteCases(@PathVariable String id) {
        if (!caseRepository.existsById(id)) {
            return ResponseEntity.notFound().build();
        }
        caseRepository.deleteById(id);
        return ResponseEntity.noContent().build();
    }
}