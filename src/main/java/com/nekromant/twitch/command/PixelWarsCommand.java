package com.nekromant.twitch.command;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.nekromant.twitch.content.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import static com.nekromant.twitch.content.MessageContent.PIXEL_WARS;

@Component
public class PixelWarsCommand extends BotCommand {
    @Value("${twitch.pixelWarsLink}")
    private String LINK;

    @Autowired
    public PixelWarsCommand() {
        super("pixelwars", "Отправляет в чат ссылку на PixelWars");
    }

    @Override
    public void processMessage(ChannelMessageEvent event) {
        String channelName = event.getChannel().getName();
        String senderUsername = event.getMessageEvent().getUser().getName();
        Message replyMessage = new Message(senderUsername, PIXEL_WARS + LINK);
        event.getMessageEvent().getTwitchChat().sendMessage(channelName, replyMessage.getMessage());
    }
}
