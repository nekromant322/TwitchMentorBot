package com.nekromant.twitch.contoller;


import com.nekromant.twitch.dto.PixelDTO;
import com.nekromant.twitch.service.PixelStateService;
import org.springframework.beans.factory.annotation.Autowired;
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
