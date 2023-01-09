package filiciak.cyran.demo.UI.views.adminViews.room;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
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
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@PageTitle("Manage Single Room")
@Route(value = "manage-single-room", layout = MainLayout.class)
public class ChangeSingleRoomView extends Div implements AfterNavigationObserver, HasComponents, HasStyle{

    private ComboBox<String> reservationStatus = new ComboBox<>();
    private ComboBox<String> office = new ComboBox<>();
    private CheckboxGroup<String> equipment = new CheckboxGroup<>();

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    private Button delete = new Button("Delete");

    ConferenceRoomController conferenceRoomController;
    OfficeController officeController;
    EquipmentController equipmentController;
    ConferenceRoom room = new ConferenceRoom();

    public ChangeSingleRoomView(OfficeController officeController, ConferenceRoomController conferenceRoomController, EquipmentController equipmentController) throws BadRequestException {
        this.officeController = officeController;
        this.conferenceRoomController = conferenceRoomController;
        this.equipmentController = equipmentController;
        if(ComponentUtil.getData(UI.getCurrent(), ConferenceRoom.class) == null) {
            UI.getCurrent().navigate(ManageRoomView.class);
        }
        room = ComponentUtil.getData(UI.getCurrent(), ConferenceRoom.class);
        addClassName("manage-single-room-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());


        cancel.addClickListener(e -> UI.getCurrent().navigate(ManageRoomView.class));
        save.addClickListener(e -> {
            room.setStatus(AvailabilityStatus.valueOf(reservationStatus.getValue()));

            this.equipmentController.all().forEach(eq -> {
                try {
                    this.conferenceRoomController.deleteEquipment(room.getId(), eq.getId(), "admin");
                } catch (BadRequestException ex) {
                    throw new RuntimeException(ex);
                }
            });

            try {
                room.setOfficeId(officeController.getOfficeByName(office.getValue()).getId());
            } catch (BadRequestException ex) {
                throw new RuntimeException(ex);
            }
            try {
                conferenceRoomController.updateRoom(room, "admin");
            } catch (BadRequestException ex) {
                throw new RuntimeException(ex);
            }

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
        delete.addClickListener(e -> {
            try {
                conferenceRoomController.deleteRoom(room.getId(), "admin");
            } catch (BadRequestException ex) {
                throw new RuntimeException(ex);
            }
            UI.getCurrent().navigate(ManageRoomView.class);
        });
    }

    private Component createTitle() {
        return new H3("Room " + room.getName());
    }

    private Component createFormLayout() throws BadRequestException {
        FormLayout formLayout = new FormLayout();

        List<String> statusList = new ArrayList<>();
        Arrays.stream(AvailabilityStatus.values()).forEach(s -> statusList.add(s.name()));

        List<String> officeList = new ArrayList<>();
        officeController.all().forEach(o -> officeList.add(o.getName()));

        List<String> equipmentList = new ArrayList<>();
        equipmentController.all().forEach(e -> equipmentList.add(e.getName()));

        List<String> currentEquipment = new ArrayList<>(conferenceRoomController.getEquipment(room.getId()));

        reservationStatus.setWidth("120px");
        reservationStatus.setLabel("Reservation Status");
        reservationStatus.setItems(statusList);
        reservationStatus.setValue(room.getStatus().name());

        office.setWidth("120px");
        office.setLabel("Office");
        office.setItems(officeList);
        office.setValue(officeController.getOffice(room.getOfficeId()).getName());

        equipment.setWidth("120px");
        equipment.setLabel("Equipment");
        equipment.setItems(equipmentList);
        equipment.select(currentEquipment);

        formLayout.add(reservationStatus, office, equipment);
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
