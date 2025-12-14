package dev.buhanzaz.web.dto.image;

public record GetFolderImagesRequestDto(
        String bucketName,
        String warehouseFolder,
        String cabinId,
        String folderName,
        String rentalReturnDate
) {
}
