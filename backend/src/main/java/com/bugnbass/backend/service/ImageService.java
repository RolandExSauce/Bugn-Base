package com.bugnbass.backend.service;
import com.bugnbass.backend.dto.ImageDTO;
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

@Service
public class ImageService {

    private final ProductRepository productRepository;
    private final ImageRepository imageRepository;

    public ImageService(ProductRepository productRepository,ImageRepository imageRepository) {
        this.imageRepository = imageRepository;
        this.productRepository = productRepository;
    }

    public Image getImage (String imageId) {
        return imageRepository.findById(imageId)
                .orElseThrow(ImageNotFoundException::new);
    }

    public List<Image> getProductImages(String productId) {
        List<Image> images = imageRepository.findAllByProduct_ProductId(productId);
        if (images.isEmpty()) {
            throw new ImageNotFoundException();
        }
        return images;
    }

    @Transactional
    public void addImageToProduct(MultipartFile file, String productId) {

        Product product = productRepository.findById(productId)
                .orElseThrow(ProductNotFoundException::new);


        String fileName = file.getOriginalFilename();
        String category = product.getCategory().toString().toLowerCase();
        Path physicalPath = Paths.get("src/main/resources/static/product_images", category, fileName);
        Path dbPath = Paths.get("/product_images", category, fileName);


        Image image = new Image();
        image.setUrl(dbPath.toString());
        image.setProduct(product);
        imageRepository.save(image);

        try {
            Files.copy(file.getInputStream(), physicalPath);
        } catch (IOException e) {
            throw new ImageUploadUpdateException();
        }
    }

    @Transactional
    public void deleteImage(String imageId) {

        Image image = imageRepository.findById(imageId)
                .orElseThrow(ImageNotFoundException::new);

        Product product = image.getProduct();


        try {
            String relativeUrl = image.getUrl().replaceFirst("^/", "");
            Path path = Paths.get("src/main/resources/static", relativeUrl);

            Files.delete(path);
        }
        catch (IOException e) {
            throw new RuntimeException("Error...");
        }
        catch (Exception e) {
            throw new ImageUploadUpdateException("Error...");
        }

        product.getImages().remove(image);
        image.setProduct(null);
        try {
            imageRepository.deleteById(image.getImageId());
        }
        catch (Exception e) {
            throw new ImageNotFoundException("Image is missing in the DB");
        }

    }

    @Transactional
    public void updateImage(ImageDTO imageDTO) {
        // Image image = getImage(imageDTO.imageId());
        // später zu implementieren:
        // vl hat image fields: alt, das editiert werden kann...
    }

}
