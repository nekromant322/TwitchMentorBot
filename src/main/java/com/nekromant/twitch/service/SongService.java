package com.nekromant.twitch.service;

import com.nekromant.twitch.dto.SongDTO;
import com.nekromant.twitch.mapper.SongMapper;
import com.nekromant.twitch.model.Song;
import com.nekromant.twitch.repository.SongRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.stream.Collectors;

@Service
public class SongService {
    @Autowired
    private SongRepository songRepository;
    @Autowired
    private SongMapper songMapper;

    public List<SongDTO> getAllSongsDTO() {
        return songRepository.findAll().stream()
                .map(SongMapper::mapToSongDTO)
                .collect(Collectors.toList());
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

    public SongDTO getSongDTOById(Long id) {
        return SongMapper.mapToSongDTO(getSongById(id));
    }

    public Song getSongById(Long id) {
        return songRepository.findById(id).get();
    }
}
