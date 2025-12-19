package com.bugnbass.backend.service;

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

  private static final Path MEDIA_ROOT = Paths.get("media");

  public String uploadImage(MultipartFile file, String subDir) {
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

      Path targetDir = MEDIA_ROOT.resolve(subDir);
      Files.createDirectories(targetDir);

      Path targetFile = targetDir.resolve(fileName);
      Files.copy(file.getInputStream(), targetFile, StandardCopyOption.REPLACE_EXISTING);

      return "/media/" + subDir + "/" + fileName;

    } catch (IOException e) {
      throw new RuntimeException("Failed to upload image", e);
    }
  }

  public Resource getImage(String relativePath) {
    try {
      Path filePath = MEDIA_ROOT.resolve(relativePath).normalize();
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
      Path filePath = MEDIA_ROOT.resolve(relativePath).normalize();
      Files.deleteIfExists(filePath);
    } catch (IOException e) {
      throw new RuntimeException("Failed to delete image", e);
    }
  }
}
