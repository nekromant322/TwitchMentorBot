package com.nekromant.twitch.command;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

@Component
public class HelloCommand extends BotCommand {

    @Autowired
    public HelloCommand() {
        super("hello", "Приветствует пользователя в чате");
    }

    @Override
    public void processMessage(ChannelMessageEvent event) {
        String channelName = event.getChannel().getName();
        event.getMessageEvent().getTwitchChat().sendMessage(channelName, "Hello " + event.getUser().getName());
    }
}
