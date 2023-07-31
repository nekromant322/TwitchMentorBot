package com.nekromant.twitch.model;

import lombok.*;

import javax.persistence.*;

@Entity(name = "songs")
@Builder
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Song {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column
    private String name;

    @Column
    private String artist;

    @Column
    private String chordsLink;

    @Column
    private String comment;

    public Song(String name, String artist, String chordsLink, String comment) {
        this.name = name;
        this.artist = artist;
        this.chordsLink = chordsLink;
        this.comment = comment;
    }
}