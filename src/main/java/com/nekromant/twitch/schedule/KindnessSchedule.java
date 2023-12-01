package com.nekromant.twitch.schedule;

import com.nekromant.twitch.service.KindnessService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

//@Component
//TODO remove when openAI token ready
public class KindnessSchedule {
    @Autowired
    private KindnessService kindnessService;

//    @Scheduled(fixedDelayString = "#{@scheduleConfigProperties.periodEvaluationKindness}")
//TODO remove when openAI token ready
    public void evaluationKindnessUserByChatGPT() {
        kindnessService.evaluationKindnessUserWithMostMessages();
    }
}
