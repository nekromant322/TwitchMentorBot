package com.nekromant.twitch.repository;

import com.nekromant.twitch.model.TwitchToken;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface TwitchTokenRepository extends CrudRepository<TwitchToken, Long> {
    TwitchToken findByType(String type);
}
