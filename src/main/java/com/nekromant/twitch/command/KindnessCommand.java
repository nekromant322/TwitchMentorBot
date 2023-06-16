package com.nekromant.twitch.command;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.nekromant.twitch.content.Message;
import com.nekromant.twitch.service.KindnessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import static com.nekromant.twitch.content.MessageContent.INDEX_YOUR_KINDNESS;

@Component
public class KindnessCommand extends BotCommand {
    @Autowired
    private KindnessService kindnessService;

    @Autowired
    public KindnessCommand() {
        super("kindness", "Рассчитывает показатель доброты");
    }

    @Override
    public void processMessage(ChannelMessageEvent event) {
        String channelName = event.getChannel().getName();
        String senderUsername = event.getMessageEvent().getUser().getName();
        Long idUser = Long.valueOf(event.getMessageEvent().getUser().getId());
        Message replyMessage = new Message(senderUsername,
                INDEX_YOUR_KINDNESS + kindnessService.getIndexKindness(idUser));

        event.getMessageEvent().getTwitchChat().sendMessage(channelName, replyMessage.getMessage());
    }
}
