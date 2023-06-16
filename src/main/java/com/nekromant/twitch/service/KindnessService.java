package com.nekromant.twitch.service;

import com.nekromant.twitch.chatGPT.ChatGptService;
import com.nekromant.twitch.model.Kindness;
import com.nekromant.twitch.model.TwitchUser;
import com.nekromant.twitch.model.TwitchUserMessage;
import com.nekromant.twitch.repository.KindnessRepository;
import com.nekromant.twitch.repository.TwitchUserMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

import java.time.Instant;
import java.util.ArrayList;
import java.util.stream.Collectors;

@Slf4j
@Component
public class KindnessService {
    @Autowired
    ChatGptService chatGptService;
    @Autowired
    KindnessRepository kindnessRepository;
    @Autowired
    TwitchUserService twitchUserService;
    @Autowired
    TwitchUserMessageRepository twitchUserMessageRepository;
    private final double DEFAULT_INDEX_KINDNESS = 100;
    private final double DEFAULT_LENGTH_MESSAGES = 200;

    public double getIndexKindness(Long idUser) {
        TwitchUser twitchUser = twitchUserService.getTwitchUserById(idUser);
        Kindness kindnessByUser = twitchUser.getKindness();
        if (kindnessByUser != null) {
            return kindnessByUser.getIndexKindness();
        }
        save(new Kindness(Instant.now(), DEFAULT_INDEX_KINDNESS, DEFAULT_LENGTH_MESSAGES), twitchUser);
        return DEFAULT_INDEX_KINDNESS;
    }

    @Scheduled(fixedDelay = 90000)
    public void evaluationKindnessAllUsers() {
        evaluationKindnessUser(twitchUserService.getTwitchUserWithMostMessages());
    }

    public void evaluationKindnessUser(TwitchUser twitchUser) {
        if (twitchUser != null) {
            String twitchUserMessages = twitchUser.getMessages().stream()
                    .map(TwitchUserMessage::getMessage)
                    .collect(Collectors.joining(", "));

            Kindness kindness = createKindness(twitchUserMessages, twitchUser);
            twitchUser.setMessages(new ArrayList<>());
            save(kindness, twitchUser);
            deleteMessagesByTwitchUser(twitchUser);
            log.info("Выполняется оценка доброты пользователя " + twitchUser.getName());
        } else {
            log.info("Сообщения для оценки доброты закончились");
        }
    }

    private Kindness createKindness(String twitchUserMessages, TwitchUser twitchUser) {
        double newLengthMessages = twitchUserMessages.length();
        double newIndexKindness = DEFAULT_INDEX_KINDNESS;
        String responseChatGPT = chatGptService.getIndexKindness(twitchUserMessages).replaceAll("\\D", "");
        if (!responseChatGPT.isEmpty()) {
            newIndexKindness = Double.parseDouble(responseChatGPT);
        }
        Kindness kindnessByUser = twitchUser.getKindness();
        if (kindnessByUser != null) {
            Kindness newKindness = calculationIndexKindness(kindnessByUser.getLengthMessages(), newLengthMessages,
                    kindnessByUser.getIndexKindness(), newIndexKindness);
            deleteKindness(kindnessByUser, twitchUser);
            return newKindness;
        } else {
            return calculationIndexKindness(DEFAULT_LENGTH_MESSAGES, newLengthMessages,
                    DEFAULT_INDEX_KINDNESS, newIndexKindness);
        }
    }

    private Kindness calculationIndexKindness
            (double lengthMessages, double newLengthMessages, double indexKindness, double newIndexKindness) {

        double allLengthMessages = newLengthMessages + lengthMessages;
        double calculatedIndexKindness = Math.ceil(lengthMessages / allLengthMessages * indexKindness +
                newLengthMessages / allLengthMessages * newIndexKindness);

        return new Kindness(Instant.now(), calculatedIndexKindness, allLengthMessages);
    }

    private void save(Kindness kindness, TwitchUser twitchUser) {
        twitchUser.setKindness(kindness);
        kindness.setTwitchUser(twitchUser);
        twitchUserService.save(twitchUser);
    }

    private void deleteKindness(Kindness kindness, TwitchUser twitchUser) {
        twitchUser.setKindness(null);
        kindnessRepository.delete(kindness);
    }

    public void deleteMessagesByTwitchUser(TwitchUser twitchUser) {
        twitchUserMessageRepository.deleteAllByTwitchUser(twitchUser);
    }
}
