package com.nekromant.twitch;

import com.github.twitch4j.TwitchClient;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.nekromant.twitch.command.BotCommand;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.HashMap;
import java.util.List;

@Component
public class TwitchChatBot {
    private final static String PREFIX = "!";

    private HashMap<String, BotCommand> botCommands;
    private TwitchClient twitchClient;

    @Autowired
    public TwitchChatBot(TwitchClient twitchClient, List<BotCommand> allCommands) {
        this.twitchClient = twitchClient;
        botCommands = new HashMap<>();
        allCommands.forEach(command -> botCommands.put(command.getCommandIdentifier(), command));
        twitchClient.getEventManager().onEvent(ChannelMessageEvent.class, event -> onChatMessageEvent(event));
    }

    public void onChatMessageEvent(ChannelMessageEvent event) {
        String message = event.getMessage();

        if (isCommand(message)) {
            String command = message.split(" ")[0].substring(1);

            if (botCommands.containsKey(command)) {
                botCommands.get(command).processMessage(event);
            }
        }
    }

    public boolean isCommand(String message) {
        return message.startsWith(PREFIX);
    }
}
