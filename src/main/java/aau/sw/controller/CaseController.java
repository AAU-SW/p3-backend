package aau.sw.controller;

import aau.sw.model.Case;
import aau.sw.repository.CaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.http.ResponseEntity;

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

    @GetMapping
    public List<Case> getCases() {
        return caseRepository.findAll();

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