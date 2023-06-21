package com.nekromant.twitch.service;

import com.nekromant.twitch.feign.ChatGptFeign;
import lombok.extern.slf4j.Slf4j;
import org.json.JSONArray;
import org.json.JSONException;
import org.json.JSONObject;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Slf4j
@Service
public class ChatGptService {
    private final static String PREFIX = "Оцени доброжелательность текста, дай единственную оценку от 0 до 100, " +
            "отвечай только цифрами:";
    @Autowired
    private ChatGptFeign chatGptFeign;
    @Value("${chatGPT.API_KEY_Chat_GPT}")
    private String API_KEY;

    public String getIndexKindness(String message) {
        try {
            String jsonResponse = chatGptFeign.getResponse(API_KEY, createJsonRequest(PREFIX + message).toString());
            return getStringFromJsonResponse(jsonResponse);
        } catch (JSONException e) {
            log.error(String.valueOf(e));
            throw new RuntimeException(e);
        }
    }

    private JSONObject createJsonRequest(String message) throws JSONException {
        JSONArray messagesArray = new JSONArray();

        JSONObject userMessageObject = new JSONObject();
        userMessageObject.put("role", "user");
        userMessageObject.put("content", message);
        messagesArray.put(userMessageObject);

        JSONObject requestObject = new JSONObject();
        requestObject.put("model", "gpt-3.5-turbo");
        requestObject.put("messages", messagesArray);
        requestObject.put("temperature", 0.5);
        requestObject.put("max_tokens", 60);
        return requestObject;
    }

    private String getStringFromJsonResponse(String output) throws JSONException {
        String result = (String) new JSONObject(output)
                .getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .get("content");
        return result.replace(".", "");
    }
}
