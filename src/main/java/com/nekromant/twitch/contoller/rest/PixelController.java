package com.nekromant.twitch.contoller.rest;

import com.nekromant.twitch.dto.PixelDTO;
import com.nekromant.twitch.service.PixelStateService;
import com.nekromant.twitch.service.RedeemedPixelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
public class PixelController {
    @Autowired
    private RedeemedPixelsService redeemedPixelsService;
    @Autowired
    private PixelStateService pixelStateService;

    @GetMapping("/pixel/redeemed-pixels")
    public Integer getRedeemedPixelsCountByToken(@RequestParam(value = "token") String accessToken) {
        Integer redeemedPixelsCount = redeemedPixelsService.getRedeemedPixelsCountByToken(accessToken);
        return redeemedPixelsCount;
    }

    @PostMapping("/pixel/editor")
    public void savePixel(@RequestBody PixelDTO pixelDTO, @RequestParam(value = "token") String accessToken) {
        pixelStateService.savePixel(pixelDTO, accessToken);
        redeemedPixelsService.takeRedeemedPixel(accessToken);
    }

    @GetMapping("/pixel/matrix")
    public String getPixelMatrix() {
        return pixelStateService.getPixelMatrix();
    }
}