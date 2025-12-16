package com.bugnbass.backend.controller;
import com.bugnbass.backend.dto.ProductFilter;
import com.bugnbass.backend.dto.ProductResponseDTO;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.enums.ProductCategory;
import com.bugnbass.backend.service.ProductService;
import org.springframework.web.bind.annotation.*;
        import java.util.List;

@RestController
@RequestMapping("/bugnbass/api/shop")
public class ShopProductsController {

    private final ProductService userProductService;

    public ShopProductsController(ProductService userProductService) {
        this.userProductService = userProductService;
    }

    @GetMapping("/products/product/{id}")
    public Product getProduct(@PathVariable String id) {
        return userProductService.getProduct(id);
    }

    @GetMapping("/products")
    public List<ProductResponseDTO> getProducts (
            //@RequestParam(required = false) String id,
            @RequestParam(required = false) String name,
            @RequestParam(required = false) ProductCategory category,
            @RequestParam(required = false) Double priceMin,
            @RequestParam(required = false) Double priceMax,
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