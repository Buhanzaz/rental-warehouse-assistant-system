package dev.buhanzaz.web.dto.image;

public record DeleteFolderRequestDto(
        String bucketName,
        String warehouseFolder,
        String cabinId,
        String folderName,
        String rentalReturnDate
) {
}
