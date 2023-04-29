package com.nekromant.twitch.service;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.node.ObjectNode;
import com.nekromant.twitch.dto.PixelDTO;
import com.nekromant.twitch.dto.TableSizeDTO;
import com.nekromant.twitch.model.PixelState;
import com.nekromant.twitch.repository.PixelStateRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

@Service
public class PixelStateService {
    @Value("${pixelWars.width}")
    private Integer tableWidth;
    @Value("${pixelWars.height}")
    private Integer tableHeight;
    @Autowired
    private PixelStateRepository pixelStateRepository;
    @Autowired
    private RedeemedPixelsService redeemedPixelsService;

    public TableSizeDTO getTableSize() {
        return new TableSizeDTO(tableWidth, tableHeight);
    }

    public PixelState getPixelState() {
        return pixelStateRepository.findById(1L).orElse(null);
    }

    public String getPixelMatrix() {
        PixelState pixelState = pixelStateRepository.findById(1L).orElse(null);
        return  (pixelState != null) ? pixelState.getMatrix() : null;
    }

    public PixelState savePixel(PixelDTO pixel, String token) {
        int redeemedPixelCounts = redeemedPixelsService.getRedeemedPixelsCountByToken(token);

        if (redeemedPixelCounts > 0 && validatePixelPosition(pixel.getRow(), pixel.getCol())) {
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
        return null;
    }

    public boolean validatePixelPosition(int row, int col) {
        return row < tableHeight && col < tableWidth;
    }
}
