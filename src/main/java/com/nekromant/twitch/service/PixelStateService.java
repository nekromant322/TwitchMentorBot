package com.nekromant.twitch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nekromant.twitch.dto.PixelDTO;
import com.nekromant.twitch.model.PixelState;
import com.nekromant.twitch.repository.PixelStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class PixelStateService {
    @Autowired
    private PixelStateRepository pixelStateRepository;

    public PixelState getPixelState() {
        PixelState pixelState = pixelStateRepository.findById(1L).orElse(null);
        return pixelState;
    }

    public String getPixelMatrix() {
        PixelState pixelState = pixelStateRepository.findById(1L).orElse(null);
        return  (pixelState != null) ? pixelState.getMatrix() : null;
    }

    public PixelState savePixel(PixelDTO pixel) {
        PixelState pixelState = getPixelState();
        ObjectMapper jsonMapper = new ObjectMapper();
        ObjectNode json = jsonMapper.createObjectNode();

        if (pixelState == null) {
            pixelState = new PixelState();
            pixelState.setId(1L);
        } else {
            String oldMatrix = pixelState.getMatrix();
            try {
                json = jsonMapper.readTree(oldMatrix).deepCopy();
            } catch (JsonProcessingException e) {
                e.printStackTrace();
            }
        }

        json.put(pixel.getRow() + ":" + pixel.getCol(), pixel.getColor());
        pixelState.setMatrix(json.toString());
        return pixelStateRepository.save(pixelState);
    }
}
