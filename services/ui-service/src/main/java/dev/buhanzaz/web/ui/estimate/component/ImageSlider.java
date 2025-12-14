package dev.buhanzaz.web.ui.estimate.component;

import com.vaadin.flow.component.Key;
import com.vaadin.flow.component.ModalityMode;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.dialog.Dialog;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.Image;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.Route;
import lombok.extern.slf4j.Slf4j;
import org.springframework.core.io.ByteArrayResource;

import java.util.List;
import java.util.concurrent.atomic.AtomicInteger;

@Slf4j
@Route("slider")
public class ImageSlider extends VerticalLayout {

    private List<Image> images;
    private final Div imageContainer = new Div();
    private final Div placeholder = new Div();
    private final AtomicInteger index = new AtomicInteger(0);

    public ImageSlider() {

        setHeightFull();
        setPadding(false);
        setSpacing(false);
        setMargin(false);
        setJustifyContentMode(JustifyContentMode.START);
        setAlignItems(Alignment.STRETCH);

        // КЛЮЧЕВОЕ: запрещаем контейнеру расти выше отведённого места
        getStyle().set("overflow", "hidden");
        getStyle().set("min-height", "0");

        initImage();
        initControls();
    }

    public void setImages(List<byte[]> images) {
        this.images = images.stream()
                .map(data -> {
                            ByteArrayResource byteArrayResource = new ByteArrayResource(data);
                            return new Image(data, byteArrayResource.getFilename());
                        }
                )
                .toList();
        index.set(0);
        updateImage();
    }

    private void initImage() {
        // Фиксируем область для картинки относительно родителя, чтобы UI не прыгал и не раздувал страницу
        imageContainer.setWidthFull();
        imageContainer.setHeightFull();
        imageContainer.getStyle().set("display", "flex");
        imageContainer.getStyle().set("align-items", "center");
        imageContainer.getStyle().set("justify-content", "center");
        imageContainer.getStyle().set("overflow", "hidden");
        imageContainer.getStyle().set("background-color", "#f8f8f8"); // опционально — фон плейсхолдера

        // Плейсхолдер, когда нет изображений
        placeholder.setText("Нет изображений");
        placeholder.getStyle().set("color", "#888");
        placeholder.getStyle().set("font-size", "1rem");

        updateImage();
        add(imageContainer);
        setFlexGrow(1, imageContainer);
    }

    private void initControls() {
        Button prev = new Button("←", _ -> {
            if (images.isEmpty()) {
                return;
            }
            index.updateAndGet(i -> (i - 1 + images.size()) % images.size());
            updateImage();
        });

        Button next = new Button("→", _ -> {
            if (images.isEmpty()) {
                return;
            }
            index.updateAndGet(i -> (i + 1) % images.size());
            updateImage();
        });

        prev.addClickShortcut(Key.ARROW_LEFT);
        next.addClickShortcut(Key.ARROW_RIGHT);
        HorizontalLayout controls = new HorizontalLayout(prev, next);
        controls.setWidthFull();
        add(controls);
    }

    private void updateImage() {
        imageContainer.removeAll();

        if (images.isEmpty()) {
            return;
        }

        Image current = images.get(index.get());
        // Стилизуем изображение
        current.setHeight("100%");
        current.setWidth("auto");
        applyObjectFitContainStyle(current);
        current.getStyle().set("cursor", "pointer");
        current.getStyle().set("max-width", "100%");

        // Перед добавлением убедимся, что элемент не в другом месте
        current.getParent().ifPresent(parent -> parent.getElement().removeChild(current.getElement()));

        // Клик для показа на весь экран
        current.addClickListener(_ -> openFullscreen());

        imageContainer.add(current);
    }

    private void applyObjectFitContainStyle(Image current) {
        current.getStyle().set("object-fit", "contain");
    }

    private void openFullscreen() {
        if (images.isEmpty()) {
            return;
        }

        Dialog dialog = new Dialog();
        dialog.setModality(ModalityMode.VISUAL);
        dialog.setWidthFull();
        dialog.setHeightFull();

        dialog.setCloseOnEsc(true);

        Image current = images.get(index.get());
        // Используем тот же Image, но меняем стили для fullscreen
        current.setSizeFull();
        applyObjectFitContainStyle(current);

        // Убираем из контейнера перед добавлением в диалог
        current.getParent().ifPresent(parent -> parent.getElement().removeChild(current.getElement()));

        dialog.addOpenedChangeListener(e -> {
            if (!e.isOpened()) {
                // Возвращаем изображение обратно в контейнер при закрытии
                current.setWidth("100%");
                current.setHeight("auto");
                applyObjectFitContainStyle(current);
                imageContainer.add(current);
            }
        });

        current.addClickListener(_ -> dialog.close()); // закрытие по клику

        dialog.add(current);
        dialog.open();
    }
}
