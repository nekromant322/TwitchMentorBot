package com.nekromant.twitch.repository;

import com.nekromant.twitch.model.TwitchCommand;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TwitchCommandRepository extends CrudRepository<TwitchCommand, Long> {
    TwitchCommand findByName(String name);

    @Query(value = "SELECT * FROM twitch_commands WHERE period <> 0 AND enabled = TRUE " +
            "AND response IS NOT NULL ORDER BY last_completion_time, period", nativeQuery = true)
    List<TwitchCommand> getTwitchCommandsSortedByLastCompletionTimeAndPeriodWithEnabledTrueAndPeriodNotZeroAndResponseIsNotNull();
}
