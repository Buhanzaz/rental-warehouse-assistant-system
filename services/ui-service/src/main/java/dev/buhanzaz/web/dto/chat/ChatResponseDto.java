package dev.buhanzaz.web.dto.chat;

import dev.buhanzaz.web.enumerated.ChatStatus;

import java.util.List;
import java.util.Map;

public record ChatResponseDto(
        ChatStatus status,
        String message,
        List<String> options,
        Map<String, Integer> normalizedMap,
        String fileUrl
) {
}
