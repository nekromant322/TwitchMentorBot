package com.nekromant.twitch.chatGPT;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.stereotype.Component;

@Component
public class ChatGptService {
    protected final String PREFIX = "Оцени доброжелательность текста, дай единственную оценку от 0 до 100, " +
            "отвечай только цифрами:";
    @Autowired
    private ChatGptFeign chatGptFeign;
    @Autowired
    private ChatGPTJson chatGPTJson;
    @Value("${chatGPT.API_KEY}")
    private String API_KEY;

    public String getIndexKindness(String message) {
        try {
            String jsonResponse = chatGptFeign.getResponse(API_KEY, chatGPTJson.createJsonRequest(PREFIX + message).toString());
            return chatGPTJson.getStringFromJsonResponse(jsonResponse);
        } catch (JSONException e) {
            throw new RuntimeException(e);
        }
    }
}
