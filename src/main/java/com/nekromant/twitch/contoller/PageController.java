package com.nekromant.twitch.contoller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {

    @GetMapping("/incomingReviewWidget")
    public String getIncomingReviewWidget(@RequestParam(required = false) String color) {
        return "incomingReviewWidget.html";
    }
}
