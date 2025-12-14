package dev.buhanzaz.web.service;

import dev.buhanzaz.web.dto.image.*;
import org.springframework.core.io.Resource;
import org.springframework.http.MediaType;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.service.annotation.HttpExchange;

import java.util.List;

@HttpExchange("/api/v1/storage/images")
public interface ImageStorageClient {
    // Uploading a single image
    @PostMapping(
            consumes = MediaType.MULTIPART_FORM_DATA_VALUE
    )
    String uploadImage(@RequestPart("file") Resource file,
                       @RequestPart("upload-request") UploadImageRequestDto dto);

    // Uploading multiple images
    @PostMapping("/batch")
    List<String> uploadImages(@RequestPart("files") List<Resource> files,
                              @RequestPart("upload-request") UploadImagesBatchRequestDto dto);

    // Single Image Acquisition
    @GetMapping
    byte[] getImage(@RequestBody GetImageRequestDto dto);

    // Multiple Image Acquisition
    @GetMapping("/batch")
    List<byte[]> getImages(@RequestBody GetImagesBatchRequestDto dto);

    // Get all images from a folder
    @GetMapping("/folder")
    List<byte[]> getImagesFromFolder(@RequestBody GetFolderImagesRequestDto dto);

    // Delete a single image
    @DeleteMapping
    void deleteImage(@RequestBody DeleteImageRequestDto dto);

    // Deleting multiple images
    @DeleteMapping("/batch")
    void deleteImages(@RequestBody DeleteImagesBatchRequestDto dto);

    // Delete all images from a folder and the folder itself
    @DeleteMapping("/folder")
    void deleteFolderWithImages(@RequestBody DeleteFolderRequestDto dto);
}
