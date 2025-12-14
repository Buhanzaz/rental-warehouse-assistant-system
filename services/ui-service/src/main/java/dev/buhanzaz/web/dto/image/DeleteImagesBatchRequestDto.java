package dev.buhanzaz.web.dto.image;

import java.util.List;

public record DeleteImagesBatchRequestDto(
        String bucketName,
        List<String> objectKey
) {
}
