package dev.buhanzaz.web.domain;

import lombok.*;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;


@Getter
@Setter
@Builder
@ToString
@EqualsAndHashCode
@AllArgsConstructor
@NoArgsConstructor
public class EstimateDraft {
    private String cabinId;
    private LocalDate rentalReturnDate;
    @Builder.Default
    private Map<String, String> imageFileUrlsMap = new HashMap<>();

    public void addImageUrl(String originalNameFile, String fileUrl) {
        this.imageFileUrlsMap.put(originalNameFile, fileUrl);
    }

    public String getImageUrl(String originalNameFile) {
        return this.imageFileUrlsMap.get(originalNameFile);
    }

    public void removeImageUrl(String originalNameFile) {
        this.imageFileUrlsMap.remove(originalNameFile);
    }

    public List<String> getImageUrls() {
        return new ArrayList<>(this.imageFileUrlsMap.values());
    }

    public void clearImageUrls() {
        this.imageFileUrlsMap.clear();
    }
}

