package com.nekromant.twitch.mapper;

import com.nekromant.twitch.dto.DonatDTO;
import com.nekromant.twitch.model.Donat;
import org.springframework.stereotype.Component;

import java.sql.Timestamp;

@Component
public class DonatMapper {
    public Donat toDonat(DonatDTO dto) {
        Donat donat = new Donat();
        donat.setId((long) dto.getId());
        donat.setName(dto.getUsername().toLowerCase());
        donat.setAmount(Double.parseDouble(dto.getAmount()));
        donat.setTimeDonat(Timestamp.valueOf(dto.getCreated_at()).toInstant());
        return donat;
    }
}
