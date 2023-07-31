package com.nekromant.twitch.repository;

import com.nekromant.twitch.model.DailyBonus;
import org.springframework.data.repository.CrudRepository;

public interface DailyBonusRepository extends CrudRepository<DailyBonus, Long> {
}
