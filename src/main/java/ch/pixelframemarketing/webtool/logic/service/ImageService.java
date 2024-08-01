package ch.pixelframemarketing.webtool.logic.service;

import ch.pixelframemarketing.webtool.data.entity.ImageMetadata;
import ch.pixelframemarketing.webtool.data.repository.ImageRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ImageService {
    
    @Autowired
    private ImageRepository imageRepository;
    
    public static final String DEFAULT_USER_IMAGE_ID = "default_user_image";

    public static final String IMAGE_UPLOAD_DIRECTORY = System.getProperty("user.home") + "/Pictures/PixelframeMarketing/Webtool/Uploads";

    public File getImage(String id) {
        ImageMetadata imageMetadata = imageRepository.findById(id).orElse(null);
        return imageMetadata != null ? getImageFromDisk(imageMetadata) : null;
    }

    public ImageMetadata uploadImage(MultipartFile file) {
        ImageMetadata imageMetadata = new ImageMetadata();
        imageMetadata.setFileType(extractExtension(file.getOriginalFilename()));
        imageMetadata = imageRepository.save(imageMetadata);
        saveImageToDisk(imageMetadata, file);
        return imageMetadata;
    }
    
    private File getImageFromDisk(ImageMetadata imageMetadata) {
       return new File(IMAGE_UPLOAD_DIRECTORY + "/" + imageMetadata.getFileName());
    }

    private void saveImageToDisk(ImageMetadata imageMetadata, MultipartFile file) {
        try {
            Path folderPath = Paths.get(IMAGE_UPLOAD_DIRECTORY).toAbsolutePath().normalize();
            if (!Files.exists(folderPath)) Files.createDirectories(folderPath);
            Files.copy(file.getInputStream(), folderPath.resolve(imageMetadata.getFileName()), StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
    
    private String extractExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) throw new RuntimeException("Filetype of uploaded image could not be determined.");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

}
