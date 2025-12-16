package com.bugnbass.backend.service;
import com.bugnbass.backend.dto.ProductFilter;
import com.bugnbass.backend.dto.ProductResponseDTO;
import com.bugnbass.backend.exceptions.ProductNotFoundException;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.repository.ProductRepository;
import org.springframework.stereotype.Service;
import java.util.List;

@Service
public class ProductService {

    private final ProductRepository productRepository;

    public ProductService(ProductRepository productRepository) {
        this.productRepository = productRepository;
    }

    public Product getProduct(String id) {
        return productRepository.findByIdAndActiveTrue(Long.valueOf(id))
                .orElseThrow(ProductNotFoundException::new);
    }

    public List<ProductResponseDTO> getProducts(ProductFilter filter) {

        final int DEFAULT_PAGE_SIZE = 25;
        final int DEFAULT_PAGE_NUMBER = 0;

        List<Product> allProducts = productRepository.findAll().stream()
                .filter(Product::getActive)
                .toList();


        return allProducts.stream()
                .filter(p -> filter.name() == null ||
                        p.getName().toLowerCase().contains(filter.name().toLowerCase()))
                .filter(p -> filter.category() == null ||
                        p.getCategory().equals(filter.category()))
                .filter(p -> filter.priceMin() == null ||
                        p.getPrice() >= filter.priceMin())
                .filter(p -> filter.priceMax() == null ||
                        p.getPrice() <= filter.priceMax())
                .filter(p -> filter.brand() == null ||
                        filter.brand().contains(p.getBrand()))
                .skip(filter.pageNumber() == null ? DEFAULT_PAGE_NUMBER :
                        (long) filter.pageNumber() * filter.pageSize())
                .limit(filter.pageSize() == null ? DEFAULT_PAGE_SIZE : filter.pageSize())
                .map(ProductResponseDTO::fromEntity)
                .toList();
    }
}
