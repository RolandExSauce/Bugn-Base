package com.bugnbass.backend.controller.admin;
import com.bugnbass.backend.model.Image;
import com.bugnbass.backend.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import java.util.List;

@RestController
@RequestMapping("/bugnbass/api/admin/products")
@RequiredArgsConstructor
public class AdminImageController {

    private final ImageService imageService;

    @PostMapping("/{productId}/images")
    public ResponseEntity<String> uploadImages(
            @PathVariable String productId,
            @RequestParam("files") List<MultipartFile> files
    ) {
        files.forEach(file -> imageService.addImageToProduct(file, productId));
        return ResponseEntity.status(HttpStatus.CREATED)
                .body("Images uploaded successfully.");
    }

    @GetMapping("/{productId}/images")
    public List<Image> getImages(@PathVariable String productId) {
        return imageService.getProductImages(productId);
    }

    @DeleteMapping("/images/{imageId}")
    public ResponseEntity<Void> deleteImage(@PathVariable String imageId) {
        imageService.deleteImage(imageId);
        return ResponseEntity.noContent().build();
    }
}
