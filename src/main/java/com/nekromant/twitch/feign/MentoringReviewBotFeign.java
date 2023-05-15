package com.nekromant.twitch.feign;

import com.nekromant.twitch.dto.BookedReviewDTO;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;

@FeignClient(name = "mentoringReviewBot", url = "${telegram.reviewBotHost}")
public interface MentoringReviewBotFeign {

    @GetMapping(value = "/incoming-review-with-period")
    List<BookedReviewDTO> getIncomingReview(@RequestParam(value = "mentor") String mentorTelegramUsername);
}
