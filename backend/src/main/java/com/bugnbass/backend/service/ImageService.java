package com.bugnbass.backend.service;

import com.bugnbass.backend.model.Image;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.repository.ImageRepository;
import org.springframework.stereotype.Service;

@Service
public class ImageService {

  private final ImageRepository imageRepository;

  public ImageService(ImageRepository imageRepository) {
    this.imageRepository = imageRepository;
  }

  public Image addImageToProduct(Product product, String imageUrl) {
    Image image = new Image();
    image.setProduct(product);
    image.setUrl(imageUrl);
    return imageRepository.save(image);
  }
}
