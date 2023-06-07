package com.nekromant.twitch.service;

import com.nekromant.twitch.TwitchClientHolder;
import com.nekromant.twitch.model.TwitchCommand;
import com.nekromant.twitch.repository.TwitchCommandRepository;
import com.nekromant.twitch.task.TwitchCommandTimerTask;
import lombok.Setter;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;
import java.util.concurrent.Executors;
import java.util.concurrent.ScheduledExecutorService;
import java.util.concurrent.TimeUnit;

@Setter
@Component
public class TwitchCommandTimerService {
    @Autowired
    private TwitchCommandRepository twitchCommandRepository;
    @Autowired
    private TwitchAuthService twitchAuthService;
    @Autowired
    private TwitchClientHolder twitchClientHolder;
    @Value("${twitch.channelName}")
    private String channelName;
    private Long NOT_PERIOD_EXECUTION = 0L;
    private final HashMap<Long, ScheduledExecutorService> hashMap = new HashMap<>();

    public TwitchCommandTimerService() {
    }

    public void executedCommandsByTime() {
        List<TwitchCommand> allTwitchCommands =
                twitchCommandRepository.findAllByPeriodNotAndEnabledIsTrue(NOT_PERIOD_EXECUTION);
        for (TwitchCommand twitchCommand : allTwitchCommands) {
            createTimerTask(twitchCommand);
        }
    }

    public void createTimerTask(TwitchCommand twitchCommand) {
        if (validateCommand(twitchCommand)) {
            ScheduledExecutorService scheduler = Executors.newSingleThreadScheduledExecutor();
            scheduler.scheduleAtFixedRate
                    (new TwitchCommandTimerTask(twitchClientHolder, channelName, twitchCommand, twitchAuthService),
                            0, twitchCommand.getPeriod(), TimeUnit.MINUTES);
            hashMap.put(twitchCommand.getId(), scheduler);
        }
    }

    public void updateTimerTask(TwitchCommand twitchCommand) {
        deleteTimerTask(twitchCommand.getId());
        createTimerTask(twitchCommand);
    }

    public void deleteTimerTask(Long id) {
        ScheduledExecutorService schedulerCommand = hashMap.get(id);
        if (schedulerCommand != null) {
            schedulerCommand.shutdown();
            hashMap.remove(id);
        }
    }

    private boolean validateCommand(TwitchCommand twitchCommand) {
        return twitchCommand.isEnabled() && !twitchCommand.getPeriod().equals(NOT_PERIOD_EXECUTION);
    }
}
