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
import filiciak.cyran.demo.Controllers.OfficeController;
import filiciak.cyran.demo.Controllers.SeatController;
import filiciak.cyran.demo.Entities.*;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.UI.views.MainLayout;
import filiciak.cyran.demo.UI.views.adminViews.seat.ManageSeatView;
import filiciak.cyran.demo.UI.views.login.LoginView;
import org.springframework.beans.factory.annotation.Autowired;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

@PageTitle("Manage Single Room")
@Route(value = "manage-single-room", layout = MainLayout.class)
public class ChangeSingleRoomView extends Div implements AfterNavigationObserver, HasComponents, HasStyle {

    private ComboBox<String> reservationStatus = new ComboBox<>();
    private ComboBox<String> office = new ComboBox<>();
    private CheckboxGroup<String> equipment = new CheckboxGroup<>();

    private Button cancel = new Button("Cancel");
    private Button save = new Button("Save");

    ConferenceRoomController conferenceRoomController;
    OfficeController officeController;
    ConferenceRoom room = new ConferenceRoom();

    public ChangeSingleRoomView(OfficeController officeController, ConferenceRoomController conferenceRoomController) throws BadRequestException {
        this.officeController = officeController;
        this.conferenceRoomController = conferenceRoomController;
        if(ComponentUtil.getData(UI.getCurrent(), Seat.class) == null) {
            UI.getCurrent().navigate(ManageSeatView.class);
        }
        room = ComponentUtil.getData(UI.getCurrent(), ConferenceRoom.class);
        addClassName("manage-single-seat-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());


        cancel.addClickListener(e -> UI.getCurrent().navigate(ManageSeatView.class));
        save.addClickListener(e -> {
            room.setStatus(AvailabilityStatus.valueOf(reservationStatus.getValue()));
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
            UI.getCurrent().navigate(ManageSeatView.class);
        });
    }

    private Component createTitle() {
        return new H3("Seat " + room.getName());
    }

    private Component createFormLayout() throws BadRequestException {
        FormLayout formLayout = new FormLayout();

        List<String> statusList = new ArrayList<>();
        Arrays.stream(AvailabilityStatus.values()).forEach(s -> statusList.add(s.name()));

        List<String> officeList = new ArrayList<>();
        officeController.all().forEach(o -> officeList.add(o.getName()));

        reservationStatus.setWidth("120px");
        reservationStatus.setLabel("Reservation Status");
        reservationStatus.setItems(statusList);
        reservationStatus.setValue(room.getStatus().name());

        office.setWidth("120px");
        office.setLabel("Office");
        office.setItems(officeList);
        office.setValue(officeController.getOffice(room.getOfficeId()).getName());

        formLayout.add(reservationStatus, office);
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
