package com.bugnbass.backend.dto;

import java.util.UUID;

/**
 * DTO representing an image associated with a product.
 *
 * @param imageId the unique identifier of the image
 * @param url     the URL or path of the image
 */
public record ImageDto(
    UUID imageId,
    String url
) {
  /**
   * Converts an {@link com.bugnbass.backend.model.Image} entity to an {@link ImageDto}.
   *
   * @param image the image entity
   * @return the corresponding ImageDTO
   */
  public static ImageDto fromEntity(com.bugnbass.backend.model.Image image) {
    return new ImageDto(
        image.getImageId(),
        image.getUrl()
    );
  }
}
