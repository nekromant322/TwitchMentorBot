package com.nekromant.twitch.service;

import com.nekromant.twitch.model.ReviewSchedule;
import com.nekromant.twitch.repository.ReviewScheduleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Service
public class ReviewScheduleService {
    @Autowired
    private ReviewScheduleRepository reviewScheduleRepository;

    public void saveSchedule(String newSchedule) {
        LocalDate today = LocalDate.now();
        LocalDateTime start = today.atTime(18, 0);
        LocalDateTime end = today.plusDays(1L).atTime(03, 0);
        ReviewSchedule reviewSchedule = new ReviewSchedule();
        reviewSchedule.setId(1L);
        reviewSchedule.setReviews(newSchedule);
        reviewSchedule.setStartTime(start);
        reviewSchedule.setEndTime(end);
        reviewScheduleRepository.save(reviewSchedule);
    }

    public ReviewSchedule findTodayUpdatedSchedule() {
        ReviewSchedule reviewSchedule = reviewScheduleRepository.findById(1L).orElse(null);
        LocalDateTime now = LocalDateTime.now();
        if (reviewSchedule != null && now.isBefore(reviewSchedule.getEndTime())) {
            return reviewSchedule;
        }
        return null;
    }
}
