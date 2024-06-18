package com.example.artist.repository;

import com.example.artist.domain.Artists;
import org.springframework.data.jpa.repository.JpaRepository;
import java.util.Optional;

public interface ArtistRepository extends JpaRepository<Artists, Long> {
    Optional<Artists> findByArtistName(String artistName);
}
