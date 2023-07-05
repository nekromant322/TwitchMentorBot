package com.nekromant.twitch.schedule;

import com.nekromant.twitch.service.TwitchCommandTimerService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class CommandSchedule {
    @Autowired
    private TwitchCommandTimerService twitchCommandTimerService;

    @Scheduled(fixedDelayString = "${schedule.periodExecutedCommandsByTime}")
    public void executedCommandsByTime() {
        twitchCommandTimerService.addCommandToLinkedHashSet();
    }

    @Scheduled(fixedDelayString = "${schedule.periodGetExecutedCommandsByTime}")
    public void getExecutedCommandsByTime() {
        twitchCommandTimerService.getCommandFromLinkedHashSet();
    }
}
