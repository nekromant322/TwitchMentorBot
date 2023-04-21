//package com.nekromant.twitch.command;
//
//import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
//import com.nekromant.twitch.content.Message;
//import com.nekromant.twitch.service.TwitchCommandService;
//import org.springframework.beans.factory.annotation.Autowired;
//import org.springframework.stereotype.Component;
//
//@Component
//public class SimpleResponseCommand {
//    @Autowired
//    private TwitchCommandService twitchCommandService;
//
//    private String commandName;
//    private String description;
//
//
//    @Autowired
//    public SimpleResponseCommand(String commandName, String description) {
//        commandName = twitchCommandService.getCommand(commandName).getName();
//        description = "Отправляет в чат ответ на команду " + twitchCommandService.getCommand(commandName).getName();
//    }
//
//    @Override
//    public void processMessage(ChannelMessageEvent event) {
//        String channelName = event.getChannel().getName();
//        String senderUsername = event.getMessageEvent().getUser().getName();
//        Message replyMessage = new Message(senderUsername, twitchCommandService.getCommand(commandName).getResponse());
//        event.getMessageEvent().getTwitchChat().sendMessage(channelName, replyMessage.getMessage());
//    }
//}
