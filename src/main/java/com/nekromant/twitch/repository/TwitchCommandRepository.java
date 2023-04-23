package com.nekromant.twitch.repository;

import com.nekromant.twitch.model.TwitchCommand;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwitchCommandRepository extends CrudRepository<TwitchCommand, Long> {
    TwitchCommand findByName(String name);
}
