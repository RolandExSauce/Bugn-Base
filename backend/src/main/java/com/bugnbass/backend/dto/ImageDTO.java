package com.bugnbass.backend.dto;

/**
 * DTO representing an image associated with a product.
 *
 * @param imageId the unique identifier of the image
 * @param url the URL of the image
 * @param productId the ID of the product the image belongs to
 */
public record ImageDTO(String imageId, String url, String productId) {}
