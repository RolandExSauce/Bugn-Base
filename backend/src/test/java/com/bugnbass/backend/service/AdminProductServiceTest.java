package com.bugnbass.backend.service;

import com.bugnbass.backend.dto.ProductDto;
import com.bugnbass.backend.exceptions.ProductNotFoundException;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.enums.ProductCategory;
import com.bugnbass.backend.model.enums.StockStatus;
import com.bugnbass.backend.repository.ProductRepository;
import com.bugnbass.backend.validator.ProductValidator;
import java.util.List;
import java.util.Optional;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;

@ExtendWith(MockitoExtension.class)
class AdminProductServiceTest {

    @Mock
    ProductRepository productRepository;

    @Mock
    ProductValidator productValidator;

    @InjectMocks
    AdminProductService adminProductService;

    @Test
    void getProduct_returnsProduct_whenFound() {
        Product p = Product.builder().id(1L).name("P1").build();
        when(productRepository.findById(1L)).thenReturn(Optional.of(p));

        Product result = adminProductService.getProduct("1");

        assertThat(result).isSameAs(p);

        verify(productRepository).findById(1L);
        verifyNoMoreInteractions(productRepository);
        verifyNoMoreInteractions(productValidator);
    }

    @Test
    void getProduct_throwsProductNotFound_whenMissing() {
        when(productRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> adminProductService.getProduct("1"))
                .isInstanceOf(ProductNotFoundException.class);

        verify(productRepository).findById(1L);
        verifyNoMoreInteractions(productRepository);
        verifyNoMoreInteractions(productValidator);
    }

    @Test
    void getProducts_returnsAll() {
        Product p1 = Product.builder().id(1L).build();
        Product p2 = Product.builder().id(2L).build();

        when(productRepository.findAll()).thenReturn(List.of(p1, p2));

        List<Product> result = adminProductService.getProducts();

        assertThat(result).containsExactly(p1, p2);

        verify(productRepository).findAll();
        verifyNoMoreInteractions(productRepository);
        verifyNoMoreInteractions(productValidator);
    }

    @Test
    void addProduct_validates_buildsAndSaves_andReturnsSaved() {
        ProductDto dto = fullDto();

        Product saved = Product.builder().id(99L).name(dto.name()).build();
        when(productRepository.save(any(Product.class))).thenReturn(saved);

        Product result = adminProductService.addProduct(dto);

        assertThat(result).isSameAs(saved);

        verify(productValidator).validateProductData(dto);

        ArgumentCaptor<Product> captor = ArgumentCaptor.forClass(Product.class);
        verify(productRepository).save(captor.capture());

        Product toSave = captor.getValue();

        assertThat(toSave.getName()).isEqualTo(dto.name());
        assertThat(toSave.getCategory()).isEqualTo(dto.category());
        assertThat(toSave.getDescription()).isEqualTo(dto.description());
        assertThat(toSave.getBrand()).isEqualTo(dto.brand());
        assertThat(toSave.getPrice()).isEqualTo(dto.price());
        assertThat(toSave.getShippingCost()).isEqualTo(dto.shippingCost());
        assertThat(toSave.getStockStatus()).isEqualTo(dto.stockStatus());
        assertThat(toSave.getShippingTime()).isEqualTo(dto.shippingTime());
        assertThat(toSave.getActive()).isEqualTo(dto.active());

        verifyNoMoreInteractions(productRepository, productValidator);
    }

    @Test
    void deleteProduct_setsActiveFalse_andSaves() {
        Product existing = Product.builder().id(5L).active(true).build();

        when(productRepository.findById(5L)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        adminProductService.deleteProduct("5");

        assertThat(existing.getActive()).isFalse();

        verify(productRepository).findById(5L);
        verify(productRepository).save(existing);
        verifyNoMoreInteractions(productRepository);
        verifyNoMoreInteractions(productValidator);
    }

    @Test
    void updateProduct_validatesWithId_updatesOnlyNonNullFields_andSaves() {
        Product existing = Product.builder()
                .id(7L)
                .name("Old")
                .category(ProductCategory.PIANOS)
                .description("OldDesc")
                .brand("OldBrand")
                .price(10)
                .shippingCost(1)
                .shippingTime(3)
                .stockStatus(StockStatus.IN_STOCK)
                .active(true)
                .build();

        when(productRepository.findById(7L)).thenReturn(Optional.of(existing));
        when(productRepository.save(any(Product.class))).thenAnswer(inv -> inv.getArgument(0));

        ProductDto dto = new ProductDto(
                "NewName",
                null,
                null,
                99,
                null,
                null,
                null,
                null,
                null
        );

        adminProductService.updateProduct("7", dto);

        verify(productValidator).validateProductData(eq(dto), eq(7L));
        verify(productRepository).findById(7L);
        verify(productRepository).save(existing);

        assertThat(existing.getName()).isEqualTo("NewName");
        assertThat(existing.getPrice()).isEqualTo(99);

        // unchanged
        assertThat(existing.getCategory()).isEqualTo(ProductCategory.PIANOS);
        assertThat(existing.getDescription()).isEqualTo("OldDesc");
        assertThat(existing.getBrand()).isEqualTo("OldBrand");
        assertThat(existing.getShippingCost()).isEqualTo(1);
        assertThat(existing.getShippingTime()).isEqualTo(3);
        assertThat(existing.getStockStatus()).isEqualTo(StockStatus.IN_STOCK);
        assertThat(existing.getActive()).isTrue();

        verifyNoMoreInteractions(productRepository, productValidator);
    }

    private static ProductDto fullDto() {
        return new ProductDto(
                "Grand Piano",
                ProductCategory.PIANOS,
                "High quality piano",
                5000,
                50,
                "Yamaha",
                StockStatus.IN_STOCK,
                3,
                true
        );
    }
}
