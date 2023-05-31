package com.nekromant.twitch.command;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.nekromant.twitch.content.Message;
import com.nekromant.twitch.model.TwitchCommand;
import com.nekromant.twitch.repository.TwitchCommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.stream.Collectors;

import static com.nekromant.twitch.content.MessageContent.AVAILABLE_COMMANDS;
import static com.nekromant.twitch.content.MessageContent.NO_AVAILABLE_COMMANDS;

@Component
public class HelpCommand extends BotCommand {
    @Autowired
    private TwitchCommandRepository twitchCommandRepository;

    @Autowired
    public HelpCommand() {
        super("help", "Отправляет в чат доступные команды");
    }

    @Override
    public void processMessage(ChannelMessageEvent event) {
        String channelName = event.getChannel().getName();
        String senderUsername = event.getMessageEvent().getUser().getName();
        String replyMessageText = NO_AVAILABLE_COMMANDS;
        Message replyMessage = new Message(senderUsername, "");

        List<TwitchCommand> listCommands = (List<TwitchCommand>) twitchCommandRepository.findAll();
        if (!listCommands.isEmpty()) {
            String listAvailableCommands = listCommands.stream()
                    .filter(TwitchCommand::isEnabled)
                    .map(TwitchCommand::getName)
                    .map((command) -> "!" + command)
                    .collect(Collectors.joining(" "));
            if (!listAvailableCommands.isEmpty()) {
                replyMessageText = AVAILABLE_COMMANDS + listAvailableCommands;
            }
        }
        replyMessage.setMessageText(replyMessageText);
        event.getMessageEvent().getTwitchChat().sendMessage(channelName, replyMessage.getMessage());
    }
}

