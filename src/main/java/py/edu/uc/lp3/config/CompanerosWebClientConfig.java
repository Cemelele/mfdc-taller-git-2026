package py.edu.uc.lp3.config;

import java.time.Duration;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.http.client.reactive.ReactorClientHttpConnector;
import org.springframework.web.reactive.function.client.WebClient;

import io.netty.channel.ChannelOption;
import reactor.netty.http.client.HttpClient;

@Configuration
public class CompanerosWebClientConfig {

    private static final int CONNECT_TIMEOUT_MS = 3000;
    private static final Duration RESPONSE_TIMEOUT = Duration.ofSeconds(5);

    @Bean
    public WebClient ruthWebClient(WebClient.Builder builder,
                                   @Value("${companeros.a2.base-url}") String baseUrl) {
        return construir(builder, baseUrl);
    }

    @Bean
    public WebClient sebastianWebClient(WebClient.Builder builder,
                                        @Value("${companeros.a3.base-url}") String baseUrl) {
        return construir(builder, baseUrl);
    }

    private WebClient construir(WebClient.Builder builder, String baseUrl) {
        HttpClient httpClient = HttpClient.create()
                .option(ChannelOption.CONNECT_TIMEOUT_MILLIS, CONNECT_TIMEOUT_MS)
                .responseTimeout(RESPONSE_TIMEOUT);

        return builder.clone()
                .baseUrl(baseUrl)
                .clientConnector(new ReactorClientHttpConnector(httpClient))
                .build();
    }
}
