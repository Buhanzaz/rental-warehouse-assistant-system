package dev.buhanzaz.web.client;

import dev.buhanzaz.web.dto.chat.ChatRequestDto;
import dev.buhanzaz.web.dto.chat.ChatResponseDto;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.service.annotation.HttpExchange;
import org.springframework.web.service.annotation.PostExchange;

@HttpExchange("/api/v1/llm") // базовый префикс пути
public interface LLMClient {

    @PostExchange("/estimate/create")
    ChatResponseDto generateEstimate(@RequestBody ChatRequestDto request);
}
