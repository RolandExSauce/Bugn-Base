package com.bugnbass.backend.service;

import com.bugnbass.backend.dto.AdminUpdateUserDto;
import com.bugnbass.backend.dto.ProductDto;
import com.bugnbass.backend.exceptions.ProductNotFoundException;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.User;
import com.bugnbass.backend.repository.ProductRepository;
import com.bugnbass.backend.repository.UserRepository;
import com.bugnbass.backend.validator.ProductValidator;
import jakarta.persistence.EntityNotFoundException;
import java.util.List;
import java.util.UUID;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.web.server.ResponseStatusException;

/**
 * Service responsible for administrative operations related to products and users.
 *
 * <p>This service provides functionality for administrators to:
 * <ul>
 *     <li>Manage products (create, update, retrieve, soft delete)</li>
 *     <li>Manage users (retrieve, update, deactivate)</li>
 * </ul>
 *
 * <p>Business validation is delegated to dedicated validator components where applicable.
 */
@Service
public class AdminService {

    /**
     * Repository for CRUD operations on {@link Product} entities.
     */
    private final ProductRepository productRepository;

    /**
     * Repository for CRUD operations on {@link User} entities.
     */
    private final UserRepository userRepository;

    /**
     * Validator responsible for validating product data before persistence.
     */
    private final ProductValidator productValidator;

    /**
     * Constructs the AdminService with required dependencies.
     *
     * @param productRepository repository for product persistence
     * @param userRepository    repository for user persistence
     * @param productValidator  validator for product business rules
     */
    public AdminService(
            ProductRepository productRepository,
            UserRepository userRepository,
            ProductValidator productValidator) {
        this.productRepository = productRepository;
        this.userRepository = userRepository;
        this.productValidator = productValidator;
    }

    /**
     * Retrieves a product by its identifier.
     *
     * @param id the product identifier as string
     * @return the {@link Product} entity
     * @throws ProductNotFoundException if no product with the given ID exists
     */
    public Product getProduct(String id) {
        return productRepository.findById(Long.valueOf(id))
                .orElseThrow(ProductNotFoundException::new);
    }

    /**
     * Retrieves all products.
     *
     * @return list of all {@link Product} entities
     */
    public List<Product> getProducts() {
        return productRepository.findAll();
    }

    /**
     * Creates and persists a new product.
     *
     * <p>Product data is validated before persistence.
     *
     * @param dto the product data transfer object
     * @return the persisted {@link Product}
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
     * Soft deletes a product by marking it as inactive.
     *
     * <p>The product remains in the database but will typically be excluded
     * from active listings.
     *
     * @param id the product identifier as string
     */
    public void deleteProduct(String id) {
        Product product = getProduct(id);
        product.setActive(false);
    }

    /**
     * Updates an existing product with provided values.
     *
     * <p>Only non-null fields in the DTO are applied.
     *
     * @param id  the product identifier as string
     * @param dto the product update data
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

    /**
     * Retrieves all users.
     *
     * @return list of all {@link User} entities
     */
    @Transactional(readOnly = true)
    public List<User> getUsers() {
        return userRepository.findAll();
    }

    /**
     * Retrieves a user by UUID.
     *
     * @param id the user identifier as string
     * @return the {@link User} entity
     * @throws EntityNotFoundException if user does not exist
     */
    @Transactional(readOnly = true)
    public User getUserById(String id) {
        UUID uuid = parseUuidOrThrow(id);

        return userRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));
    }

    /**
     * Updates user profile and administrative properties.
     *
     * @param id  the user identifier as string
     * @param dto the update data transfer object
     * @return the updated {@link User}
     */
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

    /**
     * Soft deletes a user by marking the account as inactive.
     *
     * <p>The user remains in the database but is disabled.
     *
     * @param id the user identifier as string
     */
    public void deleteUser(String id) {
        UUID uuid = parseUuidOrThrow(id);

        User user = userRepository.findById(uuid)
                .orElseThrow(() -> new EntityNotFoundException("User not found: " + id));

        user.setActive(false);
        userRepository.save(user);
    }

    /**
     * Parses a UUID from string or throws a {@link ResponseStatusException}
     * with HTTP 400 if the format is invalid.
     *
     * @param id the UUID string
     * @return parsed {@link UUID}
     */
    private UUID parseUuidOrThrow(String id) {
        try {
            return UUID.fromString(id);
        } catch (IllegalArgumentException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Invalid UUID: " + id);
        }
    }
}
