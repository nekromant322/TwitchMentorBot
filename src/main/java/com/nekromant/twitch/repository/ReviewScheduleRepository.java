package com.nekromant.twitch.repository;

import com.nekromant.twitch.model.ReviewSchedule;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;

@Repository
public interface ReviewScheduleRepository extends CrudRepository<ReviewSchedule, Long> {
}
