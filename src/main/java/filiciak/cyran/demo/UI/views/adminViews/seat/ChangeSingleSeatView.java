package filiciak.cyran.demo.UI.views.adminViews.seat;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.customfield.CustomField;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.EmailField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import filiciak.cyran.demo.Controllers.EquipmentController;
import filiciak.cyran.demo.Controllers.OfficeController;
import filiciak.cyran.demo.Controllers.SeatController;
import filiciak.cyran.demo.Entities.*;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.Services.OfficeService;
import filiciak.cyran.demo.Services.SeatService;
import filiciak.cyran.demo.UI.views.MainLayout;
import filiciak.cyran.demo.UI.views.login.LoginView;
import org.hibernate.service.spi.InjectService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.transaction.annotation.Transactional;

import java.util.*;
import java.util.stream.Stream;

@PageTitle("Manage Single Seat")
@Route(value = "manage-single-seat", layout = MainLayout.class)
public class ChangeSingleSeatView extends Div implements AfterNavigationObserver, HasComponents, HasStyle {

    private ComboBox<String> reservationStatus = new ComboBox<>();
    private ComboBox<String> seatType = new ComboBox<>();
    private ComboBox<String> office = new ComboBox<>();
    private CheckboxGroup<String> equipment = new CheckboxGroup<>();

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");
    private Button delete = new Button("Delete");

    SeatController seatController;
    OfficeController officeController;
    EquipmentController equipmentController;
    Seat seat = new Seat();

    public ChangeSingleSeatView(OfficeController officeController, SeatController seatController, EquipmentController equipmentController) throws BadRequestException {
        this.officeController = officeController;
        this.seatController = seatController;
        this.equipmentController = equipmentController;
        if(ComponentUtil.getData(UI.getCurrent(), Seat.class) == null) {
            UI.getCurrent().navigate(ManageSeatView.class);
        }
        seat = ComponentUtil.getData(UI.getCurrent(), Seat.class);
        addClassName("manage-single-seat-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());


        cancel.addClickListener(e -> UI.getCurrent().navigate(ManageSeatView.class));
        save.addClickListener(e -> {
            seat.setStatus(AvailabilityStatus.valueOf(reservationStatus.getValue()));
            seat.setType(SeatType.valueOf(seatType.getValue()));

            this.equipmentController.all().forEach(eq -> {
                try {
                    this.seatController.deleteEquipment(seat.getId(), eq.getId(), "admin");
                } catch (BadRequestException ex) {
                    throw new RuntimeException(ex);
                }
            });

            try {
                seat.setOfficeID(this.officeController.getOfficeByName(office.getValue()).getId());
            } catch (BadRequestException ex) {
                throw new RuntimeException(ex);
            }
            try {
                this.seatController.updateSeat(seat, "admin");
            } catch (BadRequestException ex) {
                throw new RuntimeException(ex);
            }

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
        delete.addClickListener(e -> {
            try {
                seatController.deleteSeat(seat.getId(), "admin");
            } catch (BadRequestException ex) {
                throw new RuntimeException(ex);
            }
            UI.getCurrent().navigate(ManageSeatView.class);
        });
    }

    private Component createTitle() {
        return new H3("Seat " + seat.getSeatNumber());
    }

    private Component createFormLayout() throws BadRequestException {
        FormLayout formLayout = new FormLayout();

        List<String> statusList = new ArrayList<>();
        Arrays.stream(AvailabilityStatus.values()).forEach(s -> statusList.add(s.name()));

        List<String> seatTypeList = new ArrayList<>();
        Arrays.stream(SeatType.values()).forEach(s -> seatTypeList.add(s.name()));

        List<String> officeList = new ArrayList<>();
        officeController.all().forEach(o -> officeList.add(o.getName()));

        List<String> equipmentList = new ArrayList<>();
        equipmentController.all().forEach(e -> equipmentList.add(e.getName()));

        List<String> currentEquipment = new ArrayList<>(seatController.getEquipment(seat.getId()));

        reservationStatus.setWidth("120px");
        reservationStatus.setLabel("Reservation Status");
        reservationStatus.setItems(statusList);
        reservationStatus.setValue(seat.getStatus().name());

        seatType.setWidth("120px");
        seatType.setLabel("Seat Type");
        seatType.setItems(seatTypeList);
        seatType.setValue(seat.getType().name());

        office.setWidth("120px");
        office.setLabel("Office");
        office.setItems(officeList);
        office.setValue(officeController.getOffice(seat.getOfficeID()).getName());

        equipment.setWidth("120px");
        equipment.setLabel("Equipment");
        equipment.setItems(equipmentList);
        equipment.select(currentEquipment);

        formLayout.add(reservationStatus, seatType, office, equipment);
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
