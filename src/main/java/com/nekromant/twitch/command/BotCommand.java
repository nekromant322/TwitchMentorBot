package com.nekromant.twitch.command;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;


public abstract class BotCommand {
    private final String commandIdentifier;
    private final String description;

    public BotCommand(String commandIdentifier, String description) {
        this.commandIdentifier = commandIdentifier;
        this.description = description;
    }

    public abstract void processMessage(ChannelMessageEvent event);

    public String getCommandIdentifier() {
        return this.commandIdentifier;
    }

    public String getDescription() {
        return this.description;
    }
}
