package ch.pixelframemarketing.webtool.general.config;

import ch.pixelframemarketing.webtool.api.interceptor.SecurityInterceptor;
import com.dropbox.core.DbxRequestConfig;
import com.dropbox.core.v2.DbxClientV2;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.servlet.config.annotation.CorsRegistry;
import org.springframework.web.servlet.config.annotation.InterceptorRegistry;
import org.springframework.web.servlet.config.annotation.WebMvcConfigurer;

@Configuration
public class WebConfig implements WebMvcConfigurer {

    @Autowired
    private SecurityInterceptor securityInterceptor;

    @Override
    public void addInterceptors(InterceptorRegistry registry) {
        registry.addInterceptor(securityInterceptor);
    }

    @Override
    public void addCorsMappings(CorsRegistry registry) {
        registry.addMapping("/**").allowedMethods("HEAD", "GET", "POST", "PUT", "DELETE", "PATCH", "OPTIONS");
    }
    
    @Bean
    public DbxClientV2 dropboxClient() {
        String identifier = System.getenv("DROPBOX_IDENTIFIER");
        if (identifier == null || identifier.isEmpty()) {
            throw new IllegalArgumentException("DROPBOX_IDENTIFIER is not set in environment variables.");
        }
        
        String token = System.getenv("DROPBOX_TOKEN");
        if (token == null || token.isEmpty()) {
            throw new IllegalArgumentException("DROPBOX_TOKEN is not set in environment variables.");
        }

        // Create a Dropbox client with the access token
        DbxRequestConfig config = DbxRequestConfig.newBuilder(identifier).build();
        return new DbxClientV2(config, token);
    }
}