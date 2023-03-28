package com.nekromant.twitch.command;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.nekromant.twitch.ModerationTwitchHelix;
import com.nekromant.twitch.service.ReviewScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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
        String senderUsername = event.getMessageEvent().getUser().getName().toLowerCase();
        String broadcasterId = event.getChannel().getId();
        List<String> moderators = getModeratorList(broadcasterId);

        if (senderUsername.equals(channelOwner) || moderators.contains(senderUsername)) {
            String message = event.getMessage();
            String newReviewSchedule = Arrays.stream(message.split(" ")).skip(1).collect(Collectors.joining(" "));

            reviewScheduleService.saveSchedule(newReviewSchedule);

            event.getTwitchChat().sendMessage(channelName, "Расписание обновлено");
        } else {
            event.getTwitchChat().sendMessage(channelName, "Ты не можешь менять расписание");
        }
    }

    public List<String> getModeratorList(String broadcasterId) {
        List<String> moderators = moderationTwitchHelix.getHelix()
                .getModerators(moderationTwitchHelix.getModerationToken(), broadcasterId, null, null, 100)
                .execute()
                .getModerators().stream()
                .map(moderator -> moderator.getUserName())
                .collect(Collectors.toList());

        return moderators;
    }
}
