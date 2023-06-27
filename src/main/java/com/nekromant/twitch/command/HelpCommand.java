package com.nekromant.twitch.command;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.nekromant.twitch.command.settings.CustomCommandNamesHolder;
import com.nekromant.twitch.content.Message;
import com.nekromant.twitch.model.TwitchCommand;
import com.nekromant.twitch.repository.TwitchCommandRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.StringJoiner;
import java.util.stream.Collectors;

import static com.nekromant.twitch.content.MessageContent.AVAILABLE_COMMANDS;
import static com.nekromant.twitch.content.MessageContent.NO_AVAILABLE_COMMANDS;

@Component
public class HelpCommand extends BotCommand {
    @Autowired
    private TwitchCommandRepository twitchCommandRepository;
    @Autowired
    private CustomCommandNamesHolder customCommandNamesHolder;

    @Autowired
    public HelpCommand() {
        super("help", "Отправляет в чат доступные команды");
    }

    @Override
    public void processMessage(ChannelMessageEvent event) {
        String channelName = event.getChannel().getName();
        String senderUsername = event.getMessageEvent().getUser().getName();
        Message replyMessage = new Message(senderUsername, "");
        replyMessage.setMessageText(getReplyMessageText());
        event.getMessageEvent().getTwitchChat().sendMessage(channelName, replyMessage.getMessage());
    }

    private String getReplyMessageText() {
        String customCommand = getCustomCommands();
        String twitchCommand = getTwitchCommands();
        if (!customCommand.equals(twitchCommand)) {
            return getAvailableCommands(customCommand, twitchCommand);
        } else {
            return NO_AVAILABLE_COMMANDS;
        }
    }

    private String getAvailableCommands(String customCommand, String twitchCommand) {
        StringJoiner commandJoiner = new StringJoiner(" ");
        commandJoiner.add(AVAILABLE_COMMANDS);
        if (!customCommand.equals(AVAILABLE_COMMANDS)) {
            commandJoiner.add(customCommand);
        }
        if (!twitchCommand.equals(AVAILABLE_COMMANDS)) {
            commandJoiner.add(twitchCommand);
        }
        return commandJoiner.toString();
    }

    private String getCustomCommands() {
        if (!customCommandNamesHolder.listOfCustomCommand().isEmpty()) {
            return customCommandNamesHolder.stringOfCustomCommand();
        } else {
            return NO_AVAILABLE_COMMANDS;
        }
    }

    private String getTwitchCommands() {
        List<TwitchCommand> listCommands = (List<TwitchCommand>) twitchCommandRepository.findAll();
        if (!listCommands.isEmpty()) {
            String listAvailableCommands = listCommands.stream()
                    .filter(TwitchCommand::isEnabled)
                    .map(TwitchCommand::getName)
                    .map((command) -> "!" + command)
                    .collect(Collectors.joining(" "));
            return !listAvailableCommands.isEmpty() ? listAvailableCommands : NO_AVAILABLE_COMMANDS;
        } else {
            return NO_AVAILABLE_COMMANDS;
        }
    }
}