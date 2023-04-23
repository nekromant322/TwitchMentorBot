package com.nekromant.twitch.service;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.nekromant.twitch.command.BotCommand;
import com.nekromant.twitch.content.Message;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.HashMap;
import java.util.List;

@Service
public class ResponseService {
    private TwitchCommandService twitchCommandService;
    private HashMap<String, BotCommand> botCommands;

    @Autowired
    public ResponseService(TwitchCommandService twitchCommandService,
                           List<BotCommand> allCommands) {
        this.twitchCommandService = twitchCommandService;
        botCommands = new HashMap<>();
        allCommands.forEach(command -> botCommands.put(command.getCommandIdentifier(), command));
    }

    public void response(ChannelMessageEvent event, String command) {
        if (validateDBCommand(command)) {
            processSimpleResponse(event, command);
        }

        if (validateClassCommand(command)) {
            botCommands.get(command).processMessage(event);
        }
    }

    private boolean validateDBCommand(String command) {
        return twitchCommandService.getCommand(command) != null && twitchCommandService.getCommand(command).isEnabled();
    }

    private void processSimpleResponse(ChannelMessageEvent event, String command) {
        String channelName = event.getChannel().getName();
        String senderUsername = event.getMessageEvent().getUser().getName();
        Message replyMessage = new Message(senderUsername, twitchCommandService.getCommand(command).getResponse());
        event.getMessageEvent().getTwitchChat().sendMessage(channelName, replyMessage.getMessage());
    }

    private boolean validateClassCommand(String command) {
        return botCommands.containsKey(command);
    }
}
