package com.nekromant.twitch.dto;

import lombok.*;

@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class PointAucObjectDTO {
    private Long fastId;

    private Double id;

    private String extra;

    private Integer amount;

    private String name;
}
