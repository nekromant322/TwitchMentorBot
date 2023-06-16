package com.nekromant.twitch.chatGPT;

import org.springframework.boot.configurationprocessor.json.JSONArray;
import org.springframework.boot.configurationprocessor.json.JSONException;
import org.springframework.boot.configurationprocessor.json.JSONObject;
import org.springframework.stereotype.Component;

@Component
public class ChatGPTJson {
    public JSONObject createJsonRequest(String message) throws JSONException {
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

    public String getStringFromJsonResponse(String output) throws JSONException {
        String result = (String) new JSONObject(output)
                .getJSONArray("choices")
                .getJSONObject(0)
                .getJSONObject("message")
                .get("content");
        return result.replace(".", "");
    }
}
