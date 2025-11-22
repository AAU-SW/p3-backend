package aau.sw.controller;

import aau.sw.aspect.LogExecution;
import aau.sw.model.Case;
import aau.sw.model.Comment;
import aau.sw.model.Image;
import aau.sw.repository.CaseRepository;
import aau.sw.service.AuditableService;
import aau.sw.repository.ImageRepository;
import aau.sw.service.CaseService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.Date;
import java.io.IOException;
import java.util.Arrays;
import java.util.List;


@RestController
@RequestMapping("/api/cases")
public class CaseController {

    private final CaseService caseService;
    @Autowired
    private ImageRepository imageRepository;

    @Autowired
    private AuditableService auditableService;

    public CaseController(CaseService caseService){
        this.caseService = caseService;
    }

    @Autowired
    private CaseRepository caseRepository;

    private static final List<String> ALLOWED_CONTENT_TYPES = Arrays.asList(
            "image/jpeg",
            "image/jpg",
            "image/png",
            "image/gif",
            "image/webp",
            "application/pdf"
    );


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
    public String updateCase(@PathVariable String id, @RequestBody Case updatedCase) {
        var entity = caseRepository.findById(id).orElse(null);
        if (entity == null) {
            return "Case not found";
        }
        entity.setDescription(updatedCase.getDescription());
        entity.setTitle(updatedCase.getTitle());
        entity.setStatus(updatedCase.getStatus());
        entity.setLocation(updatedCase.getLocation());
        entity.setAssignedTo(updatedCase.getAssignedTo());
        entity.setDueDate(updatedCase.getDueDate());
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

    @PostMapping("/{caseId}/images")
    public ResponseEntity<?> uploadFileToCase(
            @PathVariable String caseId,
            @RequestParam("file") MultipartFile file) {

        try {
            // Validate file
            if (file.isEmpty()) {
                return ResponseEntity.badRequest().body("File is empty");
            }

            // Validate file type
            String contentType = file.getContentType();
            if (contentType == null || !ALLOWED_CONTENT_TYPES.contains(contentType.toLowerCase())) {
                return ResponseEntity.badRequest()
                        .body("File must be an image (JPEG, PNG, GIF, WebP) or PDF");
            }

            // Find the case
            Case existingCase = caseRepository.findById(caseId)
                    .orElseThrow(() -> new RuntimeException("Case not found with id: " + caseId));

            // Upload image/file
            Image uploadedImage = caseService.uploadFileToCase(existingCase, file);

            return ResponseEntity.status(HttpStatus.CREATED).body(uploadedImage);

        } catch (RuntimeException e) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(e.getMessage());
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }

    @GetMapping("/files")
    @LogExecution("Fetched file:")
    public List<Image> getAllCaseFiles(@RequestParam(required = true) Case caseId) {
        return imageRepository.findByConnectedCaseId(caseId);
    }
}