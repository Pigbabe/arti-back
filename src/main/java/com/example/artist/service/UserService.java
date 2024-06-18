package com.example.artist.service;

import com.example.artist.domain.Genre;
import com.example.artist.domain.User;
import com.example.artist.domain.Artists;
import com.example.artist.repository.ArtistRepository;
import com.example.artist.repository.GenreRepository;
import com.example.artist.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;


@Service
public class UserService {

    private static final Logger logger = LoggerFactory.getLogger(UserService.class);


    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ArtistRepository artistRepository;
    @Autowired
    private PasswordEncoder passwordEncoder;
    @Autowired
    private GenreRepository genreRepository;
    @Transactional
    public User registerUser(User user) {
        try {
            user.setPassword(passwordEncoder.encode(user.getPassword()));
            User savedUser = userRepository.save(user);
            logger.info("User saved: {}", savedUser);

            if (savedUser.getIsArtist()) {
                Artists artist = new Artists();
                artist.setArtistName(savedUser.getName());
                artist.setImageUrl(null);
                artist.setSongUrl(null);
                artistRepository.save(artist);
                logger.info("Artist saved: {}", artist);
            }
            Genre genre = new Genre();
            genre.setUsername(savedUser.getUsername());
            genreRepository.save(genre);
            logger.info("Genre saved for user: {}", savedUser.getUsername());

            return savedUser;
        } catch (Exception e) {
            logger.error("Error during registration: ", e);
            throw e;
        }
    }
    public boolean checkLogin(String username, String password) {
        User user = userRepository.findByUsername(username);
        if (user != null) {
            boolean passwordMatch = passwordEncoder.matches(password, user.getPassword());
            System.out.println("Password match: " + passwordMatch); // 로그 추가
            return passwordMatch;
        }
        System.out.println("User not found with username: " + username); // 로그 추가
        return false;
    }
}