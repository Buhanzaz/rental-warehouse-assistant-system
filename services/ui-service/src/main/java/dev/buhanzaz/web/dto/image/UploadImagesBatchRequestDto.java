package dev.buhanzaz.web.dto.image;

public record UploadImagesBatchRequestDto(
        String firstName,
        String lastName,
        String position,
        String bucketName,
        String warehouseFolder,
        String cabinId,
        String folderName,
        String rentalReturnDate
) {
}
