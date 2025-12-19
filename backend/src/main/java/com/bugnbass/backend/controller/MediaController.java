package com.bugnbass.backend.controller;

import com.bugnbass.backend.service.MediaService;
import jakarta.servlet.http.HttpServletRequest;
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

  @GetMapping("/**")
  public ResponseEntity<Resource> getImage(HttpServletRequest request) {
    String path = request.getRequestURI().replaceFirst(".*/media/", "");
    Resource image = mediaService.getImage(path);
    return ResponseEntity.ok()
        .contentType(MediaType.APPLICATION_OCTET_STREAM)
        .body(image);
  }

}
