package filiciak.cyran.demo.UI.views.adminViews.office;

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
import filiciak.cyran.demo.Controllers.AddressController;
import filiciak.cyran.demo.Controllers.EquipmentController;
import filiciak.cyran.demo.Controllers.OfficeController;
import filiciak.cyran.demo.Controllers.SeatController;
import filiciak.cyran.demo.Entities.*;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.UI.views.MainLayout;
import filiciak.cyran.demo.UI.views.adminViews.manageObjects.AddRoomsToOfficeView;
import filiciak.cyran.demo.UI.views.adminViews.manageObjects.AddSeatsToOfficeView;
import filiciak.cyran.demo.UI.views.adminViews.manageObjects.DeleteRoomsFromOfficeView;
import filiciak.cyran.demo.UI.views.adminViews.manageObjects.DeleteSeatsFromOfficeView;
import filiciak.cyran.demo.UI.views.adminViews.seat.ManageSeatView;
import filiciak.cyran.demo.UI.views.login.LoginView;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@PageTitle("Manage Single Office")
@Route(value = "manage-single-office", layout = MainLayout.class)
public class ChangeSingleOfficeView extends Div implements AfterNavigationObserver, HasComponents, HasStyle {

    private TextField officeName = new TextField();
    private TextField city = new TextField();
    private TextField country = new TextField();
    private TextField postal_code = new TextField();
    private TextField street = new TextField();

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");
    private Button addSeats = new Button("Add Seats");
    private Button deleteSeats = new Button("Delete Seats");
    private Button addRooms = new Button("Add Rooms");
    private Button deleteRooms = new Button("Delete Rooms");

    OfficeController officeController;
    AddressController addressController;
    Office office = new Office();
    Address address;

    public ChangeSingleOfficeView(OfficeController officeController, AddressController addressController) throws BadRequestException {
        this.officeController = officeController;
        this.addressController = addressController;
        if(ComponentUtil.getData(UI.getCurrent(), Seat.class) == null) {
            UI.getCurrent().navigate(ManageSeatView.class);
        }
        office = ComponentUtil.getData(UI.getCurrent(), Office.class);
        address = office.getAddress();
        addClassName("manage-single-office-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());
        add(createButtonLayout2());


        cancel.addClickListener(e -> UI.getCurrent().navigate(ManageOfficeView.class));
        save.addClickListener(e -> {

            if (officeName.isEmpty() || city.isEmpty() || country.isEmpty() || postal_code.isEmpty() || street.isEmpty()) {
                Notification.show("All fields must be filled up", 5000, Notification.Position.MIDDLE);
                return;
            }

            Address address = new Address();
            address.setCity(city.getValue());
            address.setCountry(country.getValue());
            address.setStreet(street.getValue());
            address.setPostalCode(postal_code.getValue());

            if(!addressController.all().contains(address)) {
                try {
                    addressController.createAddress(address, "admin");
                } catch (BadRequestException ex) {
                    throw new RuntimeException(ex);
                }
            }

            office.setAddress(address);
            office.setName(officeName.getValue());

            try {
                officeController.updateOffice(office, "admin");
            } catch (BadRequestException ex) {
                throw new RuntimeException(ex);
            }

            UI.getCurrent().navigate(ManageOfficeView.class);
        });
        delete.addClickListener(e -> {
            try {
                officeController.deleteOffice(office.getId(), "admin");
            } catch (BadRequestException ex) {
                throw new RuntimeException(ex);
            }
            UI.getCurrent().navigate(ManageOfficeView.class);
        });
        addSeats.addClickListener(e -> {
            ComponentUtil.setData(UI.getCurrent(), Office.class, office);
            UI.getCurrent().navigate(AddSeatsToOfficeView.class);
        });
        deleteSeats.addClickListener(e -> {
            ComponentUtil.setData(UI.getCurrent(), Office.class, office);
            UI.getCurrent().navigate(DeleteSeatsFromOfficeView.class);
        });
        addRooms.addClickListener(e -> {
            ComponentUtil.setData(UI.getCurrent(), Office.class, office);
            UI.getCurrent().navigate(AddRoomsToOfficeView.class);
        });
        deleteRooms.addClickListener(e -> {
            ComponentUtil.setData(UI.getCurrent(), Office.class, office);
            UI.getCurrent().navigate(DeleteRoomsFromOfficeView.class);
        });
    }

    private Component createTitle() {
        return new H3("Manage " + office.getName());
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();

        officeName.setWidth("120px");
        officeName.setLabel("Office Name");
        officeName.setValue(office.getName());

        city.setWidth("120px");
        city.setLabel("City");
        city.setValue(address.getCity());

        country.setWidth("120px");
        country.setLabel("Country");
        country.setValue(address.getCountry());

        postal_code.setWidth("120px");
        postal_code.setLabel("Postal Code");
        postal_code.setValue(address.getPostalCode());

        street.setWidth("120px");
        street.setLabel("Street");
        street.setValue(address.getStreet());

        formLayout.add(officeName, city, country, postal_code, street);
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

    private Component createButtonLayout2() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button2-layout");
        addSeats.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        addRooms.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(addSeats);
        buttonLayout.add(deleteSeats);
        buttonLayout.add(addRooms);
        buttonLayout.add(deleteRooms);
        return buttonLayout;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        if (!UserInstance.getInstance().isLogged() && !UserInstance.getInstance().isAdmin()) {
            UI.getCurrent().navigate(LoginView.class);
        }
    }
}
