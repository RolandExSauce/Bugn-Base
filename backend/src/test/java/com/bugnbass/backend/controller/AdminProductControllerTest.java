package com.bugnbass.backend.controller;

import com.bugnbass.backend.dto.ProductDto;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.enums.ProductCategory;
import com.bugnbass.backend.model.enums.StockStatus;
import com.bugnbass.backend.security.AuthTokenFilter;
import com.bugnbass.backend.service.AdminOrderService;
import com.bugnbass.backend.service.AdminProductService;
import com.bugnbass.backend.service.MediaService;
import com.fasterxml.jackson.databind.ObjectMapper;
import java.util.List;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.http.MediaType;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.verifyNoInteractions;
import static org.mockito.Mockito.verifyNoMoreInteractions;
import static org.mockito.Mockito.when;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(
        controllers = AdminProductController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = AuthTokenFilter.class
        )
)
@AutoConfigureMockMvc
@Import(AdminProductControllerTest.MethodSecurityTestConfig.class)
class AdminProductControllerTest {

    @TestConfiguration
    @EnableMethodSecurity(prePostEnabled = true)
    static class MethodSecurityTestConfig {}

    @Autowired MockMvc mockMvc;
    @Autowired ObjectMapper objectMapper;

    @MockBean AdminProductService adminProductService;
    @MockBean MediaService mediaService;
    @MockBean
    AdminOrderService adminOrderService;

    // ---------- GET SINGLE PRODUCT ----------

    @Test
    @WithMockUser(roles = "ADMIN")
    void getProduct_withAdmin_returns200_andJson() throws Exception {
        Product p = validProduct();
        when(adminProductService.getProduct("1")).thenReturn(p);

        mockMvc.perform(get("/bugnbass/api/admin/product/{id}", "1"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.id").value(p.getId()))
                .andExpect(jsonPath("$.name").value(p.getName()))
                .andExpect(jsonPath("$.category").value(p.getCategory().name()))
                .andExpect(jsonPath("$.price").value(p.getPrice()))
                .andExpect(jsonPath("$.shippingCost").value(p.getShippingCost()))
                .andExpect(jsonPath("$.brand").value(p.getBrand()))
                .andExpect(jsonPath("$.stockStatus").value(p.getStockStatus().name()))
                .andExpect(jsonPath("$.shippingTime").value(p.getShippingTime()))
                .andExpect(jsonPath("$.active").value(p.getActive()));

        verify(adminProductService).getProduct("1");
        verifyNoMoreInteractions(adminProductService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getProduct_withUserRole_returns403() throws Exception {
        mockMvc.perform(get("/bugnbass/api/admin/product/{id}", "1"))
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminProductService);
    }

    // ---------- GET ALL PRODUCTS ----------

    @Test
    @WithMockUser(roles = "ADMIN")
    void getProducts_withAdmin_returns200_andArray() throws Exception {
        when(adminProductService.getProducts()).thenReturn(List.of(validProduct()));

        mockMvc.perform(get("/bugnbass/api/admin/products"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$").isArray())
                .andExpect(jsonPath("$.length()").value(1));

        verify(adminProductService).getProducts();
        verifyNoMoreInteractions(adminProductService);
    }

    // ---------- ADD PRODUCT ----------

    @Test
    @WithMockUser(roles = "ADMIN")
    void addProduct_withAdmin_returns201_andPassesDto() throws Exception {
        ProductDto dto = validProductDto();
        Product saved = validProduct();

        when(adminProductService.addProduct(any(ProductDto.class))).thenReturn(saved);

        mockMvc.perform(post("/bugnbass/api/admin/add-product")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.name").value(saved.getName()))
                .andExpect(jsonPath("$.category").value(saved.getCategory().name()));

        ArgumentCaptor<ProductDto> captor = ArgumentCaptor.forClass(ProductDto.class);
        verify(adminProductService).addProduct(captor.capture());

        assertThat(captor.getValue().name()).isEqualTo(dto.name());
        assertThat(captor.getValue().category()).isEqualTo(dto.category());

        verifyNoMoreInteractions(adminProductService);
    }

    // ---------- DELETE PRODUCT ----------

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteProduct_withAdmin_returns204_andCallsService() throws Exception {
        mockMvc.perform(delete("/bugnbass/api/admin/delete-product/{id}", "5")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(adminProductService).deleteProduct("5");
        verifyNoMoreInteractions(adminProductService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteProduct_withUserRole_returns403() throws Exception {
        mockMvc.perform(delete("/bugnbass/api/admin/delete-product/{id}", "5")
                        .with(csrf()))
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminProductService);
    }

    // ---------- UPDATE PRODUCT ----------

    @Test
    @WithMockUser(roles = "ADMIN")
    void updateProduct_withAdmin_returns200_andPassesArgs() throws Exception {
        ProductDto dto = validProductDto();

        mockMvc.perform(put("/bugnbass/api/admin/update-product/{id}", "9")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(dto)))
                .andExpect(status().isOk());

        ArgumentCaptor<ProductDto> captor = ArgumentCaptor.forClass(ProductDto.class);
        verify(adminProductService).updateProduct(eq("9"), captor.capture());

        assertThat(captor.getValue().name()).isEqualTo(dto.name());
        assertThat(captor.getValue().category()).isEqualTo(dto.category());

        verifyNoMoreInteractions(adminProductService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void updateProduct_withUserRole_returns403() throws Exception {
        mockMvc.perform(put("/bugnbass/api/admin/update-product/{id}", "9")
                        .with(csrf())
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(objectMapper.writeValueAsString(validProductDto())))
                .andExpect(status().isForbidden());

        verifyNoInteractions(adminProductService);
    }

    // -------------------- HELPERS --------------------

    private static Product validProduct() {
        return Product.builder()
                .id(1L)
                .name("Fender Stratocaster")
                .category(ProductCategory.GUITARS)
                .description("Legendäre Gitarre")
                .price(1200)
                .shippingCost(20)
                .brand("Fender")
                .stockStatus(StockStatus.IN_STOCK)
                .shippingTime(3)
                .active(true)
                .build();
    }

    private static ProductDto validProductDto() {
        return new ProductDto(
                "Fender Stratocaster",
                ProductCategory.GUITARS,
                "Legendäre Gitarre",
                1200,
                20,
                "Fender",
                StockStatus.IN_STOCK,
                3,
                true
        );
    }
}
