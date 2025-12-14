package dev.buhanzaz.web.dto.image;

import java.util.List;

public record GetImagesBatchRequestDto(
        String bucketName,
        List<String> objectName
) {
}
