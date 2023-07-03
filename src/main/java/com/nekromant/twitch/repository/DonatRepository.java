package com.nekromant.twitch.repository;

import com.nekromant.twitch.model.Donat;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface DonatRepository extends CrudRepository<Donat, Long> {
    @Query(value = "SELECT * FROM donat d ORDER BY d.time_donat DESC LIMIT 1", nativeQuery = true)
    Donat getLastDonat();
}
