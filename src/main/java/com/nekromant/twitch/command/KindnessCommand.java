package com.nekromant.twitch.command;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.nekromant.twitch.content.Message;
import com.nekromant.twitch.service.KindnessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.Locale;

import static com.nekromant.twitch.content.MessageContent.*;

@Component
public class KindnessCommand extends BotCommand {
    private final static int LENGTH_COMMAND_KINDNESS = 8;
    @Autowired
    private KindnessService kindnessService;

    @Autowired
    public KindnessCommand() {
        super("доброта", "Рассчитывает показатель доброты");
    }

    @Override
    public void processMessage(ChannelMessageEvent event) {
        String channelName = event.getChannel().getName();
        String senderUsername = event.getMessageEvent().getUser().getName();
        String replyMessageText = NOT_FOUND_USER;
        Message replyMessage = new Message(senderUsername, "");
        Long idUser = Long.valueOf(event.getMessageEvent().getUser().getId());

        String textCommand = event.getMessage().replaceAll("\\s", "");
        if (textCommand.length() > LENGTH_COMMAND_KINDNESS) {
            String userName = textCommand.substring(LENGTH_COMMAND_KINDNESS).toLowerCase(Locale.ROOT);
            String indexKindness = kindnessService.getIndexKindnessByName(userName);
            if (indexKindness != null) {
                replyMessageText = String.format(INDEX_KINDNESS_USER, userName) + indexKindness;
            }
        } else {
            replyMessageText = INDEX_YOUR_KINDNESS + kindnessService.getIndexKindness(idUser, senderUsername);
        }
        replyMessage.setMessageText(replyMessageText);
        event.getMessageEvent().getTwitchChat().sendMessage(channelName, replyMessage.getMessage());
    }
}
