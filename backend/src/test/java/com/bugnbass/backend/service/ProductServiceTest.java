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
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@MockitoSettings(strictness = Strictness.LENIENT)
@ExtendWith(MockitoExtension.class)
class ProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductService productService;

    private Product activeP1;
    private Product activeP2;
    private Product inactiveP;

    private ProductCategory category; // <-- fix: echte Variable, kein Mock-call

    @BeforeEach
    void setUp() {
        // nimm irgendeinen Enum-Wert (oder fix, z.B. ProductCategory.BASS)
        category = ProductCategory.class.getEnumConstants()[0];

        activeP1 = mock(Product.class);
        when(activeP1.getActive()).thenReturn(true);
        when(activeP1.getId()).thenReturn(1L);
        when(activeP1.getName()).thenReturn("Jazz Bass");
        when(activeP1.getBrand()).thenReturn("Fender");
        when(activeP1.getPrice()).thenReturn(999);
        when(activeP1.getCategory()).thenReturn(category);
        when(activeP1.getDescription()).thenReturn("desc");
        when(activeP1.getShippingCost()).thenReturn(10);
        when(activeP1.getStockStatus()).thenReturn(null);
        when(activeP1.getShippingTime()).thenReturn(3);
        when(activeP1.getImages()).thenReturn(List.of()); // wichtig für fromEntity()

        activeP2 = mock(Product.class);
        when(activeP2.getActive()).thenReturn(true);
        when(activeP2.getId()).thenReturn(2L);
        when(activeP2.getName()).thenReturn("Precision Bass");
        when(activeP2.getBrand()).thenReturn("Ibanez");
        when(activeP2.getPrice()).thenReturn(499);
        when(activeP2.getCategory()).thenReturn(category); // <-- fix
        when(activeP2.getDescription()).thenReturn("desc");
        when(activeP2.getShippingCost()).thenReturn(10);
        when(activeP2.getStockStatus()).thenReturn(null);
        when(activeP2.getShippingTime()).thenReturn(3);
        when(activeP2.getImages()).thenReturn(List.of()); // wichtig

        inactiveP = mock(Product.class);
        when(inactiveP.getActive()).thenReturn(false);
        when(inactiveP.getId()).thenReturn(3L);
        when(inactiveP.getImages()).thenReturn(List.of());
    }

    @Test
    void getProduct_returnsProductWhenFoundAndActive() {
        when(productRepository.findByIdAndActiveTrue(1L)).thenReturn(Optional.of(activeP1));

        Product result = productService.getProduct(1L);

        assertThat(result).isSameAs(activeP1);
        verify(productRepository).findByIdAndActiveTrue(1L);
    }

    @Test
    void getProduct_throwsWhenNotFound() {
        when(productRepository.findByIdAndActiveTrue(99L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> productService.getProduct(99L))
                .isInstanceOf(ProductNotFoundException.class);

        verify(productRepository).findByIdAndActiveTrue(99L);
    }

    @Test
    void getProducts_filtersOutInactiveAlways() {
        when(productRepository.findAll()).thenReturn(List.of(activeP1, inactiveP, activeP2));

        ProductFilter filter = new ProductFilter(null, null, null, null, null, null, null);

        List<ProductResponseDto> result = productService.getProducts(filter);

        assertThat(result).hasSize(2);
        assertThat(result).extracting(ProductResponseDto::id).containsExactly(1L, 2L);
        verify(productRepository).findAll();
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
