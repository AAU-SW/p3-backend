package aau.sw.controller;

import java.io.IOException;
import java.util.List;

import aau.sw.model.Image;
import aau.sw.service.ImageStorageService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/images")
public class ImageStorageController {

    @Autowired
    ImageStorageService service;

    @GetMapping
    public ResponseEntity<List<Image>> getImages() {
        return ResponseEntity.ok(service.getImage());
    }

    @PostMapping
    public ResponseEntity<Void> saveImage(@RequestParam(value = "image") MultipartFile image) {
        try {
            service.saveFile(image);
            return ResponseEntity.status(HttpStatus.CREATED).build();
        } catch (IOException e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).build();
        }
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteById(@PathVariable String id) {
        try {
            service.deleteFile(Long.valueOf(id));
            return ResponseEntity.noContent().build();
        } catch (Exception e) {
            return ResponseEntity.notFound().build();
        }
    }
}