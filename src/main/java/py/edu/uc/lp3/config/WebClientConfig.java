package py.edu.uc.lp3.config;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.reactive.function.client.WebClient;

@Configuration
public class WebClientConfig {

    @Bean
    public WebClient apiExternaWebClient(WebClient.Builder builder,
                                         @Value("${api.externa.base-url}") String baseUrl) {
        return builder.baseUrl(baseUrl).build();
    }
}
