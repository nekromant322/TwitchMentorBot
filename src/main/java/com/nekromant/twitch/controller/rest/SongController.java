package com.nekromant.twitch.controller.rest;

import com.nekromant.twitch.dto.SongDTO;
import com.nekromant.twitch.model.Song;
import com.nekromant.twitch.service.SongService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/songs")
public class SongController {
    @Autowired
    private SongService songService;

    @GetMapping("/list")
    public List<Song> songList() {
        return songService.getAllSongs();
    }

    @PostMapping
    public void create(@RequestBody SongDTO songDTO) {
        songService.save(songDTO);
    }

    @GetMapping("/{id}")
    public SongDTO getSong(@PathVariable Long id) {
        return songService.getSongById(id);
    }

    @PostMapping("/{id}")
    public void editSong(@RequestBody SongDTO songDTO, @PathVariable Long id) {
        songService.update(songDTO, id);
    }

    @DeleteMapping("/{id}")
    public void delete(@PathVariable Long id) {
        songService.delete(id);
    }
}