package com.nekromant.twitch.controller;

import com.nekromant.twitch.cache.TwitchTokenUserCache;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class PageController {


    @GetMapping("/incomingReviewWidget")
    public String getIncomingReviewWidget(@RequestParam(required = false) String color) {
        return "incomingReviewWidget.html";
    }

    @GetMapping("/commands")
    public String getTwitchBotPanel() {
        return "/botCommandsPanel.html";
    }

    @GetMapping("/login")
    public String getLoginPage() {
        return "/login.html";
    }

    @GetMapping("/songList/editor")
    public String getSongListEditor() {
        return "/songListEditor.html";
    }

    @GetMapping("/songList/panel")
    public String getSongListPanel() {
        return "/songListPanel.html";
    }

    @GetMapping("/dailyBonus")
    public String getDailyBonusPage() {
        return "/dailyBonusPage.html";
    }
}
