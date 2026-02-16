package com.bugnbass.backend.service;

import com.bugnbass.backend.model.Image;
import com.bugnbass.backend.model.Product;
import com.bugnbass.backend.repository.ImageRepository;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class ImageServiceTest {

    @Mock
    ImageRepository imageRepository;

    @InjectMocks
    ImageService imageService;

    @Test
    void addImageToProduct_createsImage_setsFields_savesAndReturnsSavedEntity() {
        Product product = mock(Product.class);
        String url = "/media/guitars/123_test.png";

        Image saved = new Image();
        saved.setUrl(url);
        saved.setProduct(product);

        when(imageRepository.save(any(Image.class))).thenReturn(saved);

        Image result = imageService.addImageToProduct(product, url);

        ArgumentCaptor<Image> captor = ArgumentCaptor.forClass(Image.class);
        verify(imageRepository).save(captor.capture());
        verifyNoMoreInteractions(imageRepository);

        Image toSave = captor.getValue();
        assertThat(toSave.getProduct()).isSameAs(product);
        assertThat(toSave.getUrl()).isEqualTo(url);

        assertThat(result).isSameAs(saved);
        assertThat(result.getProduct()).isSameAs(product);
        assertThat(result.getUrl()).isEqualTo(url);
    }

    @Test
    void addImageToProduct_throws_whenRepositoryThrows() {
        Product product = mock(Product.class);
        String url = "/media/guitars/123_test.png";

        when(imageRepository.save(any(Image.class)))
                .thenThrow(new RuntimeException("db down"));

        assertThatThrownBy(() -> imageService.addImageToProduct(product, url))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("db down");

        verify(imageRepository).save(any(Image.class));
        verifyNoMoreInteractions(imageRepository);
    }
}
