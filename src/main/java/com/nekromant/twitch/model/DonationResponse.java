package com.nekromant.twitch.model;

import com.nekromant.twitch.dto.DonatDTO;
import lombok.Getter;

import java.util.List;

@Getter
public class DonationResponse {
    private List<DonatDTO> data;
}
