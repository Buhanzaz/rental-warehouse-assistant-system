package dev.buhanzaz.web.ui.estimate.draft;

import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import dev.buhanzaz.web.domain.EstimateDraft;
import lombok.Getter;

@Getter
public class EstimateDraftCreateForm extends FormLayout {
    private final Binder<EstimateDraft> binder;

    public EstimateDraftCreateForm(EstimateDraft estimateDraft) {
        this.binder = new Binder<>(EstimateDraft.class);
        this.binder.setBean(estimateDraft);

        TextField cabinIdField = new TextField();
        addFormItem(cabinIdField, "Номер бытовки");
        binder.bind(cabinIdField, EstimateDraft::getCabinId, EstimateDraft::setCabinId);

        DatePicker startDatePicker = new DatePicker();
        addFormItem(startDatePicker, "Дата возврата");
        binder.bind(startDatePicker, EstimateDraft::getRentalReturnDate, EstimateDraft::setRentalReturnDate);

        add(cabinIdField, startDatePicker);
    }
}
