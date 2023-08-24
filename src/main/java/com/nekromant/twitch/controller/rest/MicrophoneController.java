package com.nekromant.twitch.controller.rest;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
public class MicrophoneController {

    private boolean microEnabled = true;

    @GetMapping("/muted")
    public void muted() {
        microEnabled = false;
    }

    @GetMapping("/unmuted")
    public void unmuted() {
        microEnabled = true;
    }

    @GetMapping("/getState")
    public boolean getState() {
        return microEnabled;
    }
}
