package com.nekromant.twitch.controller;


import com.nekromant.twitch.dto.PixelDTO;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.stereotype.Controller;


@Controller
public class WebSocketController {
    @MessageMapping("/edit")
    @SendTo("/edit")
    public PixelDTO sendPixel(PixelDTO pixel) {
        return pixel;
    }
}
