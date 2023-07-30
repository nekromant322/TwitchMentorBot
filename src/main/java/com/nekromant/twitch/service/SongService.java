package com.nekromant.twitch.service;

import com.nekromant.twitch.dto.SongDTO;
import com.nekromant.twitch.mapper.SongMapper;
import com.nekromant.twitch.model.Song;
import com.nekromant.twitch.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class SongService {

    @Autowired
    private SongRepository songRepository;

    @Autowired
    private SongMapper songMapper;

    public List<Song> getAllSongs() {
        return songRepository.findAll();
    }

    public void save(SongDTO songDTO) {
        Song song = songMapper.mapToSong(songDTO);
        songRepository.save(song);
    }

    public void update(SongDTO songDTO, Long id) {
        Song existingSong = songRepository.findById(id).get();
        existingSong.setArtist(songDTO.getArtist());
        existingSong.setName(songDTO.getName());
        existingSong.setChordsLink(songDTO.getChordsLink());
        existingSong.setComment(songDTO.getComment());
        songRepository.save(existingSong);
    }

    public void delete(Long id) {
        songRepository.deleteById(id);
    }

    public SongDTO getSongById(Long id) {
        Song song = songRepository.findById(id).get();
        return songMapper.mapToSongDTO(song);
    }
}
