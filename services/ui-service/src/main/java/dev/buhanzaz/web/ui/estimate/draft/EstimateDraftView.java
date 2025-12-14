package dev.buhanzaz.web.ui.estimate.draft;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteParam;
import dev.buhanzaz.web.domain.EstimateDraft;
import dev.buhanzaz.web.client.EstimateDraftClient;
import dev.buhanzaz.web.ui.estimate.full.EstimateCreateView;

@Route("estimate/draft/list")
public class EstimateDraftView extends VerticalLayout {
    private final transient EstimateDraftClient estimateDraftClient;

    public EstimateDraftView(EstimateDraftClient estimateDraftClient) {
        this.estimateDraftClient = estimateDraftClient;

        add(
                getHeader(),
                getEstimateGrid(),
                getFooter()
        );

        viewSettings();
    }

    private Component getFooter() {
        Button createDraftEstimate = new Button(
                "Создать черновик сметы",
                _ -> UI.getCurrent().navigate(EstimateDraftCreateView.class)
        );
        Button createEstimate = new Button(
                "Создать полную смету",
                _ -> UI.getCurrent().navigate("estimate/create")
        );

        HorizontalLayout horizontalLayout = new HorizontalLayout(
                createDraftEstimate,
                createEstimate
        );

        horizontalLayout.setAlignItems(Alignment.CENTER);
        horizontalLayout.setJustifyContentMode(JustifyContentMode.CENTER);
        return horizontalLayout;
    }

    private Component getHeader() {
        return new H1("Меню смет");
    }

    private void viewSettings() {
        setSizeFull();
        setPadding(false);
        setSpacing(false);
        setMargin(false);
        setAlignItems(Alignment.STRETCH);
        setJustifyContentMode(JustifyContentMode.CENTER);
    }

    private Component getEstimateGrid() {
        var layout = new VerticalLayout();
        H3 title = new H3("Черновики смет");
        Grid<EstimateDraft> grid = new Grid<>(EstimateDraft.class, false);

        grid.addColumn(EstimateDraft::getCabinId).setHeader("Номер бытовки");
        grid.addComponentColumn(estimateDraft -> {
                    Button continueCreateEstimate = new Button("Продолжить создание сметы");

                    continueCreateEstimate.addClickListener(
                            _ -> UI.getCurrent()
                                    .navigate(
                                            EstimateCreateView.class,
                                            new RouteParam("cabinId", estimateDraft.getCabinId()
                                            )
                                    )
                    );

                    return continueCreateEstimate;
                }
        ).setHeader("Действия");

        grid.setItems(estimateDraftClient.getAll());
        grid.setSizeFull();

        layout.add(title, grid);
        layout.setSizeFull();

        return layout;
    }
}
