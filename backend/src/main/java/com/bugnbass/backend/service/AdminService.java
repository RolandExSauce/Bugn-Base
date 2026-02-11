package com.bugnbass.backend.service;

import com.bugnbass.backend.dto.AdminUpdateUserDto;
import com.bugnbass.backend.dto.ProductDto;
import com.bugnbass.backend.exceptions.ProductNotFoundException;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.repository.ProductRepository;
import com.bugnbass.backend.repository.UserRepository;
import com.bugnbass.backend.validator.ProductValidator;
import java.util.List;
import java.util.UUID;

import jakarta.persistence.EntityNotFoundException;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service class for admin-specific product operations such as retrieval,
 * creation, update, and soft deletion.
 */
@Service
public class AdminService {

    /**
     * Repository for CRUD operations on Product entities.
     */
    private final ProductRepository productRepository;

    private final UserRepository userRepository;

    /**
     * Validator for product data.
     */
    private final ProductValidator productValidator;

    /**
     * Constructs the AdminService with required dependencies.
     *
     * @param productRepository the ProductRepository instance
     * @param productValidator  the ProductValidator instance
     */
    public AdminService(ProductRepository productRepository, UserRepository userRepository, ProductValidator productValidator) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.productValidator = productValidator;
    }

    /**
     * Retrieves a product by its ID.
     *
     * @param id the product ID as String
     * @return the Product object
     * @throws ProductNotFoundException if product with given ID does not exist
     */
    public Product getProduct(String id) {
        return productRepository.findById(Long.valueOf(id))
                .orElseThrow(ProductNotFoundException::new);
    }

    /**
     * Retrieves all products.
     *
     * @return a list of all Product objects
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * Adds a new product to the repository.
     *
     * @param dto the ProductDTO containing product data
     * @return the saved Product object
     */
    public Product addProduct(ProductDto dto) {
        productValidator.validateProductData(dto);
        Product product = Product.builder()
                .name(dto.name())
                .category(dto.category())
                .description(dto.description())
                .price(dto.price())
                .shippingCost(dto.shippingCost())
                .brand(dto.brand())
                .stockStatus(dto.stockStatus())
                .shippingTime(dto.shippingTime())
                .active(dto.active())
                .build();
        return productRepository.save(product);
    }

    /**
     * Soft deletes a product by setting its active flag to false.
     * Note: Does not remove the product from the database.
     *
     * @param id the product ID as String
     */
    public void deleteProduct(String id) {
        Product product = getProduct(id);
        product.setActive(false);
    }

    /**
     * Updates an existing product with provided data.
     *
     * @param id  the product ID as String
     * @param dto the ProductDTO containing updated product data
     */
    public void updateProduct(String id, ProductDto dto) {
        Long productId = Long.valueOf(id);
        Product product = getProduct(id);
        productValidator.validateProductData(dto, productId);

        if (dto.name() != null) {
            product.setName(dto.name());
        }
        if (dto.category() != null) {
            product.setCategory(dto.category());
        }
        if (dto.description() != null) {
            product.setDescription(dto.description());
        }
        if (dto.price() != null) {
            product.setPrice(dto.price());
        }
        if (dto.shippingCost() != null) {
            product.setShippingCost(dto.shippingCost());
        }
        if (dto.brand() != null) {
            product.setBrand(dto.brand());
        }
        if (dto.shippingTime() != null) {
            product.setShippingTime(dto.shippingTime());
        }
        if (dto.stockStatus() != null) {
            product.setStockStatus(dto.stockStatus());
        }
        if (dto.active() != null) {
            product.setActive(dto.active());
        }

        productRepository.save(product);
    }

    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    @Transactional(readOnly = true)
    public User getUserById(String id) {
        UUID uuid = parseUuidOrThrow(id);
        return userRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
    }

    public User updateUser(String id, AdminUpdateUserDto dto) {
        UUID uuid = parseUuidOrThrow(id);

        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));

        user.setFirstname(dto.firstname());
        user.setLastname(dto.lastname());
        user.setPhone(dto.phone());
        user.setAddress(dto.address());
        user.setPostcode(dto.postcode());
        user.setEmail(dto.email());
        user.setActive(dto.active());
        user.setRole(dto.role());

        return userRepository.save(user);
    }

    public void deleteUser(String id) {
        UUID uuid = parseUuidOrThrow(id);

        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));

        // Soft delete empfohlen:
        user.setActive(false);
        userRepository.save(user);

        // Hard delete, falls du willst:
        // userRepository.delete(user);
    }

    private UUID parseUuidOrThrow(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            // 400 statt 500:
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID: " + id);
        }
    }

}
