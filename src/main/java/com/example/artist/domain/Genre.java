package com.example.artist.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity
@Table(name = "Genres")
@Getter
@Setter
public class Genre {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "username", unique = true, nullable = false)
    private String username;

    @Column(name = "Ballade", nullable = false)
    private boolean ballade = false;

    @Column(name = "Rock", nullable = false)
    private boolean rock = false;

    @Column(name = "Hiphop", nullable = false)
    private boolean hiphop = false;

    @Column(name = "Jazz", nullable = false)
    private boolean jazz = false;
}
