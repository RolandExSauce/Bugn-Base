package com.bugnbass.backend.controller;

import com.bugnbass.backend.exceptions.GlobalExceptionHandler;
import com.bugnbass.backend.exceptions.ImageNotFoundException;
import com.bugnbass.backend.exceptions.MediaAccessException;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.model.enums.ProductCategory;
import com.bugnbass.backend.security.AuthTokenFilter;
import com.bugnbass.backend.service.MediaService;
import com.bugnbass.backend.service.ProductService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.ComponentScan;
import org.springframework.context.annotation.FilterType;
import org.springframework.context.annotation.Import;
import org.springframework.core.io.ByteArrayResource;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.web.servlet.MockMvc;

import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;
import static org.springframework.security.test.web.servlet.request.SecurityMockMvcRequestPostProcessors.csrf;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@WebMvcTest(
        controllers = MediaController.class,
        excludeFilters = @ComponentScan.Filter(
                type = FilterType.ASSIGNABLE_TYPE,
                classes = AuthTokenFilter.class
        )
)
@Import({GlobalExceptionHandler.class, MediaControllerTest.MethodSecurityTestConfig.class})
class MediaControllerTest {

    @TestConfiguration
    @EnableMethodSecurity
    static class MethodSecurityTestConfig {}

    @Autowired
    MockMvc mockMvc;

    @MockBean
    MediaService mediaService;

    @MockBean
    ProductService productService;

    // ---------- GET /bugnbass/api/media/** ----------

    @Test
    @WithMockUser(roles = "USER") // sonst 401
    void getImage_returns200_octetStream_andCallsServiceWithStrippedPath() throws Exception {
        Resource res = new ByteArrayResource("img".getBytes());
        when(mediaService.getImage("guitars/test.png")).thenReturn(res);

        mockMvc.perform(get("/bugnbass/api/media/guitars/test.png"))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_OCTET_STREAM));

        verify(mediaService).getImage("guitars/test.png");
        verifyNoInteractions(productService);
        verifyNoMoreInteractions(mediaService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void getImage_imageNotFound_returns404_andCallsService() throws Exception {
        when(mediaService.getImage("guitars/missing.png"))
                .thenThrow(new ImageNotFoundException("Image not found: guitars/missing.png"));

        mockMvc.perform(get("/bugnbass/api/media/guitars/missing.png"))
                .andExpect(status().isNotFound())
                .andExpect(content().string("Image not found: guitars/missing.png"));

        verify(mediaService).getImage("guitars/missing.png");
        verifyNoInteractions(productService);
        verifyNoMoreInteractions(mediaService);
    }

    // ❌ negativ 2: technischer Fehler -> 500
    @Test
    @WithMockUser(roles = "USER")
    void getImage_mediaAccessError_returns500_andCallsService() throws Exception {
        when(mediaService.getImage("guitars/test.png"))
                .thenThrow(new MediaAccessException("Invalid image path", new RuntimeException("cause")));

        mockMvc.perform(get("/bugnbass/api/media/guitars/test.png"))
                .andExpect(status().isInternalServerError())
                // falls du im Handler "Media access error" zurückgibst:
                .andExpect(content().string("Media access error"));

        verify(mediaService).getImage("guitars/test.png");
        verifyNoInteractions(productService);
        verifyNoMoreInteractions(mediaService);
    }


    // ---------- POST /bugnbass/api/media/file/{productId} ----------

    @Test
    @WithMockUser(roles = "ADMIN")
    void uploadProductImage_withAdmin_returns200_andUrl() throws Exception {
        Product product = mock(Product.class);
        ProductCategory cat = ProductCategory.class.getEnumConstants()[0];
        when(product.getCategory()).thenReturn(cat);
        when(productService.getProduct(5L)).thenReturn(product);

        MockMultipartFile file = new MockMultipartFile(
                "file", "test.png", "image/png", "abc".getBytes()
        );

        when(mediaService.uploadImage(eq(file), eq(cat.name().toLowerCase()), eq(product)))
                .thenReturn("http://localhost/media/somewhere/test.png");

        mockMvc.perform(multipart("/bugnbass/api/media/file/{productId}", 5)
                        .file(file)
                        .with(csrf()))
                .andExpect(status().isOk())
                .andExpect(content().string("http://localhost/media/somewhere/test.png"));

        verify(productService).getProduct(5L);
        verify(mediaService).uploadImage(eq(file), eq(cat.name().toLowerCase()), eq(product));
        verifyNoMoreInteractions(productService, mediaService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void uploadProductImage_withUser_returns403_andDoesNotCallServices() throws Exception {
        MockMultipartFile file = new MockMultipartFile(
                "file", "test.png", "image/png", "abc".getBytes()
        );

        mockMvc.perform(multipart("/bugnbass/api/media/file/{productId}", 5)
                        .file(file)
                        .with(csrf()))
                .andExpect(status().isForbidden());

        verifyNoInteractions(productService, mediaService);
    }

    // ---------- DELETE /bugnbass/api/media/file/delete/{folder}/{filename} ----------

    @Test
    @WithMockUser(roles = "ADMIN")
    void deleteImage_withAdmin_returns204_andCallsService() throws Exception {
        mockMvc.perform(delete("/bugnbass/api/media/file/delete/{folder}/{filename}", "guitars", "a.png")
                        .with(csrf()))
                .andExpect(status().isNoContent());

        verify(mediaService).deleteImage("guitars/a.png");
        verifyNoMoreInteractions(mediaService);
        verifyNoInteractions(productService);
    }

    @Test
    @WithMockUser(roles = "USER")
    void deleteImage_withUser_returns403_andDoesNotCallService() throws Exception {
        mockMvc.perform(delete("/bugnbass/api/media/file/delete/{folder}/{filename}", "guitars", "a.png")
                        .with(csrf()))
                .andExpect(status().isForbidden());

        verifyNoInteractions(mediaService, productService);
    }
}
