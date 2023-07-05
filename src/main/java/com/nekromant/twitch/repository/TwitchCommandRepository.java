package com.nekromant.twitch.repository;

import com.nekromant.twitch.model.TwitchCommand;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface TwitchCommandRepository extends CrudRepository<TwitchCommand, Long> {
    TwitchCommand findByName(String name);

    List<TwitchCommand> findAllByPeriodNotAndEnabledIsTrue(Long period);

    @Query(value = "SELECT * FROM twitch_commands WHERE period <> :period AND enabled = TRUE " +
            "ORDER BY last_completion_time DESC", nativeQuery = true)
    List<TwitchCommand> getTwiCommandsSortedByLastCompletionTimeWithEnabledTrueAndPeriodNot(@Param("period") Long period);

    @Query(value = "SELECT * FROM twitch_commands WHERE period <> :period AND enabled = TRUE " +
            "ORDER BY period, last_completion_time", nativeQuery = true)
    List<TwitchCommand> getTwitchCommandsSortedByPeriodWithEnabledTrueAndPeriodNot(@Param("period") Long period);
}
