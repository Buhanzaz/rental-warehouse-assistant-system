package dev.buhanzaz.web.dto.image;

public record GetImageRequestDto(
        String bucketName,
        String objectKey
) {
}
