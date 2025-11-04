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

    // curl -X POST http://localhost:8080/api/files/upload -H "Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJtYXRoYXZzMDgxMEBnbWFpbC5jb20iLCJpYXQiOjE3NjIyOTUxNDMsImV4cCI6MTc2MjI5NTc0M30.3E2LI9vwubx_P2DRnGqpdFLmQm3f6IADr02V9Y8A0AC-GEoXwi1yoStYX48RV2nf" -F "file=@/Users/mathiass/Desktop/test.png"

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

    // curl -H "Authorization: Bearer eyJhbGciOiJIUzM4NCJ9.eyJzdWIiOiJtYXRoYXZzMDgxMEBnbWFpbC5jb20iLCJpYXQiOjE3NjIyOTc0ODMsImV4cCI6MTc2MjI5ODA4M30.uZ4XqjMECIVV50JMoTGVqrb0hXQ08WsM1tGJ0CVRFwUNQxOoiEdclfDG_QNvFohZ" "http://localhost:8080/api/files/url?filename=test.png"

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
