package filiciak.cyran.demo.UI.views.adminViews.seat;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import filiciak.cyran.demo.Controllers.EquipmentController;
import filiciak.cyran.demo.Controllers.OfficeController;
import filiciak.cyran.demo.Controllers.SeatController;
import filiciak.cyran.demo.Entities.AvailabilityStatus;
import filiciak.cyran.demo.Entities.Seat;
import filiciak.cyran.demo.Entities.SeatType;
import filiciak.cyran.demo.Entities.UserInstance;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.UI.views.MainLayout;
import filiciak.cyran.demo.UI.views.login.LoginView;
import org.springframework.http.ResponseEntity;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@PageTitle("Add Seat")
@Route(value = "add-seat-view", layout = MainLayout.class)
public class AddSeatView extends Div implements AfterNavigationObserver, HasComponents, HasStyle {

    private ComboBox<String> reservationStatus = new ComboBox<>();
    private ComboBox<String> seatType = new ComboBox<>();
    private ComboBox<String> office = new ComboBox<>();
    private CheckboxGroup<String> equipment = new CheckboxGroup<>();

    private Button cancel = new Button("Cancel");
    private Button add = new Button("Add");

    SeatController seatController;
    OfficeController officeController;
    EquipmentController equipmentController;
    Seat seat = new Seat();

    public AddSeatView(OfficeController officeController, SeatController seatController, EquipmentController equipmentController) throws BadRequestException {
        this.officeController = officeController;
        this.seatController = seatController;
        this.equipmentController = equipmentController;
        addClassName("manage-single-seat-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());


        cancel.addClickListener(e -> UI.getCurrent().navigate(ManageSeatView.class));
        add.addClickListener(e -> {
            ResponseEntity<Seat> createdSeat;

            if (reservationStatus.isEmpty() || seatType.isEmpty() || office.isEmpty()) {
                Notification.show("All fields must be filled up", 5000, Notification.Position.MIDDLE);
                return;
            }

            seat.setStatus(AvailabilityStatus.valueOf(reservationStatus.getValue()));
            seat.setType(SeatType.valueOf(seatType.getValue()));
            seat.setSeatNumber(seatController.getSeatWithHighestNumber().getSeatNumber() + 1);
            seat.setId(null);

            try {
                seat.setOfficeID(this.officeController.getOfficeByName(office.getValue()).getId());
            } catch (BadRequestException ex) {
                throw new RuntimeException(ex);
            }
            try {
                createdSeat = this.seatController.createSeat(seat, "admin");
            } catch (BadRequestException ex) {
                throw new RuntimeException(ex);
            }

            seat = createdSeat.getBody();

            Set<String> selectedEquipmentSet = equipment.getSelectedItems();
            List<String> selectedEquipmentList = selectedEquipmentSet.stream().toList();
            selectedEquipmentList.forEach(eq -> {
                try {
                    this.seatController.addEquipment(seat.getId(), this.equipmentController.getEquipmentByName(eq).getId(), "admin");
                } catch (BadRequestException ex) {
                    throw new RuntimeException(ex);
                }
            });

            UI.getCurrent().navigate(ManageSeatView.class);
        });
    }

    private Component createTitle() {
        return new H3("Create new seat");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();

        List<String> statusList = new ArrayList<>();
        Arrays.stream(AvailabilityStatus.values()).forEach(s -> statusList.add(s.name()));

        List<String> seatTypeList = new ArrayList<>();
        Arrays.stream(SeatType.values()).forEach(s -> seatTypeList.add(s.name()));

        List<String> officeList = new ArrayList<>();
        officeController.all().forEach(o -> officeList.add(o.getName()));

        List<String> equipmentList = new ArrayList<>();
        equipmentController.all().forEach(e -> equipmentList.add(e.getName()));

        reservationStatus.setWidth("120px");
        reservationStatus.setLabel("Reservation Status");
        reservationStatus.setItems(statusList);
        reservationStatus.setValue(AvailabilityStatus.FREE.name());

        seatType.setWidth("120px");
        seatType.setLabel("Seat Type");
        seatType.setItems(seatTypeList);
        seatType.setValue(SeatType.NORMAL.name());

        office.setWidth("120px");
        office.setLabel("Office");
        office.setItems(officeList);

        equipment.setWidth("120px");
        equipment.setLabel("Equipment");
        equipment.setItems(equipmentList);

        formLayout.add(reservationStatus, seatType, office, equipment);
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
