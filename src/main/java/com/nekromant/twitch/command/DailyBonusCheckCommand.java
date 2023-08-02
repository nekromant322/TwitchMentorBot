package com.nekromant.twitch.command;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.github.twitch4j.common.events.domain.EventUser;
import com.nekromant.twitch.content.Message;
import com.nekromant.twitch.repository.TwitchUserRepository;
import com.nekromant.twitch.service.DailyBonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.stream.Collectors;

import static com.nekromant.twitch.content.MessageContent.USER_DAILY_BONUS_COUNT;
import static com.nekromant.twitch.content.MessageContent.TARGET_USER_DAILY_BONUS_COUNT;

@Component
public class DailyBonusCheckCommand extends BotCommand {

    @Autowired
    private DailyBonusService dailyBonusService;

    @Autowired
    private TwitchUserRepository twitchUserRepository;

    public DailyBonusCheckCommand() {
        super("выпито", "Проверить количество бонусов");
    }

    @Override
    public void processMessage(ChannelMessageEvent event) {
        String channelName = event.getChannel().getName();
        event.getMessageEvent().getTwitchChat().sendMessage(channelName, getReplyMessage(event).getMessage());
    }

    public Message getReplyMessage(ChannelMessageEvent event) {
        String message = event.getMessage();
        String senderUsername = event.getMessageEvent().getUser().getName();
        String targetUsername = Arrays.stream(message.split(" "))
                .skip(1)
                .collect(Collectors.joining(" "));

        if (!targetUsername.equals("")) {
            EventUser targetEventUser = new EventUser(String.valueOf(twitchUserRepository
                    .findTwitchUserByName(targetUsername.toLowerCase()).getId()), targetUsername);
            return new Message(senderUsername, String.format(TARGET_USER_DAILY_BONUS_COUNT, targetUsername,
                    getBonus(targetEventUser), getPosition(targetEventUser)));
        } else {
            EventUser senderEventUser = event.getMessageEvent().getUser();
            return new Message(senderUsername, String.format(USER_DAILY_BONUS_COUNT, getBonus(senderEventUser),
                    getPosition(senderEventUser)));
        }
    }

    public int getBonus(EventUser eventUser) {
        return dailyBonusService.getBonusCount(eventUser);
    }

    public Long getPosition(EventUser eventUser) {
        return dailyBonusService.getPosition(eventUser);
    }
}