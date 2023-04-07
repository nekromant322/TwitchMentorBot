package com.nekromant.twitch.service;

import com.github.twitch4j.chat.TwitchChat;
import com.github.twitch4j.pubsub.domain.ChannelPointsRedemption;
import com.github.twitch4j.pubsub.domain.ChannelPointsReward;
import com.github.twitch4j.pubsub.events.RewardRedeemedEvent;
import com.nekromant.twitch.contant.Message;
import com.nekromant.twitch.model.RedeemedPixels;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import static com.nekromant.twitch.contant.MessageContent.PIXEL_WARS;

@Service
public class ChannelPointsRedemptionService {
    private final static String PIXEL_REWARD_TITLE = "pixel(не работает)";
    @Value("${pixelWars.width}")
    private int width;
    @Value("${pixelWars.height}")
    private int height;
    @Value("${twitch.channelName}")
    private String channelName;

    private static final String LINK = "v1570779.hosted-by-vdsina.ru:8080/pixel";
    @Autowired
    private RedeemedPixelsService redeemedPixelsService;

    public void onEvent(RewardRedeemedEvent event, TwitchChat chat) {
        ChannelPointsRedemption redemption = event.getRedemption();
        ChannelPointsReward reward = redemption.getReward();

        if (reward.getTitle().equals(PIXEL_REWARD_TITLE)) {
            String username = redemption.getUser().getLogin();
            savePixel(username);
            sendLinkToSelectionPixelsPage(username, chat);
        }
    }

    public RedeemedPixels savePixel(String username) {
        return redeemedPixelsService.save(username);
    }

    public void sendLinkToSelectionPixelsPage(String senderUsername, TwitchChat chat) {
        Message replyMessage = new Message(senderUsername, PIXEL_WARS + LINK);
        chat.sendMessage(channelName, replyMessage.getMessage());
    }
}
