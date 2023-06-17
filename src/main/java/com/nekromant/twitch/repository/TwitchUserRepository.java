package com.nekromant.twitch.repository;

import com.nekromant.twitch.model.TwitchUser;
import org.springframework.data.repository.CrudRepository;

public interface TwitchUserRepository extends CrudRepository<TwitchUser, Long> {
}
