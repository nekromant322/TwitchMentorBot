package com.nekromant.twitch.command;

import com.nekromant.twitch.dto.BookedReviewDTO;
import com.nekromant.twitch.feign.MentoringReviewBotFeign;
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

@Component
public class TodayCommand extends BotCommand {
    @Value("${telegram.mentorUsername}")
    private String mentorTelegramUsername;
    @Autowired
    private MentoringReviewBotFeign mentoringReviewBotFeign;

    @Autowired
    public TodayCommand() {
        super("today", "Отправляет в чат расписание ревью на сегодня");
    }

    @Override
    public void processMessage(ChannelMessageEvent event) {
        String channelName = event.getChannel().getName();

        List<BookedReviewDTO> reviewsToday = mentoringReviewBotFeign.getIncomingReview(mentorTelegramUsername);
        String messageWithReviewsToday = "Расписание ревью на сегодня\n | \n" +
                reviewsToday.stream()
                .sorted(Comparator.comparing(BookedReviewDTO::getBookedDateTime))
                .map(review ->
//                        "Студент " + review.getStudentUserName() + "\n" +
                                review.getBookedDateTime().substring(11) + "\n" +
                                review.getTitle() + "\n")
                .collect(Collectors.joining(" | "));

        event.getMessageEvent().getTwitchChat().sendMessage(channelName, messageWithReviewsToday);
    }
}
