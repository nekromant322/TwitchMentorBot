package com.nekromant.twitch.contoller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {
    @Value("${twitch.pixelWarsClientId}")
    private String pixelWarsClientId;

    @Value("${twitch.pixelWarsRedirectUrl}")
    private String pixelWarsRedirectUrl;

    @GetMapping("/incomingReviewWidget")
    public String getIncomingReviewWidget(@RequestParam(required = false) String color) {
        return "incomingReviewWidget.html";
    }

    @GetMapping("/pixel")
    public String getAuthForPixelWars() {
        String redirectUrl = "https://id.twitch.tv/oauth2/authorize?response_type=token&client_id=" +
                pixelWarsClientId + "&redirect_uri=" + pixelWarsRedirectUrl + "&scope=user%3Aread%3Afollows";
        return "redirect:" + redirectUrl;
    }

    @GetMapping("/pixel/editor")
    public String getPixelEditor() {
        return "/pixelEditor.html";
    }

    @GetMapping("/pixel/panel")
    public String getPixelPanel() {
        return "/pixelPanel.html";
    }
}
