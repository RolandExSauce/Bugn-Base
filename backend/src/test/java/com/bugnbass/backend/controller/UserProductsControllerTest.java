package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.ProductFilter;
import com.bugnbass.backend.dto.ProductResponseDto;
import com.bugnbass.backend.exceptions.GlobalExceptionHandler;
import com.bugnbass.backend.exceptions.ProductNotFoundException;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.enums.ProductCategory;
import com.bugnbass.backend.model.enums.StockStatus;
import com.bugnbass.backend.security.AuthTokenFilter;
import com.bugnbass.backend.service.ProductService;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = UserProductsController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = AuthTokenFilter.class
        )
)
@AutoConfigureMockMvc(addFilters = false)
@Import(GlobalExceptionHandler.class)
class UserProductsControllerTest {

    @Autowired
    MockMvc mockMvc;

    @MockBean
    ProductService userProductService;

    // ---------- GET /bugnbass/api/products/{id} ----------

    @Test
    void getProduct_validId_returns200_andCallsService() throws Exception {
        Product product = mock(Product.class);
        when(userProductService.getProduct(10L)).thenReturn(product);

        mockMvc.perform(get("/bugnbass/api/products/{id}", 10))
                .andExpect(status().isOk());

        verify(userProductService).getProduct(10L);
        verifyNoMoreInteractions(userProductService);
    }

    @Test
    void getProduct_notFound_returns404_andCallsService() throws Exception {
        when(userProductService.getProduct(999L))
                .thenThrow(new ProductNotFoundException("Product not found"));

        mockMvc.perform(get("/bugnbass/api/products/{id}", 999))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Product not found"));

        verify(userProductService).getProduct(999L);
        verifyNoMoreInteractions(userProductService);
    }

    // ---------- GET /bugnbass/api/products ----------

    @Test
    void getProducts_returnsListOfDtos() throws Exception {
        ProductCategory cat = ProductCategory.class.getEnumConstants()[0];
        StockStatus stock = StockStatus.class.getEnumConstants()[0];

        ProductResponseDto dto = new ProductResponseDto(
                10L,
                "Jazz Bass",
                cat,
                "Nice bass",
                999,
                15,
                "Fender",
                stock,
                3,
                true,
                List.of(),
                4.5,
                12L
        );

        when(userProductService.getProducts(any())).thenReturn(List.of(dto));

        mockMvc.perform(get("/bugnbass/api/products"))
                .andExpect(status().isOk())
                .andExpect(content().contentTypeCompatibleWith(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1))
                .andExpect(jsonPath("$[0].id").value(10))
                .andExpect(jsonPath("$[0].name").value("Jazz Bass"))
                .andExpect(jsonPath("$[0].brand").value("Fender"))
                .andExpect(jsonPath("$[0].price").value(999))
                .andExpect(jsonPath("$[0].shippingCost").value(15))
                .andExpect(jsonPath("$[0].images").isArray())
                .andExpect(jsonPath("$[0].images.length()").value(0));

        verify(userProductService).getProducts(any(ProductFilter.class));
        verifyNoMoreInteractions(userProductService);
    }

    @Test
    void getProducts_withParams_mapsToFilterCorrectly() throws Exception {
        when(userProductService.getProducts(any())).thenReturn(List.of());

        String category = ProductCategory.class.getEnumConstants()[0].name();

        mockMvc.perform(get("/bugnbass/api/products")
                        .param("name", "bass")
                        .param("category", category)
                        .param("priceMin", "10")
                        .param("priceMax", "100")
                        .param("brand", "Fender")
                        .param("brand", "Ibanez")
                        .param("pageNo", "2")
                        .param("pageSize", "20"))
                .andExpect(status().isOk());

        ArgumentCaptor<ProductFilter> captor = ArgumentCaptor.forClass(ProductFilter.class);
        verify(userProductService).getProducts(captor.capture());

        ProductFilter filter = captor.getValue();
        assertThat(filter.name()).isEqualTo("bass");
        assertThat(filter.category().name()).isEqualTo(category);
        assertThat(filter.priceMin()).isEqualTo(10);
        assertThat(filter.priceMax()).isEqualTo(100);
        assertThat(filter.brand()).containsExactly("Fender", "Ibanez");
        assertThat(filter.pageNumber()).isEqualTo(2);
        assertThat(filter.pageSize()).isEqualTo(20);

        verifyNoMoreInteractions(userProductService);
    }

    @Test
    void getProducts_invalidCategory_returns400_andDoesNotCallService() throws Exception {
        mockMvc.perform(get("/bugnbass/api/products")
                        .param("category", "NOT_A_REAL_CATEGORY"))
                .andExpect(status().isBadRequest());

        verifyNoInteractions(userProductService);
    }
}
