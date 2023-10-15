package link.go2.urlshortener.config;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Configuration;

@Configuration
@Getter
public class AppConfig {
    @Value(value = "${go2link.base_url}")
    private String baseUrl;
    @Value(value = "${go2link.url_length}")
    private int urlLength;
}
