package com.nekromant.twitch.service;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.helix.domain.Stream;
import com.nekromant.twitch.TwitchChatBot;
import com.nekromant.twitch.config.properties.ScheduleConfigProperties;
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
import java.util.stream.Collectors;

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
    @Autowired
    private ScheduleConfigProperties scheduleConfigProperties;

    private int CONFIDENCE_TIME_INTERVAL_IN_MINUTES = 1;

    private LinkedHashSet<TwitchCommand> linkedHashSet = new LinkedHashSet<>();

    public void linkedHashSetManagement() {
        TwitchClient twitchClient = twitchChatBot.getTwitchClient();
        if (isLiveStream(twitchClient)) {
            fillLinkedHashSet();
        } else {
            cleanLinkedHashSet();
        }
    }

    public void sendCommandFromLinkedHashSet() {
        TwitchClient twitchClient = twitchChatBot.getTwitchClient();
        Optional<TwitchCommand> twitchCommand = linkedHashSet.stream().findFirst();
        if (isLiveStream(twitchClient) && twitchCommand.isPresent()) {
            log.info("Список команд в очереди до отправки команды в чат: " + linkedHashSet
                    .stream()
                    .map(TwitchCommand::getName)
                    .collect(Collectors.joining(" ")));

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

    /**
     * Метод определяет логику работы с командами:
     * @return 'true', если хотя бы одна команда запускалась позднее [собственный период вызова +
     *              'CONFIDENCE_TIME_INTERVAL_IN_MINUTES' + 'интервал отправки команд в чат'] минут назад.
     *              CONFIDENCE_TIME_INTERVAL_IN_MINUTES выступает в качестве
     *              доверительного временного интервала для устранения ошибок, связанных со временем.
     *              'Интервал отправки команд в чат' == 'scheduleConfigProperties.periodSendExecutedCommandsByTime';
     *         'false' в противном случаее.
     */
    private boolean commandsWereNotSentWithinTheAllowedTime(List<TwitchCommand> twitchCommands) {
        return twitchCommands.stream()
                .anyMatch(tc -> Duration.between(tc.getLastCompletionTime(), Instant.now()).toMinutes()
                        > tc.getPeriod() +
                        CONFIDENCE_TIME_INTERVAL_IN_MINUTES +
                        Duration.ofMillis(Long.parseLong(scheduleConfigProperties.getPeriodSendExecutedCommandsByTime()))
                                .toMinutes());
    }

    /**
     * Метод заполняет 'LinkedHashSet', когда стрим запущен.
     * Метод 'commandsWereNotSentWithinTheAllowedTime(twitchCommands) = true':
     *      Имеем новый стрим, поскольку есть хотя бы одна команда, которая запускалась давно –
     *      вне диапазона установленного времени [период команды + 'CONFIDENCE_TIME_INTERVAL_IN_MINUTES'
     *      + 'интервал отправки команд в чат'].
     *      Происходит 'обнуление' времени последнего вызова команд.
     * Метод 'commandsWereNotSentWithinTheAllowedTime(twitchCommands) = false':
     *      Имеем дело с запущенным стримом.
     */
    private void fillLinkedHashSet() {
        List<TwitchCommand> twitchCommands = twitchCommandRepository
                .getTwitchCommandsSortedByPeriodWithEnabledTrueAndPeriodNotZero();
        if (commandsWereNotSentWithinTheAllowedTime(twitchCommands)) {
            twitchCommands.forEach(tc -> tc.setLastCompletionTime(Instant.now()));
            twitchCommandRepository.saveAll(twitchCommands);
            linkedHashSet.addAll(twitchCommands);
        }
        twitchCommands.forEach(this::addCommandsToLinkedHashSet);
    }

    /**
     * Метод добавляем команду 'twitchCommand' в очередь при условии, что
     * |(последний вызов команды + период команды) - нынешнее время| <= допустимое время
     * для добавления в 'LinkedHashSet'.
     */
    private void addCommandsToLinkedHashSet(TwitchCommand twitchCommand) {
        if (Math.abs(Duration.between(twitchCommand.getLastCompletionTime(), Instant.now())
                .minus(twitchCommand.getPeriod(), ChronoUnit.MINUTES).toMinutes())
                <= scheduleConfigProperties.getAllowedTimeIntervalInMinutes()) {
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