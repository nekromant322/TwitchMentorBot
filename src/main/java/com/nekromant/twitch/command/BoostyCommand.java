package com.nekromant.twitch.command;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.nekromant.twitch.content.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.nekromant.twitch.content.MessageContent.BOOSTY;

@Component
public class BoostyCommand extends BotCommand {
    @Value("${twitch.boostyLink}")
    private String LINK;

    @Autowired
    public BoostyCommand() {
        super("boosty", "Отправляет в чат ссылку на boosty");
    }

    @Override
    public void processMessage(ChannelMessageEvent event) {
        String channelName = event.getChannel().getName();
        String senderUsername = event.getMessageEvent().getUser().getName();
        Message replyMessage = new Message(senderUsername, BOOSTY + LINK);
        event.getMessageEvent().getTwitchChat().sendMessage(channelName, replyMessage.getMessage());
    }
}
