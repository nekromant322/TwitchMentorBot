package com.nekromant.twitch.command;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.nekromant.twitch.contant.Message;
import com.nekromant.twitch.ModerationTwitchHelix;
import com.nekromant.twitch.service.ReviewScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

import static com.nekromant.twitch.contant.MessageContent.TODAY_UPDATE_CONFIRM;
import static com.nekromant.twitch.contant.MessageContent.TODAY_UPDATE_REJECT;

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
        String broadcasterId = event.getChannel().getId();
        List<String> moderators = getModeratorList(broadcasterId);

        Message replyMessage = new Message(senderUsername, "");

        if (senderUsername.equals(channelOwner) || moderators.contains(senderUsername)) {
            String message = event.getMessage();
            String newReviewSchedule = Arrays.stream(message.split(" ")).skip(1).collect(Collectors.joining(" "));

            reviewScheduleService.saveSchedule(newReviewSchedule);
            replyMessage.setMessageText(TODAY_UPDATE_CONFIRM);
        } else {
            replyMessage.setMessageText(TODAY_UPDATE_REJECT);
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
}
