package filiciak.cyran.demo.UI.views.adminViews.office;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
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
import filiciak.cyran.demo.Controllers.AddressController;
import filiciak.cyran.demo.Controllers.EquipmentController;
import filiciak.cyran.demo.Controllers.OfficeController;
import filiciak.cyran.demo.Entities.*;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.UI.views.MainLayout;
import filiciak.cyran.demo.UI.views.login.LoginView;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@PageTitle("Add Office")
@Route(value = "add-office-view", layout = MainLayout.class)
public class AddOfficeView extends Div implements AfterNavigationObserver, HasComponents, HasStyle {

    private TextField officeName = new TextField();
    private TextField city = new TextField();
    private TextField country = new TextField();
    private TextField postal_code = new TextField();
    private TextField street = new TextField();

    private Button cancel = new Button("Cancel");
    private Button add = new Button("Add");

    OfficeController officeController;
    AddressController addressController;

    public AddOfficeView(OfficeController officeController, AddressController addressController) throws BadRequestException {
        this.officeController = officeController;
        this.addressController = addressController;
        addClassName("manage-single-office-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());


        cancel.addClickListener(e -> UI.getCurrent().navigate(ManageOfficeView.class));
        add.addClickListener(e -> {
            ResponseEntity<Office> createdOffice;

            if (officeName.isEmpty() || city.isEmpty() || country.isEmpty() || postal_code.isEmpty() || street.isEmpty()) {
                Notification.show("All fields must be filled up", 5000, Notification.Position.MIDDLE);
                return;
            }

            Address address = new Address();
            address.setCity(city.getValue());
            address.setCountry(country.getValue());
            address.setStreet(street.getValue());
            address.setPostalCode(postal_code.getValue());

            try {
                addressController.createAddress(address, "admin");
            } catch (BadRequestException ex) {
                throw new RuntimeException(ex);
            }

            Office office = new Office();
            office.setAddress(address);
            office.setName(officeName.getValue());

            try {
                officeController.createOffice(office, "admin");
            } catch (BadRequestException ex) {
                throw new RuntimeException(ex);
            }

            UI.getCurrent().navigate(ManageOfficeView.class);
        });
    }

    private Component createTitle() {
        return new H3("Create new office");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();

        officeName.setWidth("120px");
        officeName.setLabel("Office Name");

        city.setWidth("120px");
        city.setLabel("City");

        country.setWidth("120px");
        country.setLabel("Country");

        postal_code.setWidth("120px");
        postal_code.setLabel("Postal Code");

        street.setWidth("120px");
        street.setLabel("Street");

        formLayout.add(officeName, city, country, postal_code, street);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        add.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(add);
        buttonLayout.add(cancel);
        return buttonLayout;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        if (!UserInstance.getInstance().isLogged() && !UserInstance.getInstance().isAdmin()) {
            UI.getCurrent().navigate(LoginView.class);
        }
    }
}
