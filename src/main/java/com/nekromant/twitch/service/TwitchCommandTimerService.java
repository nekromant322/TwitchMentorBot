package com.nekromant.twitch.service;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.helix.domain.Stream;
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
import java.time.temporal.ChronoUnit;
import java.util.Collections;
import java.util.LinkedHashSet;
import java.util.List;
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
    private TwitchAuthService twitchAuthService;
    @Value("${twitch.channelName}")
    private String channelName;
    @Value("${timeSlots.allowedTimeIntervalInMinutes}")
    private Integer ALLOWED_TIME_INTERVAL_IN_MINUTES;
    private LinkedHashSet<TwitchCommand> linkedHashSet = new LinkedHashSet<>();

    public void linkedHashSetManagement() {
        TwitchClient twitchClient = twitchChatBot.getTwitchClient();
        if (isLiveStream(twitchClient)) {
            fillLinkedHashSet(commandsWereNotSentWithinTheAllowedTime());
        } else {
            cleanLinkedHashSet();
        }
    }

    public void sendCommandFromLinkedHashSet() {
        TwitchClient twitchClient = twitchChatBot.getTwitchClient();
        Optional<TwitchCommand> twitchCommand = linkedHashSet.stream().findFirst();
        if (isLiveStream(twitchClient) && twitchCommand.isPresent()) {
            twitchClient.getChat().sendMessage(channelName, twitchCommand.get().getResponse());
            linkedHashSet.remove(twitchCommand.get());
            twitchCommand.get().setLastCompletionTime(Instant.now());
            twitchCommandRepository.save(twitchCommand.get());
            log.info("Running task for command: !" + twitchCommand.get().getName());
        }
    }

    private boolean isLiveStream(TwitchClient twitchClient) {
        List<Stream> streams;
        try {
            streams = getStreams(twitchClient);
        } catch (Exception e) {
            log.error(e.getMessage());
            twitchChatBot.validateConnection();
            streams = getStreams(twitchChatBot.getTwitchClient());
            return !streams.isEmpty();
        }
        return !streams.isEmpty();
    }

    private List<Stream> getStreams(TwitchClient twitchClient) {
        return twitchClient.getHelix().getStreams(twitchAuthService.getAuthToken()
                                .getAccessToken(), null, null, 1, null, null, null,
                        Collections.singletonList(channelName))
                .execute().getStreams();
    }

    private boolean commandsWereNotSentWithinTheAllowedTime() {
        return twitchCommandRepository
                .getTwitchCommandsSortedByPeriodWithEnabledTrueAndPeriodNotZero()
                .stream()
                .noneMatch(tc -> Duration.between(tc.getLastCompletionTime(), Instant.now()).toMinutes()
                        < ALLOWED_TIME_INTERVAL_IN_MINUTES);
    }

    private void fillLinkedHashSet(boolean isNewStream) {
        List<TwitchCommand> twitchCommands = twitchCommandRepository
                .getTwitchCommandsSortedByPeriodWithEnabledTrueAndPeriodNotZero();
        if (isNewStream) {
            twitchCommands.forEach(tc -> tc.setLastCompletionTime(Instant.now()));
            twitchCommandRepository.saveAll(twitchCommands);
        }
        twitchCommands.forEach(this::addCommandsToLinkedHashSet);
    }

    private void addCommandsToLinkedHashSet(TwitchCommand twitchCommand) {
        if (Math.abs(Duration.between(twitchCommand.getLastCompletionTime(), Instant.now())
                .minus(twitchCommand.getPeriod(), ChronoUnit.MINUTES).toMinutes())
                <= ALLOWED_TIME_INTERVAL_IN_MINUTES) {
            linkedHashSet.add(twitchCommand);
        }
    }

    private void cleanLinkedHashSet() {
        LinkedHashSet<TwitchCommand> twitchCommandLinkedHashSet = linkedHashSet;
        if (!twitchCommandLinkedHashSet.isEmpty()) {
            linkedHashSet.removeAll(twitchCommandLinkedHashSet);
        }
    }
}