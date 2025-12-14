package dev.buhanzaz.web.dto.image;

public record DeleteImageRequestDto(
        String bucketName,
        String objectKey
) {
}

