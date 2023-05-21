package com.nekromant.twitch.aop;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.aspectj.lang.annotation.Aspect;
import org.aspectj.lang.annotation.Before;
import org.aspectj.lang.annotation.Pointcut;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Component;

@Component
@Aspect
public class LoggingCommands {

    private static final Logger LOGGER = LoggerFactory.getLogger(LoggingCommands.class);

    @Pointcut("execution(public void *.processMessage(..)) && args(event))")
    public void botCommandProcessMessage(ChannelMessageEvent event) {
    }

    @Before("botCommandProcessMessage(event)")
    public void beforeBotCommandProcessMessage(ChannelMessageEvent event) {
        LOGGER.info(event.getUser().getName() + " вызвал команду " + event.getMessage());
    }
}
