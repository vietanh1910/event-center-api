package com.fpt.etc.controller;

import com.fpt.etc.services.CloudinaryService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/api/upload")
@RequiredArgsConstructor
public class UploadController {

    private final CloudinaryService cloudinaryService;

    @PostMapping
    public ResponseEntity<String> upload(@RequestParam("file") MultipartFile file) {
        try {
            String url = cloudinaryService.uploadFile(file);
            return ResponseEntity.ok(url);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Upload failed: " + e.getMessage());
        }
    }

    @DeleteMapping
    public ResponseEntity<String> delete(@RequestParam("publicId") String publicId) {
        try {
            String result = cloudinaryService.deleteFile(publicId);
            return ResponseEntity.ok("Delete result: " + result);
        } catch (Exception e) {
            return ResponseEntity.status(500).body("Delete failed: " + e.getMessage());
        }
    }
}
