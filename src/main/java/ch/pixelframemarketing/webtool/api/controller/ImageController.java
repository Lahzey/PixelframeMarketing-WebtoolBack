package ch.pixelframemarketing.webtool.api.controller;

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

import java.io.File;
import java.net.URI;
import java.net.URLConnection;

@RestController
@RequestMapping("/api/img")
@RequiredArgsConstructor
public class ImageController {
    private final ImageService imageService;

    @PostMapping(consumes = "multipart/form-data")
    @Secure
    public ResponseEntity<ImageMetadata> createImage(@RequestParam("file") MultipartFile file) {
        ImageMetadata imageMetadata = imageService.uploadImage(file);
        return ResponseEntity.created(URI.create("/img/" + imageMetadata.getId())).body(imageMetadata);
    }

    @PutMapping(value = "/{id}", consumes = "multipart/form-data")
    @Secure
    public ResponseEntity<ImageMetadata> updateImage(@PathVariable(value = "id") String id, @RequestParam("file") MultipartFile file) {
        // TODO: update image instead of uploading new one
        ImageMetadata imageMetadata = imageService.uploadImage(file);
        return ResponseEntity.created(URI.create("/img/" + imageMetadata.getId())).body(imageMetadata);
    }

    @GetMapping(value = "/{id}")
    @Secure
    public ResponseEntity<Resource> getImage(@PathVariable(value = "id") String id) {
        File imageFile = imageService.getImage(id);
        if (imageFile == null || !imageFile.exists()) {
            return ResponseEntity.notFound().build();
        }

        Resource resource = new FileSystemResource(imageFile);
        String mimeType = URLConnection.guessContentTypeFromName(imageFile.getName());
        if (mimeType == null) {
            mimeType = "application/octet-stream"; // Instead of throwing an error we return the malformed file as there is no appropriate http code
        }

        HttpHeaders headers = new HttpHeaders();
        headers.setContentType(MediaType.parseMediaType(mimeType));

        return new ResponseEntity<>(resource, headers, HttpStatus.OK);
    }
}
