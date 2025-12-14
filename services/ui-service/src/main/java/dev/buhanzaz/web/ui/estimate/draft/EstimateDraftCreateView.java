package dev.buhanzaz.web.ui.estimate.draft;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.upload.Upload;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.server.streams.UploadHandler;
import dev.buhanzaz.web.domain.EstimateDraft;
import dev.buhanzaz.web.dto.image.DeleteFolderRequestDto;
import dev.buhanzaz.web.dto.image.DeleteImageRequestDto;
import dev.buhanzaz.web.dto.image.UploadImageRequestDto;
import dev.buhanzaz.web.service.EstimateDraftClient;
import dev.buhanzaz.web.service.ImageStorageClient;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;

import java.time.format.DateTimeFormatter;

@Slf4j
@Route("estimate/draft/create")
public class EstimateDraftCreateView extends VerticalLayout {

    private static final String BUCKET_NAME = "image";
    private static final String WAREHOUSE_FOLDER = "spb";
    private static final String FOLDER_NAME = "after_rent";
    private static final DateTimeFormatter FORMATTER = DateTimeFormatter.ofPattern("yyyy-MM-dd");

    private final transient ImageStorageClient imageStorageClient;
    private final transient EstimateDraftClient estimateDraftClient;
    private final transient EstimateDraft draft;
    private final Binder<EstimateDraft> binder;

    public EstimateDraftCreateView(
            ImageStorageClient imageStorageClient,
            EstimateDraftClient estimateDraftClient
    ) {
        viewSettings();
        this.imageStorageClient = imageStorageClient;
        this.estimateDraftClient = estimateDraftClient;
        this.draft = new EstimateDraft();

        EstimateDraftCreateForm estimateDraftCreateForm = new EstimateDraftCreateForm(draft);
        this.binder = estimateDraftCreateForm.getBinder();

        add(
                createHeader(),
                estimateDraftCreateForm,
                createUploadImages(),
                createFooter()
        );


    }

    private Upload createUploadImages() {
        Upload upload = new Upload(createMemoryHandler());

        upload.setAcceptedFileTypes("image/*");
        upload.addFileRejectedListener(event -> log.info("Image rejected: {}", event.getErrorMessage()));
        upload.addFileRemovedListener(event -> {
            log.info("Image removed: {}", event.getFileName());
            String objectKey = draft.getImageUrl(event.getFileName());
            imageStorageClient.deleteImage(new DeleteImageRequestDto(BUCKET_NAME, objectKey));
            draft.removeImageUrl(event.getFileName());
        });

        return upload;
    }

    private UploadHandler createMemoryHandler() {
        return UploadHandler.inMemory(
                (metadata, data) -> {
                    String fileName = metadata.fileName();

                    String cabinId = draft.getCabinId();
                    if (cabinId == null || cabinId.isBlank()) {
                        log.warn("cabinId пустой, не могу загрузить файл {}", fileName);
                        return;
                    }

                    String rentalReturnDate = draft.getRentalReturnDate().format(FORMATTER);

                    UploadImageRequestDto imageUploadRequest = new UploadImageRequestDto(
                            "TestFirstName",
                            "TestLastName",
                            "TestPosition",
                            BUCKET_NAME,
                            WAREHOUSE_FOLDER,
                            cabinId,
                            FOLDER_NAME,
                            rentalReturnDate
                    );

                    ByteArrayResource resource = new ByteArrayResource(data) {
                        @Override
                        public String getFilename() {
                            return fileName;
                        }
                    };

                    String imageUrl = imageStorageClient.uploadImage(resource, imageUploadRequest);

                    draft.addImageUrl(fileName, imageUrl);
                }
        );
    }
    
    private Component createFooter() {
        HorizontalLayout horizontalLayout = new HorizontalLayout(
                new Button("Сохаранить черновик", _ -> {
                    estimateDraftClient.save(draft);
                    UI.getCurrent().navigate(EstimateDraftView.class);
                    log.info("Черновик сметы сохранен: {}", draft);
                }),
                new Button("Отмена", _ -> {
                    imageStorageClient.deleteFolderWithImages(
                            new DeleteFolderRequestDto(
                                    BUCKET_NAME,
                                    WAREHOUSE_FOLDER,
                                    draft.getCabinId(),
                                    FOLDER_NAME,
                                    draft.getRentalReturnDate().format(FORMATTER)
                            )
                    );

                    draft.clearImageUrls();

                    binder.refreshFields();
                    log.info("Создание черновика сметы отменено и данные очищены");
                })
        );

        horizontalLayout.setAlignItems(Alignment.CENTER);
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        return horizontalLayout;
    }

    private Component createHeader() {
        return new H1("Меню черновиков смет");
    }

    private void viewSettings() {
        setSizeFull();
        setPadding(false);
        setSpacing(true);
        setMargin(false);
        setAlignItems(Alignment.STRETCH);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }
}
