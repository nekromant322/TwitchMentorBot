package com.nekromant.twitch.command;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.nekromant.twitch.content.Message;
import com.nekromant.twitch.service.DailyBonusService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;


@Component
public class DailyBonusCheckCommand extends BotCommand {

    @Autowired
    private DailyBonusService dailyBonusService;

    public DailyBonusCheckCommand() {
        super("выпито", "Проверить количество бонусов");
    }

    @Override
    public void processMessage(ChannelMessageEvent event) {
        String channelName = event.getChannel().getName();
        String senderUsername = event.getMessageEvent().getUser().getName();
        int bonus = dailyBonusService.getBonusCount(event.getMessageEvent().getUser());
        Message replyMessage = new Message(senderUsername, "Выпито " + bonus + " смузи");
        event.getMessageEvent().getTwitchChat().sendMessage(channelName, replyMessage.getMessage());
    }
}
