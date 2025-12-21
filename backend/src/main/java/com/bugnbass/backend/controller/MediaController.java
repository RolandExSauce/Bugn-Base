package com.bugnbass.backend.controller;

import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.service.MediaService;
import com.bugnbass.backend.service.ProductService;
import jakarta.servlet.http.HttpServletRequest;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.multipart.MultipartFile;

/**
 * REST controller for media-related operations.
 * Provides endpoints for retrieving, uploading, and deleting media files such as product images.
 *
 * <p>Upload and deletion operations are restricted to users with ROLE_ADMIN authority.</p>
 */
@RestController
@RequestMapping("/bugnbass/api/media")
public class MediaController {

    /** Service handling media operations such as storage and retrieval. */
    private final MediaService mediaService;

    /** Service handling product-related operations. */
    private final ProductService productService;

    /**
     * Constructs the MediaController with required services.
     *
     * @param mediaService   the MediaService instance
     * @param productService the ProductService instance
     */
    public MediaController(MediaService mediaService, ProductService productService) {
        this.mediaService = mediaService;
        this.productService = productService;
    }

    /**
     * Retrieves a media file based on the request URI.
     *
     * @param request the HttpServletRequest containing the media path
     * @return ResponseEntity containing the media as a Resource and content type
     */
    @GetMapping("/**")
    public ResponseEntity<Resource> getImage(HttpServletRequest request) {
        String path = request.getRequestURI().replaceFirst(".*/media/", "");
        Resource image = mediaService.getImage(path);
        return ResponseEntity.ok()
                .contentType(MediaType.APPLICATION_OCTET_STREAM)
                .body(image);
    }

    /**
     * Uploads an image file for a specific product.
     * Requires ROLE_ADMIN authority.
     *
     * @param productId the ID of the product
     * @param file      the image file to upload
     * @return ResponseEntity containing the URL of the uploaded image
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @PostMapping("/file/{productId}")
    public ResponseEntity<String> uploadProductImage(
            @PathVariable(name = "productId") Long productId,
            @RequestParam("file") MultipartFile file
    ) {
        Product product = productService.getProduct(productId);
        String categoryFolder = product.getCategory().name().toLowerCase();

        String url = mediaService.uploadImage(file, categoryFolder, product);

        return ResponseEntity.ok(url);
    }

    /**
     * Deletes an image file from the storage.
     * Requires ROLE_ADMIN authority.
     *
     * @param folder   the folder containing the image
     * @param filename the name of the image file
     * @return ResponseEntity with HTTP 204 (No Content) status
     */
    @PreAuthorize("hasRole('ROLE_ADMIN')")
    @DeleteMapping("/file/delete/{folder}/{filename:.+}")
    public ResponseEntity<Void> deleteImage(
            @PathVariable(name = "folder") String folder,
            @PathVariable(name = "filename") String filename
    ) {
        mediaService.deleteImage(folder + "/" + filename);
        return ResponseEntity.noContent().build();
    }
}
