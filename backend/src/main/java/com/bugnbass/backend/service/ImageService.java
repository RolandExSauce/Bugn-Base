package com.bugnbass.backend.service;
import com.bugnbass.backend.exceptions.ImageNotFoundException;
import com.bugnbass.backend.exceptions.ImageUploadUpdateException;
import com.bugnbass.backend.exceptions.ProductNotFoundException;
import com.bugnbass.backend.model.Image;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.repository.ImageRepository;
import com.bugnbass.backend.repository.ProductRepository;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;
import java.io.IOException;
import java.nio.file.*;
import java.util.List;
import java.util.UUID;

@Service
public class ImageService {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

    public ImageService(ProductRepository productRepository, ImageRepository imageRepository) {
        this.productRepository = productRepository;
        this.imageRepository = imageRepository;
    }
    /******************************************************************************************************************/
    public List<Image> getProductImages(String productId) {
        Long id = Long.valueOf(productId);
        List<Image> images = imageRepository.findAllByProduct_Id(id);

        if (images.isEmpty()) {
            throw new ImageNotFoundException("No images found for product " + productId);
        }
        return images;
    }
    /******************************************************************************************************************/
    @Transactional
    public void addImageToProduct(MultipartFile file, String productId) {

        Product product = productRepository.findById(Long.valueOf(productId))
                .orElseThrow(ProductNotFoundException::new);

        String fileName = UUID.randomUUID() + "-" + file.getOriginalFilename();
        String category = product.getCategory().name().toLowerCase();

        Path physicalPath = Paths.get(
                "src/main/resources/static/product_images",
                category,
                fileName
        );
        String url = "/product_images/" + category + "/" + fileName;
        Image image = new Image();
        image.setUrl(url);
        image.setProduct(product);

        imageRepository.save(image);

        try {
            Files.createDirectories(physicalPath.getParent());
            Files.copy(file.getInputStream(), physicalPath);
        } catch (Exception e) {
            throw new RuntimeException("Could not save file: " + file.getOriginalFilename());
        }
    }
    /******************************************************************************************************************/
    @Transactional
    public void deleteImage(String imageId) {
        Image image = imageRepository.findById(UUID.fromString(imageId))
                .orElseThrow(ImageNotFoundException::new);

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
