package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.ImageDTO;
import com.bugnbass.backend.model.Image;
import com.bugnbass.backend.service.ImageService;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;

@RestController
@RequestMapping("/api/images")
// @CrossOrigin(origins = "http://localhost:3000") // fürs frontend später
public class ImageController {

    private final ImageService imageService;

    public ImageController(ImageService imageService) {
        this.imageService = imageService;
    }

    @GetMapping("/{imageId}")
    public Image getImage(@RequestParam String imageId) {
        return imageService.getImage(imageId);
    }

    @GetMapping("/{productId}")
    public List<Image> getProductImages(@RequestParam String productId) {
        return imageService.getProductImages(productId);
    }

    @PostMapping(consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<Void> addImage(@RequestPart MultipartFile file, @RequestParam String productId) {
        imageService.addImageToProduct(file, productId);
        return ResponseEntity.status(HttpStatus.CREATED).build();
    }

    @PutMapping("/{imageId}")
    public ResponseEntity<Void> updateImage(@RequestBody ImageDTO imageDTO) {
        imageService.updateImage(imageDTO);
        return ResponseEntity.status(HttpStatus.OK).build();
    }

    @DeleteMapping("/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable String imageId) {
        imageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }

}
