package com.nekromant.twitch.command.settings;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
public class CustomCommandNamesHolder {

    @Autowired
    private HelpBotCommandBeanPostProcessor helpBotCommandBeanPostProcessor;

    public String stringOfCustomCommand() {
        return helpBotCommandBeanPostProcessor.getCustomCommand();
    }

    public List<String> listOfCustomCommand() {
        return helpBotCommandBeanPostProcessor.getListOfCustomCommand();
    }
}
