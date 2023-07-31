package com.nekromant.twitch.command;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.nekromant.twitch.content.Message;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public class SongListCommand extends BotCommand {
    @Value("https://overridetech.ru/twitch-bot/songList/panel")
    private String songListUrl;

    public SongListCommand() {
        super("sl", "Отправляет ссылку на song list");
    }

    @Override
    public void processMessage(ChannelMessageEvent event) {
        String channelName = event.getChannel().getName();
        String senderUsername = event.getMessageEvent().getUser().getName();

        Message replyMessage = new Message(senderUsername, songListUrl);
        event.getMessageEvent().getTwitchChat().sendMessage(channelName, replyMessage.getMessage());
    }
}