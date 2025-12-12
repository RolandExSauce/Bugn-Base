package com.bugnbass.backend.controller;

import com.bugnbass.backend.model.Image;
import com.bugnbass.backend.service.ImageService;
import java.util.List;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

/**
 * Controller for handling image management related to products. Provides endpoints for uploading,
 * retrieving, and deleting product images.
 */
@RestController
@RequestMapping("/api/admin/products")
@RequiredArgsConstructor
public class ImageController {

  private final ImageService imageService;

  /**
   * Uploads multiple images for a specific product.
   *
   * @param productId the ID of the product
   * @param files the list of image files to upload
   * @return a ResponseEntity with HTTP status 201 and a success message
   */
  @PostMapping("/{productId}/images")
  public ResponseEntity<String> uploadImages(
      @PathVariable String productId, @RequestParam("files") List<MultipartFile> files) {

    files.forEach(file -> imageService.addImageToProduct(file, productId));
    return ResponseEntity.status(HttpStatus.CREATED).body("Images uploaded successfully.");
  }

  /**
   * Retrieves all images associated with a specific product.
   *
   * @param productId the ID of the product
   * @return a list of images belonging to the product
   */
  @GetMapping("/{productId}/images")
  public List<Image> getImages(@PathVariable String productId) {
    return imageService.getProductImages(productId);
  }

  /**
   * Deletes a specific image from a product.
   *
   * @param productId the ID of the product
   * @param imageId the ID of the image to delete
   * @return a ResponseEntity with HTTP status 204 (No Content)
   */
  @DeleteMapping("/{productId}/images/{imageId}")
  public ResponseEntity<Void> deleteImage(
      @PathVariable String productId, @PathVariable String imageId) {
    imageService.deleteImage(imageId);
    return ResponseEntity.noContent().build();
  }
}
