package ch.pixelframemarketing.webtool.api.dto;

import lombok.AllArgsConstructor;
import lombok.NoArgsConstructor;

@AllArgsConstructor
@NoArgsConstructor
public class UploadResponseDTO {
    
    public String id;
    public String url; // only necessary for CKEditor, custom code work out the url from the id
    
}
