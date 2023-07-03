package com.nekromant.twitch.service;

import com.nekromant.twitch.model.Donat;
import com.nekromant.twitch.model.Kindness;
import com.nekromant.twitch.model.TwitchUser;
import com.nekromant.twitch.model.TwitchUserMessage;
import com.nekromant.twitch.repository.KindnessRepository;
import com.nekromant.twitch.repository.TwitchUserMessageRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Slf4j
@Service
public class KindnessService {
    @Autowired
    private ChatGptService chatGptService;
    @Autowired
    private KindnessRepository kindnessRepository;
    @Autowired
    private TwitchUserService twitchUserService;
    @Autowired
    private TwitchUserMessageRepository twitchUserMessageRepository;
    private static final double DEFAULT_INDEX_KINDNESS = 100;
    private static final double DEFAULT_LENGTH_MESSAGES = 200;
    private static final double FAILED_EVALUATION_INDEX_KINDNESS = 75;
    @Value("${donationAlerts.donations.indexConvertingKindness}")
    private double indexConvertingKindness;

    public double getIndexKindness(Long idUser, String nameUser) {
        TwitchUser twitchUser = twitchUserService.getTwitchUserById(idUser);
        if (twitchUser != null) {
            Kindness kindnessByUser = twitchUser.getKindness();
            if (kindnessByUser != null) {
                return kindnessByUser.getIndexKindness();
            }
            save(new Kindness(Instant.now(), DEFAULT_INDEX_KINDNESS, DEFAULT_LENGTH_MESSAGES), twitchUser);
            return DEFAULT_INDEX_KINDNESS;
        }
        save(new Kindness(Instant.now(), DEFAULT_INDEX_KINDNESS, DEFAULT_LENGTH_MESSAGES),
                new TwitchUser(idUser, nameUser));
        return DEFAULT_INDEX_KINDNESS;
    }

    public String getIndexKindnessByName(String nameUser) {
        TwitchUser twitchUser = twitchUserService.getTwitchUserByName(nameUser);
        if (twitchUser != null) {
            return String.valueOf(getIndexKindness(twitchUser.getId(), nameUser));
        }
        return null;
    }

    public void evaluationKindnessUserWithMostMessages() {
        evaluationKindnessUser(twitchUserService.getTwitchUserWithMostMessages());
    }

    private void evaluationKindnessUser(TwitchUser twitchUser) {
        if (twitchUser != null) {
            log.info("Выполняется оценка доброты пользователя " + twitchUser.getName());
            Kindness kindness = createKindness(twitchUser);
            twitchUser.setMessages(new ArrayList<>());
            save(kindness, twitchUser);
            deleteMessagesByTwitchUser(twitchUser);
        }
    }

    private Kindness createKindness(TwitchUser twitchUser) {
        List<String> twitchUserMessages = twitchUser.getMessages().stream()
                .map(TwitchUserMessage::getMessage)
                .collect(Collectors.toList());
        double newLengthMessages = String.join(", ", twitchUserMessages).length();
        double newIndexKindness = getIndexKindnessByChatGPT(twitchUserMessages);

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

    private double getIndexKindnessByChatGPT(List<String> twitchUserMessages) {
        try {
            return evaluationTextByChatGPT(String.join(", ", twitchUserMessages));
        } catch (Exception e) {
            log.error(e.getMessage());
            int sizeHalfMessages = twitchUserMessages.size() / 2;
            String firstPartMessages = String.join(", ", twitchUserMessages.subList(0, sizeHalfMessages));
            String secondPartMessages =
                    String.join(", ", twitchUserMessages.subList(sizeHalfMessages, twitchUserMessages.size()));
            return (evaluationTextByChatGPT(firstPartMessages)
                    + evaluationTextByChatGPT(secondPartMessages)) / 2;
        }
    }

    private double evaluationTextByChatGPT(String text) {
        String responseChatGPT = chatGptService.getIndexKindness(text).replaceAll("\\D", "");
        log.info("Сообщения для оценки чатом chatGPT: " + text);
        log.info("Ответ chatGPT: " + responseChatGPT);
        if (!responseChatGPT.isEmpty()) {
            double indexKindness = Double.parseDouble(responseChatGPT);
            if (indexKindness > 0 && indexKindness <= 100) {
                return indexKindness;
            }
        }
        return FAILED_EVALUATION_INDEX_KINDNESS;
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

    private void deleteMessagesByTwitchUser(TwitchUser twitchUser) {
        twitchUserMessageRepository.deleteAllByTwitchUser(twitchUser);
    }

    public void addKindnessForDonat(Donat donat) {
        String userName = donat.getName();
        TwitchUser twitchUser = twitchUserService.getTwitchUserByName(userName);
        if (twitchUser != null) {
            Kindness kindnessByUser = twitchUser.getKindness();
            if (kindnessByUser != null) {
                kindnessByUser.setIndexKindness(calculationIndexKindnessForDonat(kindnessByUser, donat));
                save(kindnessByUser, twitchUser);
                log.info("Добавление доброты за счёт доната пользователю: " + userName);
                return;
            }
        }
        log.info("При добавлении доброты за счёт доната, пользователь с данным именем не найден: " + userName);
    }

    private double calculationIndexKindnessForDonat(Kindness kindness, Donat donat) {
        double additionalKindness = Math.ceil(donat.getAmount() / indexConvertingKindness);
        double calculatedIndexKindness = kindness.getIndexKindness() + additionalKindness;

        if (calculatedIndexKindness <= DEFAULT_INDEX_KINDNESS) {
            return calculatedIndexKindness;
        }
        return DEFAULT_INDEX_KINDNESS;
    }
}
