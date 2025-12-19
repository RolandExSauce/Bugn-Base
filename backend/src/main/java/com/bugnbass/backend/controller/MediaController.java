package com.bugnbass.backend.controller;

import com.bugnbass.backend.service.MediaService;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/media")
public class MediaController {

  private final MediaService mediaService;

  public MediaController(MediaService mediaService) {
    this.mediaService = mediaService;
  }

  @GetMapping("/{folder}/{filename:.+}")
  public ResponseEntity<Resource> getImage(
      @PathVariable String folder,
      @PathVariable String filename
  ) {
    Resource image = mediaService.getImage(folder + "/" + filename);

    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(image);
  }
}
