package com.bugnbass.backend.controller;

import com.bugnbass.backend.model.Image;
import com.bugnbass.backend.service.ImageService;
import java.util.List;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller for handling general image management operations. Provides endpoints for retrieving,
 * adding, and deleting images.
 */
@RestController
@RequestMapping("/api/images")
public class OldImageController {

  private final ImageService imageService;

  /**
   * Constructs an OldImageController with the given ImageService.
   *
   * @param imageService the service for image operations
   */
  public OldImageController(ImageService imageService) {
    this.imageService = imageService;
  }

  /**
   * Retrieves an image by its ID.
   *
   * @param imageId the ID of the image
   * @return the Image object with the specified ID
   */
  @GetMapping("/{imageId}")
  public Image getImage(@RequestParam String imageId) {
    return imageService.getImage(imageId);
  }

  /**
   * Retrieves all images associated with a specific product.
   *
   * @param productId the ID of the product
   * @return a list of images for the specified product
   */
  @GetMapping("pr/{productId}")
  public List<Image> getProductImages(@RequestParam String productId) {
    return imageService.getProductImages(productId);
  }

  /**
   * Adds an image to a product.
   *
   * @param file the image file to upload
   * @param productId the ID of the product
   * @return a ResponseEntity with HTTP status 201 (Created)
   */
  @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
  public ResponseEntity<Void> addImage(
      @RequestPart MultipartFile file, @RequestParam String productId) {
    imageService.addImageToProduct(file, productId);
    return ResponseEntity.status(HttpStatus.CREATED).build();
  }

  /**
   * Deletes an image by its ID.
   *
   * @param imageId the ID of the image to delete
   * @return a ResponseEntity with HTTP status 204 (No Content)
   */
  @DeleteMapping("/{imageId}")
  public ResponseEntity<Void> deleteImage(@PathVariable String imageId) {
    imageService.deleteImage(imageId);
    return ResponseEntity.noContent().build();
  }
}
