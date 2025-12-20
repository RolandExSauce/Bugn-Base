package com.bugnbass.backend.controller;

import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.service.MediaService;
import com.bugnbass.backend.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

@RestController
@RequestMapping("/media")
public class MediaController {

  private final MediaService mediaService;
  private final ProductService productService;

  public MediaController(
      MediaService mediaService,
      ProductService productService) {
    this.mediaService = mediaService;
    this.productService = productService;
  }

  @GetMapping("/**")
  public ResponseEntity<Resource> getImage(HttpServletRequest request) {
    String path = request.getRequestURI().replaceFirst(".*/media/", "");
    Resource image = mediaService.getImage(path);
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(image);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @PostMapping("/file/{productId}")
  public ResponseEntity<String> uploadProductImage(
      @PathVariable Long productId,
      @RequestParam("file") MultipartFile file
  ) {
    Product product = productService.getProduct(productId);
    String categoryFolder = product.getCategory().name().toLowerCase();

    String url = mediaService.uploadImage(file, categoryFolder, product);

    return ResponseEntity.ok(url);
  }

  @PreAuthorize("hasRole('ROLE_ADMIN')")
  @DeleteMapping("/file/delete/{folder}/{filename:.+}")
  public ResponseEntity<Void> deleteImage(
      @PathVariable String folder,
      @PathVariable String filename
  ) {
    mediaService.deleteImage(folder + "/" + filename);
    return ResponseEntity.noContent().build();
  }

}
