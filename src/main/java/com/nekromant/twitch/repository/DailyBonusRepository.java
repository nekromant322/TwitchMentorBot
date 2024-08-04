package com.nekromant.twitch.repository;

import com.nekromant.twitch.model.DailyBonus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface DailyBonusRepository extends CrudRepository<DailyBonus, Long> {
    @Query(value = "SELECT count(*) + 1 FROM daily_bonus db " +
            "WHERE db.points > (SELECT db.points FROM daily_bonus db WHERE db.twitch_user_id = :id)", nativeQuery = true)
    Long getPosition(@Param("id") Long id);

    @Query(value = "SELECT * FROM daily_bonus ORDER BY points DESC, twitch_user_id ASC", nativeQuery = true)
    List<DailyBonus> findAllOrderedByBonusAndTwitchUser();
}