package com.nekromant.twitch.repository;

import com.nekromant.twitch.model.DailyBonus;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.CrudRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface DailyBonusRepository extends CrudRepository<DailyBonus, Long> {
    @Query(value = "WITH ranking AS ( SELECT db.points, db.twitch_user_id, " +
            "row_number() OVER(ORDER BY db.points DESC, db.twitch_user_id ASC) AS pos FROM daily_bonus db) " +
            "SELECT r.pos FROM ranking r WHERE r.twitch_user_id = :id", nativeQuery = true)
    Long getPosition(@Param("id") Long id);
}