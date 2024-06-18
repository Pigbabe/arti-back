// 파일 위치: src/main/java/com/example/artist/service/ArtistService.java

package com.example.artist.service;

import com.example.artist.domain.Artists;
import com.example.artist.repository.ArtistRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import java.util.Optional;


@Service
public class ArtistService {

    private final ArtistRepository artistRepository;

    @Autowired
    public ArtistService(ArtistRepository artistRepository) {
        this.artistRepository = artistRepository;
    }

    public Optional<Artists> findArtistByName(String name) {
        return artistRepository.findByArtistName(name);
    }


}
