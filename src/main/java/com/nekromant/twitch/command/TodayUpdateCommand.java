package com.nekromant.twitch.command;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.nekromant.twitch.ModerationTwitchHelix;
import com.nekromant.twitch.content.Message;
import com.nekromant.twitch.exception.PermissionException;
import com.nekromant.twitch.service.ReviewScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.nekromant.twitch.content.MessageContent.NO_PERMISSION;
import static com.nekromant.twitch.content.MessageContent.TODAY_UPDATE_CONFIRM;

@Component
public class TodayUpdateCommand extends BotCommand {

    @Value("${twitch.channelName}")
    private String channelOwner;
    @Autowired
    private ReviewScheduleService reviewScheduleService;
    @Autowired
    private ModerationTwitchHelix moderationTwitchHelix;

    @Autowired
    public TodayUpdateCommand() {
        super("todayupdate", "Обновляет расписание до конца текущего дня");
    }

    @Override
    public void processMessage(ChannelMessageEvent event) {
        String channelName = event.getChannel().getName();
        String senderUsername = event.getMessageEvent().getUser().getName();
        Message replyMessage = new Message(senderUsername, "");

        try {
            checkPermission(senderUsername, event);
            String message = event.getMessage();
            String newReviewSchedule = Arrays.stream(message.split(" ")).skip(1).collect(Collectors.joining(" "));

            reviewScheduleService.saveSchedule(newReviewSchedule);
            replyMessage.setMessageText(TODAY_UPDATE_CONFIRM);
        } catch (Exception e) {
            replyMessage.setMessageText(NO_PERMISSION);
        }

        event.getTwitchChat().sendMessage(channelName, replyMessage.getMessage());
    }

    public List<String> getModeratorList(String broadcasterId) {
        List<String> moderators = moderationTwitchHelix.getHelix()
                .getModerators(moderationTwitchHelix.getModerationToken(), broadcasterId, null, null, 100)
                .execute()
                .getModerators().stream()
                .map(moderator -> moderator.getUserLogin())
                .collect(Collectors.toList());

        return moderators;
    }

    public void checkPermission(String senderUsername, ChannelMessageEvent event) {
        String broadcasterId = event.getChannel().getId();
        List<String> moderators = getModeratorList(broadcasterId);

        if (!senderUsername.equals(channelOwner) && !moderators.contains(senderUsername)) {
            throw new PermissionException();
        }
    }
}
