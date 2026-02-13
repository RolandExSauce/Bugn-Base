package com.bugnbass.backend.validator;

import com.bugnbass.backend.dto.ProductDto;
import com.bugnbass.backend.exceptions.ProductNameAlreadyExistsException;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.repository.ProductRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.Optional;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ProductValidatorTest {

    @Mock
    ProductRepository productRepository;

    @InjectMocks
    ProductValidator validator;

    @Test
    void validateProductData_doesNothing_whenNameIsUnique() {
        ProductDto dto = mock(ProductDto.class);
        when(dto.name()).thenReturn("Guitar");

        when(productRepository.findByName("Guitar"))
                .thenReturn(Optional.empty());

        assertThatCode(() ->
                validator.validateProductData(dto))
                .doesNotThrowAnyException();

        verify(productRepository).findByName("Guitar");
    }

    @Test
    void validateProductData_throws_whenNameAlreadyExists() {
        ProductDto dto = mock(ProductDto.class);
        when(dto.name()).thenReturn("Guitar");

        Product existing = new Product();
        existing.setId(1L);

        when(productRepository.findByName("Guitar"))
                .thenReturn(Optional.of(existing));

        assertThatThrownBy(() ->
                validator.validateProductData(dto))
                .isInstanceOf(ProductNameAlreadyExistsException.class);

        verify(productRepository).findByName("Guitar");
    }

    @Test
    void validateProductData_allowsSameProductId_whenUpdating() {
        ProductDto dto = mock(ProductDto.class);
        when(dto.name()).thenReturn("Guitar");

        Product existing = new Product();
        existing.setId(5L);

        when(productRepository.findByName("Guitar"))
                .thenReturn(Optional.of(existing));

        assertThatCode(() ->
                validator.validateProductData(dto, 5L))
                .doesNotThrowAnyException();

        verify(productRepository).findByName("Guitar");
    }

    @Test
    void validateProductData_doesNothing_whenNameIsNull() {
        ProductDto dto = mock(ProductDto.class);
        when(dto.name()).thenReturn(null);

        validator.validateProductData(dto);

        verifyNoInteractions(productRepository);
    }

    @Test
    void validateProductData_throws_whenUpdatingButDifferentProductHasSameName() {
        ProductDto dto = mock(ProductDto.class);
        when(dto.name()).thenReturn("Guitar");

        Product existing = new Product();
        existing.setId(5L);

        when(productRepository.findByName("Guitar"))
                .thenReturn(Optional.of(existing));

        assertThatThrownBy(() -> validator.validateProductData(dto, 6L))
                .isInstanceOf(ProductNameAlreadyExistsException.class);

        verify(productRepository).findByName("Guitar");
    }

}

