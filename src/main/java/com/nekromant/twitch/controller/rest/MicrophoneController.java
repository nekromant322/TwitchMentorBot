package com.nekromant.twitch.controller.rest;

import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/micro")
@CrossOrigin(origins = "*")
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

    @GetMapping("/toggle")
    public void toggle() {
       microEnabled = !microEnabled;
    }

    @GetMapping("/getState")
    public boolean getState() {
        return microEnabled;
    }
}
