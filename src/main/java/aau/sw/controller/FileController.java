package aau.sw.controller;

import aau.sw.service.FileService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import software.amazon.awssdk.services.s3.model.S3Exception;

import java.io.IOException;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping("/upload")
    public ResponseEntity<String> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            String objectKey = file.getOriginalFilename(); // use the file's name
            if (objectKey == null || objectKey.isBlank()) {
                objectKey = "default-file"; // fallback name
            }
            fileService.putObject(file.getBytes(), fileService.getBucketName(), objectKey);
            return ResponseEntity.ok("File uploaded successfully as " + objectKey);
        } catch (S3Exception | IOException e) {
            e.printStackTrace();
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body("Failed to upload file: " + e.getMessage());
        }
    }

    @GetMapping("/url")
    public ResponseEntity<String> getFileUrl(@RequestParam("filename") String filename) {
        if (filename == null || filename.isBlank()) {
            return ResponseEntity.badRequest().body("Filename is required");
        }

        try {
            String fileUrl = fileService.getFileUrl(filename);
            return ResponseEntity.ok(fileUrl);
        } catch (Exception e) {
            e.printStackTrace();
            return ResponseEntity.status(500).body("Failed to generate URL: " + e.getMessage());
        }
    }

}
