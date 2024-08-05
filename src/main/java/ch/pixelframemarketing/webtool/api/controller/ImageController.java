package ch.pixelframemarketing.webtool.api.controller;

import ch.pixelframemarketing.webtool.api.dto.UploadResponseDTO;
import ch.pixelframemarketing.webtool.data.entity.ImageMetadata;
import ch.pixelframemarketing.webtool.api.interceptor.Secure;
import ch.pixelframemarketing.webtool.logic.service.ImageService;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.FileSystemResource;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

import java.io.File;
import java.net.URI;
import java.net.URISyntaxException;
import java.net.URLConnection;

@RestController
@RequestMapping("/api/img")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping(consumes = "multipart/form-data")
    @Secure
    public ResponseEntity<UploadResponseDTO> createImage(@RequestParam("upload") MultipartFile upload) {
        ImageMetadata imageMetadata = imageService.uploadImage(upload);
        String url = ServletUriComponentsBuilder.fromCurrentContextPath().build().toUriString() + ("/api/img/" + imageMetadata.getId());
        return ResponseEntity.created(URI.create(url)).body(new UploadResponseDTO(imageMetadata.getId(), url));
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    @Secure
    public ResponseEntity<ImageMetadata> updateImage(@PathVariable(value = "id") String id, @RequestParam("upload") MultipartFile upload) {
        // TODO: update image instead of uploading new one
        ImageMetadata imageMetadata = imageService.uploadImage(upload);
        return ResponseEntity.created(URI.create("/api/img/" + imageMetadata.getId())).body(imageMetadata);
    }

    @GetMapping(value = "/{id}")
    // this MUST NOT have a secure tag, as the <img> tags do not send the bearer string (login token)
    public ResponseEntity<Object> getImage(@PathVariable(value = "id") String id) throws URISyntaxException {
        ImageMetadata image = imageService.getImage(id);
        if (image == null) {
            return ResponseEntity.notFound().build();
        }

        HttpHeaders httpHeaders = new HttpHeaders();
        httpHeaders.setLocation(new URI(image.getDropboxShareLink()));
        return new ResponseEntity<>(httpHeaders, HttpStatus.TEMPORARY_REDIRECT);
    }
}
