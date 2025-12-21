package com.bugnbass.backend.service;

import com.bugnbass.backend.model.Product;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Objects;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

/**
 * Service for managing media files, including image upload, retrieval, and deletion.
 */
@Service
public class MediaService {

    /**
     * Service for associating images with products.
     */
    private final ImageService imageService;

    /**
     * Root directory for storing media files.
     */
    private final Path mediaRoot;

    /**
     * Constructs the MediaService with the media root path and ImageService.
     *
     * @param mediaRootPath the root path for storing media files
     * @param imageService  the ImageService instance
     */
    public MediaService(
            @Value("${media.root}") String mediaRootPath,
            ImageService imageService) {
        this.mediaRoot = Paths.get(mediaRootPath).normalize();
        this.imageService = imageService;
    }

    /**
     * Uploads an image file to a specified subdirectory
     * and optionally associates it with a product.
     *
     * @param file    the image file to upload
     * @param subDir  the subdirectory under the media root
     * @param product the product to associate the image with (optional)
     * @return the URL path of the uploaded image
     */
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

    /**
     * Retrieves an image resource by its relative path.
     *
     * @param relativePath the relative path of the image under the media root
     * @return the Resource representing the image
     */
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

    /**
     * Deletes an image file by its relative path.
     *
     * @param relativePath the relative path of the image under the media root
     */
    public void deleteImage(String relativePath) {
        try {
            System.out.println("deleting image " + relativePath);
            Path filePath = mediaRoot.resolve(relativePath).normalize();
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException("Failed to delete image", e);
        }
    }
}
