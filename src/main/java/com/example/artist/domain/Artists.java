package com.example.artist.domain;

import jakarta.persistence.*;
import lombok.Getter;
import lombok.Setter;

@Entity // 이 어노테이션은 클래스가 엔티티임을 나타냅니다.
@Table(name = "artists") // DB 테이블 이름 명시
@Getter
@Setter // Lombok을 사용하여 Getter와 Setter를 자동으로 생성
public class Artists {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "artist_name")
    private String artistName;

    @Column(name = "image_url") // 데이터베이스 컬럼 이름에 맞춤
    private String imageUrl;

    @Column(name = "song_url")
    private String songUrl;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getArtistName() {
        return artistName;
    }

    public void setName(String name) {
        this.artistName = name;
    }

    public String getImageUrl() {
        return imageUrl;
    }

    public void setImageUrl(String imageUrl) {
        this.imageUrl = imageUrl;
    }

    public String getMusicUrl() {
        return songUrl;
    }

    public void setMusicUrl(String musicUrl) {
        this.songUrl = musicUrl;
    }


    // 생성자, 게터, 세터 등 생략
}
