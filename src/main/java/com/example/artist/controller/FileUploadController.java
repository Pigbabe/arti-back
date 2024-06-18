package com.example.artist.controller;

import com.example.artist.service.S3Service;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;

@RestController
@RequestMapping("/api/files")
public class FileUploadController {

    private final S3Service s3Service;


    @Autowired
    public FileUploadController(S3Service s3Service) {
        this.s3Service = s3Service;
    }
    @PostMapping("/artists/{name}/images")
    public ResponseEntity<String> uploadArtistImage(@PathVariable String name, @RequestParam("file") MultipartFile file) {
        s3Service.uploadFile(name, file);
        return new ResponseEntity<>("File uploaded successfully", HttpStatus.OK);
    }




    @GetMapping("/artists")
    public ResponseEntity<List<String>> getArtistFolders() {
        List<String> artistFolders = s3Service.getTopLevelFolders();
        return ResponseEntity.ok(artistFolders);
    }

    @GetMapping("/artists/{name}/images")
    public ResponseEntity<List<String>> getArtistImages(@PathVariable String name) {
        List<String> artistImages = s3Service.getArtistImages(name);
        return ResponseEntity.ok(artistImages);
    }
}
