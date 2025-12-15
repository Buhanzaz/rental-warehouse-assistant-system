package dev.buhanzaz.web.config;

import dev.buhanzaz.web.client.EstimateDraftClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.web.client.RestClient;
import org.springframework.web.client.support.RestClientAdapter;
import org.springframework.web.service.invoker.HttpServiceProxyFactory;

@Configuration
public class EstimateDraftClientConfig {

    @Bean
    public EstimateDraftClient estimateDraftClient() {
        RestClient restClient = RestClient.builder()
                .baseUrl("http://localhost:8082") // URL сервиса с LLM
                .build();

        HttpServiceProxyFactory factory = HttpServiceProxyFactory
                .builderFor(RestClientAdapter.create(restClient))
                .build();

        return factory.createClient(EstimateDraftClient.class);
    }
}
