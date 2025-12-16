package com.bugnbass.backend.dto;
import java.util.UUID;

public record ImageDTO(
        UUID imageId,
        String url
) {
    public static ImageDTO fromEntity(com.bugnbass.backend.model.Image image) {
        return new ImageDTO(
                image.getImageId(),
                image.getUrl()
        );
    }
}