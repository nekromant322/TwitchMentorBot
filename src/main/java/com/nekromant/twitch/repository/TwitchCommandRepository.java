package com.nekromant.twitch.repository;

import com.nekromant.twitch.model.TwitchCommand;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TwitchCommandRepository extends CrudRepository<TwitchCommand, Long> {
    TwitchCommand findByName(String name);

    List<TwitchCommand> findAllByPeriodNotAndEnabledIsTrue(Long period);
}
