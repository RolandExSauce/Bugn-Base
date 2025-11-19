package com.bugnbass.backend.service;
import com.bugnbass.backend.dto.ProductDTO;
import com.bugnbass.backend.exceptions.ProductNotFoundException;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.enums.ProductCategory;
import com.bugnbass.backend.repository.ImageRepository;
import com.bugnbass.backend.repository.ProductRepository;
import com.bugnbass.backend.validator.ProductValidator;
import jakarta.transaction.Transactional;
import org.springframework.stereotype.Service;
import java.util.List;


@Service
public class AdminProductService {
    private final ProductRepository productRepository;
    private final ProductValidator productValidator;
    private final ImageRepository imageRepository;
    private final ImageService imageService;

    public AdminProductService (ProductRepository productRepository, ProductValidator productValidator, ImageRepository imageRepository, ImageService imageService) {
        this.productRepository = productRepository;
        this.productValidator = productValidator;
        this.imageRepository = imageRepository;
        this.imageService = imageService;
    }

    public Product getProduct (String id) {
        return productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);
    }

    public List<Product> getProducts () {
        return productRepository.findAll();
    }

    @Transactional
    public void addProduct(ProductDTO productDTO) {

        Product product;
        productValidator.validateProductData(productDTO);

        try {
            product = new Product();

            product.setName(productDTO.name());
            product.setCategory(ProductCategory.valueOf(productDTO.category().toUpperCase()));
            product.setDescription(productDTO.description());
            product.setPrice(productDTO.price());
            product.setShippingCost(productDTO.shippingCost());
            product.setBrand(productDTO.brand());
            product.setInStock(productDTO.stockStatus());
            product.setShippingTime(productDTO.shippingTime());
            product.setActive(productDTO.active());

            productRepository.save(product);
        }
        catch (Exception e) {
            throw new RuntimeException("Produkt konnte nicht angelegt werden.");
        }

    }

    @Transactional
    public void deleteProduct (String id) {

        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        product.setActive(false);

        //  Achtung. Lösche die Bilder nicht wirklich!
        //  Produktbilder sind in development geteilt...
        //
        // try {
        //     Iterator<Image> imagesIterator = product.getImages().iterator();

        //     while(imagesIterator.hasNext()) {
        //         Image image = imagesIterator.next();
        //         imagesIterator.remove();
        //         imageService.deleteImage(image.getImageId());
        //     }
        // }
        // catch (Exception e) {
        //     throw new RuntimeException("Produkt konnte nicht angelegt werden.");
        // }
    }


    @Transactional
    public void updateProduct(String id, ProductDTO productDTO) {

        Product product = productRepository.findById(id)
                .orElseThrow(ProductNotFoundException::new);

        productValidator.validateProductData(productDTO);

        try {
            if (productDTO.name() != null) product.setName(productDTO.name());
            if (productDTO.category() != null) product.setCategory(ProductCategory.valueOf(productDTO.category().toUpperCase()));
            if (productDTO.description() != null) product.setDescription(productDTO.description());
            if (productDTO.price() != null) product.setPrice(productDTO.price());
            if (productDTO.shippingCost() != null) product.setShippingCost(productDTO.shippingCost());
            if (productDTO.brand() != null) product.setBrand(productDTO.brand());
            if (productDTO.shippingTime() != null) product.setShippingTime(productDTO.shippingTime());
            if (productDTO.stockStatus() != null) product.setInStock(productDTO.stockStatus());
            if (productDTO.active() != null) product.setActive(productDTO.active());
        }
        catch (Exception e) {
            throw new RuntimeException("Fehler beim Updaten...");
        }
    }

}
