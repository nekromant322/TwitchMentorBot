package com.nekromant.twitch.controller.rest;

import com.nekromant.twitch.dto.DailyBonusDTO;
import com.nekromant.twitch.dto.PointAucObjectDTO;
import com.nekromant.twitch.service.DailyBonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/bonuses")
public class DailyBonusController {

    @Autowired
    private DailyBonusService dailyBonusService;

    @GetMapping
    public List<DailyBonusDTO> dailyBonusDTOList() {
        return dailyBonusService.getDailyBonusDTOList();
    }

    @GetMapping("/file")
    public List<PointAucObjectDTO> pointAucObjectDTOList() {
        return dailyBonusService.getPointAucObjectDTOList();
    }
}
