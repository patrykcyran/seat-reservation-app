package filiciak.cyran.demo.UI.views.adminViews.room;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import filiciak.cyran.demo.Controllers.ConferenceRoomController;
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

@PageTitle("Add Room")
@Route(value = "add-room-view", layout = MainLayout.class)
public class AddRoomView extends Div implements AfterNavigationObserver, HasComponents, HasStyle{

    private ComboBox<String> reservationStatus = new ComboBox<>();
    private ComboBox<String> office = new ComboBox<>();
    private CheckboxGroup<String> equipment = new CheckboxGroup<>();
    private TextField roomName = new TextField();

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    ConferenceRoomController conferenceRoomController;
    OfficeController officeController;
    EquipmentController equipmentController;
    ConferenceRoom room = new ConferenceRoom();

    public AddRoomView(OfficeController officeController, ConferenceRoomController conferenceRoomController, EquipmentController equipmentController) throws BadRequestException {
        this.officeController = officeController;
        this.conferenceRoomController = conferenceRoomController;
        this.equipmentController = equipmentController;
        addClassName("manage-single-room-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());


        cancel.addClickListener(e -> UI.getCurrent().navigate(ManageRoomView.class));
        save.addClickListener(e -> {
            ResponseEntity<ConferenceRoom> createdRoom;

            if (reservationStatus.isEmpty() || roomName.isEmpty() || office.isEmpty()) {
                Notification.show("All fields must be filled up", 5000, Notification.Position.MIDDLE);
                return;
            }

            room.setStatus(AvailabilityStatus.valueOf(reservationStatus.getValue()));
            room.setName(roomName.getValue());
            room.setId(null);

            try {
                room.setOfficeId(officeController.getOfficeByName(office.getValue()).getId());
            } catch (BadRequestException ex) {
                throw new RuntimeException(ex);
            }
            try {
                createdRoom = conferenceRoomController.createRoom(room, "admin");
            } catch (BadRequestException ex) {
                throw new RuntimeException(ex);
            }

            room = createdRoom.getBody();

            Set<String> selectedEquipmentSet = equipment.getSelectedItems();
            List<String> selectedEquipmentList = selectedEquipmentSet.stream().toList();
            selectedEquipmentList.forEach(eq -> {
                try {
                    this.conferenceRoomController.addEquipment(room.getId(), this.equipmentController.getEquipmentByName(eq).getId(), "admin");
                } catch (BadRequestException ex) {
                    throw new RuntimeException(ex);
                }
            });

            UI.getCurrent().navigate(ManageRoomView.class);
        });
    }

    private Component createTitle() {
        return new H3("Create new room");
    }

    private Component createFormLayout() throws BadRequestException {
        FormLayout formLayout = new FormLayout();

        List<String> statusList = new ArrayList<>();
        Arrays.stream(AvailabilityStatus.values()).forEach(s -> statusList.add(s.name()));

        List<String> officeList = new ArrayList<>();
        officeController.all().forEach(o -> officeList.add(o.getName()));

        List<String> equipmentList = new ArrayList<>();
        equipmentController.all().forEach(e -> equipmentList.add(e.getName()));

        reservationStatus.setWidth("120px");
        reservationStatus.setLabel("Reservation Status");
        reservationStatus.setItems(statusList);
        reservationStatus.setValue(AvailabilityStatus.FREE.name());

        office.setWidth("120px");
        office.setLabel("Office");
        office.setItems(officeList);

        equipment.setWidth("120px");
        equipment.setLabel("Equipment");
        equipment.setItems(equipmentList);

        roomName.setWidth("120px");
        roomName.setLabel("Room name");


        formLayout.add(reservationStatus, office, roomName, equipment);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        save.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(save);
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

