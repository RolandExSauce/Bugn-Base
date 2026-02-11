package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.ProductFilter;
import com.bugnbass.backend.dto.ProductResponseDto;
import com.bugnbass.backend.exceptions.GlobalExceptionHandler;
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

    @Test
    void getProducts_returnsListOfDtos() throws Exception {
        // Arrange: echtes DTO, damit JSON prüfbar ist
        ProductResponseDto dto = new ProductResponseDto(
                10L,
                "Jazz Bass",
                ProductCategory.valueOf(ProductCategory.class.getEnumConstants()[0].name()), // ersetze gern durch FIXEN enum-wert
                "Nice bass",
                999,
                15,
                "Fender",
                StockStatus.valueOf(StockStatus.class.getEnumConstants()[0].name()), // ersetze gern durch FIXEN enum-wert
                3,
                true,
                List.of() // images leer
        );

        when(userProductService.getProducts(any())).thenReturn(List.of(dto));

        // Act + Assert
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

        mockMvc.perform(get("/bugnbass/api/products")
                        .param("name", "bass")
                        .param("category", ProductCategory.class.getEnumConstants()[0].name()) // z.B. "GUITAR"
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

        // WICHTIG: falls ProductFilter ein record ist -> name(), category(), ...
        // falls es Getter hat -> getName(), getCategory(), ...
        //
        // Ich schreibe hier die record-Variante, weil du schon records nutzt:
        assertThat(filter.name()).isEqualTo("bass");
        assertThat(filter.category()).isNotNull();
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
