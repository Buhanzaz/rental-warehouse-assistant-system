// java
package dev.buhanzaz.web.ui.estimate.full;

import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.BeforeEnterEvent;
import com.vaadin.flow.router.BeforeEnterObserver;
import com.vaadin.flow.router.Route;
import dev.buhanzaz.web.domain.EstimateDraft;
import dev.buhanzaz.web.dto.image.GetImagesBatchRequestDto;
import dev.buhanzaz.web.service.EstimateDraftClient;
import dev.buhanzaz.web.service.ImageStorageClient;
import dev.buhanzaz.web.ui.estimate.component.ChatBot;
import dev.buhanzaz.web.ui.estimate.component.ImageSlider;

import java.util.List;

@Route("estimate/create/:cabinId")
public class EstimateCreateView extends VerticalLayout implements BeforeEnterObserver {

    private final transient EstimateDraftClient estimateDraftClient;
    private final transient ImageStorageClient imageStorageClient;
    private static final String BUCKET_NAME = "image";
    private final H1 header;
    private final ImageSlider imageSlider;

    public EstimateCreateView(
            EstimateDraftClient estimateDraftClient,
            ImageStorageClient imageStorageClient,
            ChatBot chatBotView
    ) {
        viewSettings();

        this.estimateDraftClient = estimateDraftClient;
        this.imageStorageClient = imageStorageClient;

        this.header = new H1("Создание полной сметы");
        this.imageSlider = new ImageSlider();

        VerticalLayout contentLayout = new VerticalLayout();
        contentLayout.setSizeFull();
        contentLayout.setPadding(false);
        contentLayout.setSpacing(false);
        contentLayout.setMargin(false);
        contentLayout.setAlignItems(Alignment.STRETCH);

        contentLayout.getStyle().set("overflow", "hidden");
        contentLayout.getStyle().set("min-height", "0");

        contentLayout.add(imageSlider, chatBotView);

        contentLayout.setFlexGrow(1, imageSlider);
        contentLayout.setFlexGrow(1, chatBotView);

        add(header);
        addAndExpand(contentLayout);
    }

    @Override
    public void beforeEnter(BeforeEnterEvent event) {
        EstimateDraft draft;
        String cabinId = event.getRouteParameters()
                .get("cabinId")
                .orElseThrow(() -> new IllegalArgumentException("Cabin id is required"));

        draft = estimateDraftClient.findById(cabinId)
                .orElseThrow(() -> new IllegalArgumentException("Draft not found"));

        header.setText("Создание полной сметы: " + draft.getCabinId());

        List<String> imageUrls = draft.getImageUrls();
        imageSlider.setImages(imageStorageClient.getImages(
                new GetImagesBatchRequestDto(
                        BUCKET_NAME,
                        imageUrls
                )
        ));
    }

    private void viewSettings() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setMargin(false);
        setAlignItems(Alignment.STRETCH);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }
}
