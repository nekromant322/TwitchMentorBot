package com.nekromant.twitch.command;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.nekromant.twitch.content.Message;
import com.nekromant.twitch.service.DailyBonusService;
import com.nekromant.twitch.service.TwitchLiveService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Component;

//@Component
//public class DailyBonusCommand extends BotCommand {
//
//    @Autowired
//    private DailyBonusService dailyBonusService;
//
//    @Lazy
//    @Autowired
//    private TwitchLiveService twitchLiveService;
//
//    private final String BONUS_SUCCESS = " выпил смузи";
//    private final String BONUS_FAILED = " уже пил смузи сегодня";
//
//    @Autowired
//    public DailyBonusCommand() {
//        super("смузи", "Получение ежедневного бонуса");
//    }
//
//    @Override
//    public void processMessage(ChannelMessageEvent event) {
//        if (!twitchLiveService.isLiveStream()) {
//            return;
//        }
//
//        String channelName = event.getChannel().getName();
//        String senderUsername = event.getMessageEvent().getUser().getName();
//        Message replyMessage = new Message(senderUsername, "");
//        if (dailyBonusService.takeBonus(event.getUser())) {
//            replyMessage.setMessageText(BONUS_SUCCESS);
//        } else {
//            replyMessage.setMessageText(BONUS_FAILED);
//        }
//        event.getMessageEvent().getTwitchChat().sendMessage(channelName, replyMessage.getMessage());
//    }
//}
