package com.nekromant.twitch.controller.rest;

import com.nekromant.twitch.dto.PixelDTO;
import com.nekromant.twitch.dto.TableSizeDTO;
import com.nekromant.twitch.service.PixelStateService;
import com.nekromant.twitch.service.RedeemedPixelsService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/pixel")
public class PixelController {
    @Autowired
    private RedeemedPixelsService redeemedPixelsService;
    @Autowired
    private PixelStateService pixelStateService;

    @GetMapping("/redeemed-pixels")
    public Integer getRedeemedPixelsCountByToken(@RequestParam(value = "token") String accessToken) {
        return redeemedPixelsService.getRedeemedPixelsCountByToken(accessToken);
    }

    @GetMapping("/size")
    public TableSizeDTO getTableSize() {
        return pixelStateService.getTableSize();
    }

    @PostMapping("/editor")
    public void savePixel(@RequestBody PixelDTO pixelDTO, @RequestParam(value = "token") String accessToken) {
        if (pixelStateService.savePixel(pixelDTO, accessToken) != null) {
            redeemedPixelsService.takeRedeemedPixel(accessToken);
        }
    }

    @GetMapping("/matrix")
    public String getPixelMatrix()  {
        return pixelStateService.getPixelMatrix();
    }
}
