package com.example.artist.service;

import com.example.artist.domain.Genre;
import com.example.artist.repository.GenreRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class GenreService {

    @Autowired
    private GenreRepository genreRepository;

    public void saveUserGenres(String username, List<String> genres) {
        Genre genre = genreRepository.findByUsername(username).orElseThrow(() -> new RuntimeException("User not found"));

        genre.setBallade(genres.contains("Ballade"));
        genre.setRock(genres.contains("Rock"));
        genre.setHiphop(genres.contains("Hiphop"));
        genre.setJazz(genres.contains("Jazz"));

        genreRepository.save(genre);
    }
}
