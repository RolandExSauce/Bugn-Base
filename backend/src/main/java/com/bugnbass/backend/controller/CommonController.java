package com.bugnbass.backend.controller;

import com.bugnbass.backend.model.enums.ProductCategory;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequestMapping("/api")
// @CrossOrigin(origins = "http://localhost:3000") // fürs frontend später
public class CommonController {

    @GetMapping("/categories")
    public ProductCategory[] getCategories() {
        return ProductCategory.values();
    }

}
