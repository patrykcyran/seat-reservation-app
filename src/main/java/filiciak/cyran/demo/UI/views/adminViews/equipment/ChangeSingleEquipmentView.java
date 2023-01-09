package filiciak.cyran.demo.UI.views.adminViews.equipment;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import filiciak.cyran.demo.Controllers.EquipmentController;
import filiciak.cyran.demo.Controllers.OfficeController;
import filiciak.cyran.demo.Entities.*;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.UI.views.MainLayout;
import filiciak.cyran.demo.UI.views.login.LoginView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@PageTitle("Manage Single Equipment")
@Route(value = "manage-single-equipment", layout = MainLayout.class)
public class ChangeSingleEquipmentView extends Div implements AfterNavigationObserver, HasComponents, HasStyle {

    private TextField equipmentName = new TextField();

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");

    EquipmentController equipmentController;
    Equipment equipment = new Equipment();

    public ChangeSingleEquipmentView(EquipmentController equipmentController) {
        this.equipmentController = equipmentController;
        if(ComponentUtil.getData(UI.getCurrent(), Equipment.class) == null) {
            UI.getCurrent().navigate(ManageEquipmentView.class);
        }
        equipment = ComponentUtil.getData(UI.getCurrent(), Equipment.class);
        addClassName("manage-single-equipment-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());


        cancel.addClickListener(e -> UI.getCurrent().navigate(ManageEquipmentView.class));
        save.addClickListener(e -> {
            if (equipmentName.isEmpty()) {
                Notification.show("All fields must be filled up", 5000, Notification.Position.MIDDLE);
                return;
            }

            equipment.setName(equipmentName.getValue());

            try {
                equipmentController.updateEquipment(equipment, "admin");
            } catch (BadRequestException ex) {
                throw new RuntimeException(ex);
            }

            UI.getCurrent().navigate(ManageEquipmentView.class);
        });
        delete.addClickListener(e -> {
            try {
                equipmentController.deleteEquipment(equipment.getId(), "admin");
            } catch (BadRequestException ex) {
                throw new RuntimeException(ex);
            }
            UI.getCurrent().navigate(ManageEquipmentView.class);
        });
    }

    private Component createTitle() {
        return new H3(equipment.getName());
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();

        equipmentName.setWidth("120px");
        equipmentName.setLabel("Equipment Name");
        equipmentName.setValue(equipment.getName());

        formLayout.add(equipmentName);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save);
        buttonLayout.add(cancel);
        buttonLayout.add(delete);
        return buttonLayout;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        if (!UserInstance.getInstance().isLogged() && !UserInstance.getInstance().isAdmin()) {
            UI.getCurrent().navigate(LoginView.class);
        }
    }
}
