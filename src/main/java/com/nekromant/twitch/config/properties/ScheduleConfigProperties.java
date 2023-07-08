package com.nekromant.twitch.config.properties;

import lombok.Data;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.context.annotation.Configuration;

@Configuration
@Data
@ConfigurationProperties(prefix = "schedule")
public class ScheduleConfigProperties {
    private String periodEvaluationKindness;
    private String periodValidateConnection;
    private String periodExecutedCommandsByTime;
    private String periodSendExecutedCommandsByTime;
    private String periodAddKindnessForDonat;

    private Long allowedTimeIntervalInMinutes;
}
