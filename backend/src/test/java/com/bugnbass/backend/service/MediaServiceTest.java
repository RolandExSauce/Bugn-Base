package com.bugnbass.backend.service;

import com.bugnbass.backend.exceptions.ImageNotFoundException;
import com.bugnbass.backend.model.Product;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.io.TempDir;
import org.springframework.core.io.Resource;
import org.springframework.web.multipart.MultipartFile;

import java.io.ByteArrayInputStream;
import java.io.IOException;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.Mockito.*;

class MediaServiceTest {

    @TempDir
    Path tempDir;

    // -------------------- uploadImage --------------------

    @Test
    void uploadImage_savesFile_returnsUrl_andAddsImageWhenProductNotNull() throws Exception {
        ImageService imageService = mock(ImageService.class);
        MediaService mediaService = new MediaService(tempDir.toString(), imageService);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("my image.png");
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("hello".getBytes(StandardCharsets.UTF_8)));

        Product product = mock(Product.class);

        String url = mediaService.uploadImage(file, "guitars", product);

        assertThat(url).startsWith("/media/guitars/");
        assertThat(url).endsWith(".png");

        String fileName = url.replace("/media/guitars/", "");
        Path saved = tempDir.resolve("guitars").resolve(fileName);

        assertThat(Files.exists(saved)).isTrue();
        assertThat(Files.readString(saved)).isEqualTo("hello");

        verify(imageService).addImageToProduct(eq(product), eq(url));
        verifyNoMoreInteractions(imageService);
    }

    @Test
    void uploadImage_savesFile_returnsUrl_andDoesNotCallImageServiceWhenProductNull() throws Exception {
        ImageService imageService = mock(ImageService.class);
        MediaService mediaService = new MediaService(tempDir.toString(), imageService);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("bass.jpeg");
        when(file.getInputStream()).thenReturn(new ByteArrayInputStream("img".getBytes(StandardCharsets.UTF_8)));

        String url = mediaService.uploadImage(file, "basses", null);

        assertThat(url).startsWith("/media/basses/");
        assertThat(url).endsWith(".jpeg");

        verifyNoInteractions(imageService);
    }

    @Test
    void uploadImage_throwsRuntimeException_whenIOExceptionOccurs() throws Exception {
        ImageService imageService = mock(ImageService.class);
        MediaService mediaService = new MediaService(tempDir.toString(), imageService);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn("x.png");
        when(file.getInputStream()).thenThrow(new IOException("io"));

        assertThatThrownBy(() -> mediaService.uploadImage(file, "guitars", null))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("Failed to upload image");

        verifyNoInteractions(imageService);
    }

    @Test
    void uploadImage_throwsNullPointer_whenOriginalFilenameNull() {
        ImageService imageService = mock(ImageService.class);
        MediaService mediaService = new MediaService(tempDir.toString(), imageService);

        MultipartFile file = mock(MultipartFile.class);
        when(file.getOriginalFilename()).thenReturn(null);

        assertThatThrownBy(() -> mediaService.uploadImage(file, "guitars", null))
                .isInstanceOf(NullPointerException.class);

        verifyNoInteractions(imageService);
    }

    // -------------------- getImage --------------------

    @Test
    void getImage_returnsResource_whenFileExistsAndReadable() throws Exception {
        ImageService imageService = mock(ImageService.class);
        MediaService mediaService = new MediaService(tempDir.toString(), imageService);

        Path rel = Path.of("guitars/test.png");
        Path file = tempDir.resolve(rel);
        Files.createDirectories(file.getParent());
        Files.writeString(file, "data", StandardCharsets.UTF_8);

        Resource resource = mediaService.getImage("guitars/test.png");

        assertThat(resource).isNotNull();
        assertThat(resource.exists()).isTrue();
        assertThat(resource.isReadable()).isTrue();
        assertThat(resource.getURI()).isEqualTo(file.toUri());

        verifyNoInteractions(imageService);
    }

    @Test
    void getImage_throwsImageNotFound_whenFileDoesNotExist() {
        ImageService imageService = mock(ImageService.class);
        MediaService mediaService = new MediaService(tempDir.toString(), imageService);

        assertThatThrownBy(() -> mediaService.getImage("does/not/exist.png"))
                .isInstanceOf(ImageNotFoundException.class)
                .hasMessageContaining("Image not found");

        verifyNoInteractions(imageService);
    }

    @Test
    void getImage_throwsImageNotFound_whenPathTraversalAttempt() {
        ImageService imageService = mock(ImageService.class);
        MediaService mediaService = new MediaService(tempDir.toString(), imageService);

        assertThatThrownBy(() -> mediaService.getImage("../secret.txt"))
                .isInstanceOf(ImageNotFoundException.class)
                .hasMessageContaining("Invalid image path");

        verifyNoInteractions(imageService);
    }

    // -------------------- deleteImage --------------------

    @Test
    void deleteImage_deletesFile_ifExists() throws Exception {
        ImageService imageService = mock(ImageService.class);
        MediaService mediaService = new MediaService(tempDir.toString(), imageService);

        Path rel = Path.of("guitars/a.png");
        Path file = tempDir.resolve(rel);
        Files.createDirectories(file.getParent());
        Files.writeString(file, "x", StandardCharsets.UTF_8);

        assertThat(Files.exists(file)).isTrue();

        mediaService.deleteImage("guitars/a.png");

        assertThat(Files.exists(file)).isFalse();
        verifyNoInteractions(imageService);
    }

    @Test
    void deleteImage_doesNothing_ifFileMissing() {
        ImageService imageService = mock(ImageService.class);
        MediaService mediaService = new MediaService(tempDir.toString(), imageService);

        mediaService.deleteImage("guitars/missing.png");

        verifyNoInteractions(imageService);
    }
}
