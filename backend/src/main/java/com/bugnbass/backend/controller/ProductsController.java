package com.bugnbass.backend.controller;
import com.bugnbass.backend.dto.ProductFilter;
import com.bugnbass.backend.dto.ProductResponseDTO;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.enums.ProductCategory;
import com.bugnbass.backend.service.ProductService;
import org.springframework.web.bind.annotation.*;
        import java.util.List;

@RestController
@RequestMapping("/products")
public class ProductsController {

    private final ProductService userProductService;

    public ProductsController(ProductService userProductService) {
        this.userProductService = userProductService;
    }

    @GetMapping("/{id}")
    public Product getProduct(@PathVariable Long id) {
        return userProductService.getProduct(id);
    }

    @GetMapping()
    public List<ProductResponseDTO> getProducts (
            //@RequestParam(required = false) String id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) Integer priceMin,
            @RequestParam(required = false) Integer priceMax,
            @RequestParam(required = false) List<String> brand,
            @RequestParam(required = false) Integer pageNo,
            @RequestParam(required = false) Integer pageSize
    ) {
        ProductFilter productFilters = new ProductFilter(
                name,
                category,
                priceMin,
                priceMax,
                brand,
                pageNo,
                pageSize);
        return userProductService.getProducts(productFilters);
    }
}