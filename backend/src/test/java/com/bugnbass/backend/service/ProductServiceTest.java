package com.bugnbass.backend.service;

import com.bugnbass.backend.dto.ProductFilter;
import com.bugnbass.backend.dto.ProductResponseDto;
import com.bugnbass.backend.exceptions.ProductNotFoundException;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.enums.ProductCategory;
import com.bugnbass.backend.repository.ProductRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    private Product activeP1;
    private Product activeP2;
    private Product inactiveP;

    private ProductCategory category;

    @BeforeEach
    void setUp() {
        category = ProductCategory.class.getEnumConstants()[0];

        activeP1 = product(1L, true, "Jazz Bass", "Fender", 999, category);
        activeP2 = product(2L, true, "Precision Bass", "Ibanez", 499, category);
        inactiveP = product(3L, false, "Hidden", "NoBrand", 123, category);
    }

    private Product product(Long id, boolean active, String name, String brand, int price, ProductCategory cat) {
        return Product.builder()
                .id(id)
                .active(active)
                .name(name)
                .brand(brand)
                .price(price)
                .category(cat)
                .description("desc")
                .shippingCost(10)
                .stockStatus(null)
                .shippingTime(3)
                .images(List.of())
                .build();
    }

    @Test
    void getProduct_returnsProductWhenFoundAndActive() {
        when(productRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(activeP1));

        Product result = productService.getProduct(1L);

        assertThat(result).isSameAs(activeP1);
        verify(productRepository).findByIdAndActiveTrue(1L);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void getProduct_throwsWhenNotFound() {
        when(productRepository.findByIdAndActiveTrue(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProduct(99L))
                .isInstanceOf(ProductNotFoundException.class);

        verify(productRepository).findByIdAndActiveTrue(99L);
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void getProducts_filtersOutInactiveAlways() {
        when(productRepository.findAll()).thenReturn(List.of(activeP1, inactiveP, activeP2));

        ProductFilter filter = new ProductFilter(null, null, null, null, null, null, null);

        List<ProductResponseDto> result = productService.getProducts(filter);

        assertThat(result).extracting(ProductResponseDto::id).containsExactly(1L, 2L);
        verify(productRepository).findAll();
        verifyNoMoreInteractions(productRepository);
    }

    @Test
    void getProducts_filtersByName_caseInsensitiveContains() {
        when(productRepository.findAll()).thenReturn(List.of(activeP1, activeP2));

        ProductFilter filter = new ProductFilter("jAzZ", null, null, null, null, null, null);

        List<ProductResponseDto> result = productService.getProducts(filter);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).name()).isEqualTo("Jazz Bass");
    }

    @Test
    void getProducts_filtersByCategory() {
        when(productRepository.findAll()).thenReturn(List.of(activeP1, activeP2));

        ProductFilter filter = new ProductFilter(null, category, null, null, null, null, null);

        List<ProductResponseDto> result = productService.getProducts(filter);

        assertThat(result).hasSize(2);
        assertThat(result).allMatch(dto -> dto.category() == category);
    }

    @Test
    void getProducts_filtersByPriceMinAndMax() {
        when(productRepository.findAll()).thenReturn(List.of(activeP1, activeP2));

        ProductFilter filter = new ProductFilter(null, null, 500, 1000, null, null, null);

        List<ProductResponseDto> result = productService.getProducts(filter);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(1L);
    }

    @Test
    void getProducts_filtersByBrandList() {
        when(productRepository.findAll()).thenReturn(List.of(activeP1, activeP2));

        ProductFilter filter = new ProductFilter(null, null, null, null, List.of("Ibanez"), null, null);

        List<ProductResponseDto> result = productService.getProducts(filter);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).brand()).isEqualTo("Ibanez");
    }

    @Test
    void getProducts_customPagination_page1_size1_returnsSecondItem() {
        when(productRepository.findAll()).thenReturn(List.of(activeP1, activeP2));

        ProductFilter filter = new ProductFilter(null, null, null, null, null, 1, 1);

        List<ProductResponseDto> result = productService.getProducts(filter);

        assertThat(result).hasSize(1);
        assertThat(result.get(0).id()).isEqualTo(2L);
    }

    @Test
    void getProducts_pageNumberProvided_butPageSizeNull_usesDefaultPageSize_andDoesNotThrow() {
        when(productRepository.findAll()).thenReturn(List.of(activeP1, activeP2));

        // pageNumber=1, pageSize=null -> darf NICHT crashen (nach Service-Fix)
        ProductFilter filter = new ProductFilter(null, null, null, null, null, 1, null);

        assertThatCode(() -> productService.getProducts(filter))
                .doesNotThrowAnyException();
    }

    @Test
    void getProducts_whenRepositoryEmpty_returnsEmptyList() {
        when(productRepository.findAll()).thenReturn(List.of());

        ProductFilter filter = new ProductFilter(null, null, null, null, null, null, null);

        List<ProductResponseDto> result = productService.getProducts(filter);

        assertThat(result).isEmpty();
    }

    @Test
    void getProducts_whenAllInactive_returnsEmptyList() {
        when(productRepository.findAll()).thenReturn(List.of(inactiveP));

        ProductFilter filter = new ProductFilter(null, null, null, null, null, null, null);

        List<ProductResponseDto> result = productService.getProducts(filter);

        assertThat(result).isEmpty();
    }

    @Test
    void getProducts_whenFilterMatchesNothing_returnsEmptyList() {
        when(productRepository.findAll()).thenReturn(List.of(activeP1, activeP2));

        ProductFilter filter = new ProductFilter("does-not-exist", null, null, null, null, null, null);

        List<ProductResponseDto> result = productService.getProducts(filter);

        assertThat(result).isEmpty();
    }
}
