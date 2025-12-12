package com.bugnbass.backend.service;

import com.bugnbass.backend.exceptions.ImageNotFoundException;
import com.bugnbass.backend.exceptions.ImageUploadUpdateException;
import com.bugnbass.backend.exceptions.ProductNotFoundException;
import com.bugnbass.backend.model.Image;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.repository.ImageRepository;
import com.bugnbass.backend.repository.ProductRepository;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service for managing images related to products. Provides methods for retrieving, adding, and
 * deleting images.
 */
@Service
public class ImageService {

  private final ProductRepository productRepository;
  private final ImageRepository imageRepository;

  public ImageService(ProductRepository productRepository, ImageRepository imageRepository) {
    this.productRepository = productRepository;
    this.imageRepository = imageRepository;
  }

  /**
   * Retrieves an image by its ID.
   *
   * @param imageId the ID of the image
   * @return the Image object
   * @throws ImageNotFoundException if the image does not exist
   */
  public Image getImage(String imageId) {
    return imageRepository
        .findById(UUID.fromString(imageId))
        .orElseThrow(ImageNotFoundException::new);
  }

  /**
   * Retrieves all images associated with a specific product.
   *
   * @param productId the ID of the product
   * @return a list of images for the product
   * @throws ImageNotFoundException if no images are found for the product
   */
  public List<Image> getProductImages(String productId) {
    Long id = Long.valueOf(productId);
    List<Image> images = imageRepository.findAllByProduct_Id(id);

    if (images.isEmpty()) {
      throw new ImageNotFoundException("No images found for product " + productId);
    }
    return images;
  }

  /**
   * Adds an image to a product and saves it both in the database and the filesystem.
   *
   * @param file the image file to upload
   * @param productId the ID of the product
   * @throws ProductNotFoundException if the product does not exist
   * @throws RuntimeException if the file cannot be saved
   */
  @Transactional
  public void addImageToProduct(MultipartFile file, String productId) {
    Product product =
        productRepository
            .findById(Long.valueOf(productId))
            .orElseThrow(ProductNotFoundException::new);

    String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
    String category = product.getCategory().name().toLowerCase();

    Path physicalPath = Paths.get("src/main/resources/static/product_images", category, fileName);
    Path dbPath = Paths.get("/product_images", category, fileName);

    Image image = new Image();
    image.setUrl(dbPath.toString());
    image.setProduct(product);

    imageRepository.save(image);

    try {
      Files.createDirectories(physicalPath.getParent());
      Files.copy(file.getInputStream(), physicalPath);
    } catch (Exception e) {
      throw new RuntimeException("Could not save file: " + file.getOriginalFilename());
    }
  }

  /**
   * Deletes an image from both the database and the filesystem.
   *
   * @param imageId the ID of the image to delete
   * @throws ImageNotFoundException if the image does not exist
   * @throws ImageUploadUpdateException if the file cannot be deleted from the filesystem
   */
  @Transactional
  public void deleteImage(String imageId) {
    Image image =
        imageRepository.findById(UUID.fromString(imageId)).orElseThrow(ImageNotFoundException::new);

    Product product = image.getProduct();

    try {
      String relativeUrl = image.getUrl().replaceFirst("^/", "");
      Path path = Paths.get("src/main/resources/static", relativeUrl);
      Files.deleteIfExists(path);
    } catch (IOException e) {
      throw new ImageUploadUpdateException("Could not delete file.");
    }

    product.getImages().remove(image);
    image.setProduct(null);

    imageRepository.delete(image);
  }
}
