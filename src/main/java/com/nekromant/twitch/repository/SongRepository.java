package com.nekromant.twitch.repository;

import com.nekromant.twitch.model.Song;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SongRepository extends JpaRepository<Song, Long> {
    @Query(value = "SELECT * FROM songs s ORDER BY s.artist, s.name", nativeQuery = true)
    List<Song> findAllOrderedByArtistAndName();
}
