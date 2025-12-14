package dev.buhanzaz.web.config;

import dev.buhanzaz.web.service.ImageStorageClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class ImageStorageClientConfig {

    @Bean
    public ImageStorageClient llmService() {
        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:8083") // URL сервиса с LLM
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(ImageStorageClient.class);
    }
}
