package com.example.artist.controller;

import com.example.artist.service.GenreService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/api/genres")
public class GenreController {

    @Autowired
    private GenreService genreService;

    @PostMapping
    public ResponseEntity<?> saveUserGenres(@RequestBody List<String> genres, @RequestParam String username) {
        try {
            genreService.saveUserGenres(username, genres);
            return ResponseEntity.ok("Genres saved successfully.");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Failed to save genres.");
        }
    }
}
