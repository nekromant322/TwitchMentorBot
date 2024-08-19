package com.nekromant.twitch.controller;

import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;

@Controller
public class PageController {


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
