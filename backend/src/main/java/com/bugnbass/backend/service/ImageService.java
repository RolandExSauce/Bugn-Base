package com.bugnbass.backend.service;

import com.bugnbass.backend.model.Image;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.repository.ImageRepository;
import org.springframework.stereotype.Service;

/**
 * Service for managing images associated with products.
 */
@Service
public class ImageService {

    /**
     * Repository for CRUD operations on Image entities.
     */
    private final ImageRepository imageRepository;

    /**
     * Constructs the ImageService with required ImageRepository.
     *
     * @param imageRepository the ImageRepository instance
     */
    public ImageService(final ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
    }

    /**
     * Creates a new Image entity and associates it with a product.
     *
     * @param product  the Product entity to associate the image with
     * @param imageUrl the URL of the image
     * @return the saved Image entity
     */
    public Image addImageToProduct(Product product, String imageUrl) {
        Image image = new Image();
        image.setProduct(product);
        image.setUrl(imageUrl);
        return imageRepository.save(image);
    }
}
