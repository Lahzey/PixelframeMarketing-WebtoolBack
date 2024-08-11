package ch.pixelframemarketing.webtool.logic.service;

import ch.pixelframemarketing.webtool.data.entity.ImageMetadata;
import ch.pixelframemarketing.webtool.data.repository.ImageRepository;
import ch.pixelframemarketing.webtool.general.exception.ValidationException;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.oauth.DbxCredential;
import com.dropbox.core.v2.DbxAppClientV2;
import com.dropbox.core.v2.DbxClientV2;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.InputStream;
import java.text.CharacterIterator;
import java.text.StringCharacterIterator;
import java.util.UUID;

@Service
@Slf4j
@Transactional(rollbackOn = Exception.class)
@RequiredArgsConstructor
public class ImageService {
    
    @Autowired
    private UserService userService;
    
    @Autowired
    private ImageRepository imageRepository;
    
    @Autowired
    private DbxClientV2 dropboxClient;
    
    public static final String DEFAULT_USER_IMAGE_ID = "default_user_image";
    
    private static final long MAX_IMAGE_FILE_SIZE = 5000000;
    
    
    public ImageMetadata getImage(String id) {
        return imageRepository.findById(id).orElse(null);
    }

    public ImageMetadata uploadImage(MultipartFile file) {
        ImageMetadata imageMetadata = new ImageMetadata();
        imageMetadata.setId(UUID.randomUUID().toString());
        imageMetadata.setOwner(userService.getCurrentUser());
        imageMetadata.setFileType(extractExtension(file.getOriginalFilename()));
        imageMetadata.setDropboxShareLink(uploadImageToDropbox(imageMetadata, file));
        imageMetadata = imageRepository.save(imageMetadata);
        return imageMetadata;
    }

    private String uploadImageToDropbox(ImageMetadata imageMetadata, MultipartFile file) {
        if (imageMetadata.getOwner() == null) throw new RuntimeException("You must be logged in to upload an image.");
        if (file.getSize() > MAX_IMAGE_FILE_SIZE) throw new ValidationException("image", "Image too large (max " + humanReadableByteCountSI(MAX_IMAGE_FILE_SIZE) + ").");

        try (InputStream inputStream = file.getInputStream()) {
            String path = "/" + imageMetadata.getOwner().getId() + "/" + imageMetadata.getFileName();
            dropboxClient.files().uploadBuilder(path).uploadAndFinish(inputStream);
            String link = dropboxClient.sharing().createSharedLinkWithSettings(path).getUrl();
            return link.substring(0, link.lastIndexOf("dl=0")) + "raw=1";
        } catch (Exception e) {
            throw new RuntimeException("Failed to upload image.", e);
        }
    }
    
    private String extractExtension(String fileName) {
        if (fileName == null || !fileName.contains(".")) throw new RuntimeException("Filetype of uploaded image could not be determined.");
        return fileName.substring(fileName.lastIndexOf(".") + 1);
    }

    public static String humanReadableByteCountSI(long bytes) {
        if (-1000 < bytes && bytes < 1000) {
            return bytes + " B";
        }
        CharacterIterator ci = new StringCharacterIterator("kMGTPE");
        while (bytes <= -999_950 || bytes >= 999_950) {
            bytes /= 1000;
            ci.next();
        }
        return String.format("%.1f %cB", bytes / 1000.0, ci.current());
    }

}
