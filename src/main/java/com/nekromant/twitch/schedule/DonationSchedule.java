package com.nekromant.twitch.schedule;

import com.nekromant.twitch.service.DonatService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class DonationSchedule {
    @Autowired
    private DonatService donatService;

    @Scheduled(fixedDelayString = "#{@scheduleConfigProperties.periodAddKindnessForDonat}")
    public void addKindnessForDonat() {
        donatService.addKindnessForDonat();
    }
}
