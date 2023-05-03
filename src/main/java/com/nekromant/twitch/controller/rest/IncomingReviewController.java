package com.nekromant.twitch.controller.rest;

import com.nekromant.twitch.dto.BookedReviewDTO;
import com.nekromant.twitch.feign.MentoringReviewBotFeign;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class IncomingReviewController {
    @Autowired
    private MentoringReviewBotFeign mentoringReviewBotFeign;
    @Value("${telegram.mentorUsername}")
    private String mentorTelegramUsername;

    @GetMapping("/incoming-reviews")
    public List<BookedReviewDTO> getIncomingReview() {
        return mentoringReviewBotFeign.getIncomingReview(mentorTelegramUsername);
    }
}
