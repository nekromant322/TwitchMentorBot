package com.nekromant.twitch.command;

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent;
import com.nekromant.twitch.content.Message;
import com.nekromant.twitch.dto.BookedReviewDTO;
import com.nekromant.twitch.feign.MentoringReviewBotFeign;
import com.nekromant.twitch.model.ReviewSchedule;
import com.nekromant.twitch.service.ReviewScheduleService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.util.Comparator;
import java.util.List;
import java.util.stream.Collectors;

import static com.nekromant.twitch.content.MessageContent.NO_REVIEWS;
import static com.nekromant.twitch.content.MessageContent.TODAY_REVIEW_HEADER;

@Component
public class TodayCommand extends BotCommand {
    @Value("${telegram.mentorUsername}")
    private String mentorTelegramUsername;
    @Autowired
    private MentoringReviewBotFeign mentoringReviewBotFeign;
    @Autowired
    private ReviewScheduleService reviewScheduleService;

    @Autowired
    public TodayCommand() {
        super("today", "Отправляет в чат расписание ревью на сегодня");
    }

    @Override
    public void processMessage(ChannelMessageEvent event) {
        String channelName = event.getChannel().getName();
        String senderUsername = event.getMessageEvent().getUser().getName();
        String replyMessageText;
        Message replyMessage = new Message(senderUsername, "");

        ReviewSchedule updatedReviewSchedule = reviewScheduleService.findTodayUpdatedSchedule();

        if (updatedReviewSchedule != null) {
            String reviews = updatedReviewSchedule.getReviews();
            if (reviews.equals("")) {
                replyMessageText = NO_REVIEWS;
            } else {
                replyMessageText = TODAY_REVIEW_HEADER + reviews;
            }
        } else {
            List<BookedReviewDTO> reviewsToday = mentoringReviewBotFeign.getIncomingReview(mentorTelegramUsername);

            if (reviewsToday == null || reviewsToday.isEmpty()) {
                replyMessageText = NO_REVIEWS;
            } else {
                replyMessageText = TODAY_REVIEW_HEADER +
                        reviewsToday.stream()
                                .sorted(Comparator.comparing(BookedReviewDTO::getBookedDateTime))
                                .map(review ->
                                        review.getBookedDateTime().substring(11) + "\n" +
                                                review.getTitle() + "\n")
                                .collect(Collectors.joining(" | "));
            }
        }

        replyMessage.setMessageText(replyMessageText);
        event.getMessageEvent().getTwitchChat().sendMessage(channelName, replyMessage.getMessage());
    }
}
