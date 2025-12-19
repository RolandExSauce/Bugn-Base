package com.bugnbass.backend.service;

import com.bugnbass.backend.model.Product;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.util.Objects;

@Service
public class MediaService {



  private final ImageService imageService;

  private final Path mediaRoot;

  public MediaService(
      @Value("${media.root}") String mediaRootPath,
      ImageService imageService) {
    this.mediaRoot = Paths.get(mediaRootPath).normalize();
    this.imageService = imageService;
  }

  public String uploadImage(
      MultipartFile file,
      String subDir,
      Product product) {
    try {
      String originalName = Objects.requireNonNull(file.getOriginalFilename());

      String extension = "";
      int dotIndex = originalName.lastIndexOf('.');
      if (dotIndex >= 0) {
        extension = originalName.substring(dotIndex);
        originalName = originalName.substring(0, dotIndex);
      }

      String safeBase = originalName.replaceAll("[^A-Za-z0-9_-]", "");
      String fileName = System.currentTimeMillis() + "_" + safeBase + extension;

      Path targetDir = mediaRoot.resolve(subDir);
      Files.createDirectories(targetDir);

      Path targetFile = targetDir.resolve(fileName);
      Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);

      String imageUrl = "/media/" + subDir + "/" + fileName;

      if (product != null) {
        imageService.addImageToProduct(product, imageUrl);
      }

      return imageUrl;

    } catch (IOException e) {
      throw new RuntimeException("Failed to upload image", e);
    }
  }

  public Resource getImage(String relativePath) {
    try {
      Path filePath = mediaRoot.resolve(relativePath).normalize();
      Resource resource = new UrlResource(filePath.toUri());

      if (!resource.exists() || !resource.isReadable()) {
        throw new RuntimeException("Image not found: " + relativePath);
      }

      return resource;

    } catch (MalformedURLException e) {
      throw new RuntimeException("Invalid image path", e);
    }
  }

  public void deleteImage(String relativePath) {
    try {
      Path filePath = mediaRoot.resolve(relativePath).normalize();
      Files.deleteIfExists(filePath);
    } catch (IOException e) {
      throw new RuntimeException("Failed to delete image", e);
    }
  }
}
