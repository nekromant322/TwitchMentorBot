package com.nekromant.twitch.service;

import com.github.twitch4j.TwitchClient;
import com.nekromant.twitch.TwitchChatBot;
import com.nekromant.twitch.model.TwitchCommand;
import com.nekromant.twitch.repository.TwitchCommandRepository;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.time.Duration;
import java.time.Instant;
import java.util.Optional;

@Slf4j
@Setter
@NoArgsConstructor
@Component
public class TwitchCommandTimerService {

    @Autowired
    private TwitchChatBot twitchChatBot;

    @Autowired
    private TwitchCommandRepository twitchCommandRepository;

    @Autowired
    private TwitchLiveService twitchLiveService;

    @Value("${twitch.channelName}")
    private String channelName;

    public void sendCommands() {
        if (twitchLiveService.isLiveStream()) {
            Optional<TwitchCommand> twitchCommand = twitchCommandRepository
                    .getTwitchCommandsSortedByLastCompletionTimeAndPeriodWithEnabledTrueAndPeriodNotZero()
                    .stream()
                    .filter(tc -> Duration.between(tc.getLastCompletionTime(), Instant.now()).toMinutes() > tc.getPeriod())
                    .findFirst();
            if (twitchCommand.isPresent()) {
                TwitchClient twitchClient = twitchChatBot.getTwitchClient();
                twitchClient.getChat().sendMessage(channelName, twitchCommand.get().getResponse());

                twitchCommand.get().setLastCompletionTime(Instant.now());
                twitchCommandRepository.save(twitchCommand.get());
                log.info("Running task for command: !" + twitchCommand.get().getName());
            }
        }
    }
}