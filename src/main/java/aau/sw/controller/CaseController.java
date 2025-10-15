package aau.sw.controller;

import aau.sw.model.Case;
import aau.sw.repository.CaseRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api/cases")
public class CaseController {

    @Autowired
    private CaseRepository caseRepository;

    @PostMapping
    public Case createCase(@RequestBody Case newCase) {
        return caseRepository.save(newCase);

    @GetMapping

    }
}