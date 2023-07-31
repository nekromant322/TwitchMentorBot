package com.nekromant.twitch.mapper;

import com.nekromant.twitch.dto.SongDTO;
import com.nekromant.twitch.model.Song;
import org.springframework.stereotype.Component;

@Component
public class SongMapper {
    public Song mapToSong(SongDTO songDTO) {
        return Song.builder()
                .artist(songDTO.getArtist())
                .name(songDTO.getName())
                .chordsLink(songDTO.getChordsLink())
                .comment(songDTO.getComment())
                .build();
    }

    public static SongDTO mapToSongDTO(Song song) {
        return SongDTO.builder()
                .id(song.getId())
                .artist(song.getArtist())
                .name(song.getName())
                .chordsLink(song.getChordsLink())
                .comment(song.getComment())
                .build();
    }
}
