package filiciak.cyran.demo.UI.views.makeRoomReservation;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.datepicker.DatePicker;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import filiciak.cyran.demo.Controllers.ConferenceRoomReservedController;
import filiciak.cyran.demo.Controllers.UserController;
import filiciak.cyran.demo.Entities.*;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.UI.views.MainLayout;
import filiciak.cyran.demo.UI.views.login.LoginView;
import filiciak.cyran.demo.UI.views.makeSeatReservation.MakeSeatReservationView;
import filiciak.cyran.demo.UI.views.yourReservations.YourReservationsView;

@PageTitle("Reserve Room")
@Route(value = "reserve-room", layout = MainLayout.class)
public class ChooseRoomReservationDatesView extends Div implements AfterNavigationObserver {

    private DatePicker fromDate = new DatePicker();
    private DatePicker toDate = new DatePicker();
    private Button reserve = new Button("Reserve");
    private Button cancel = new Button("Cancel");

    ConferenceRoom conferenceRoom;
    ConferenceRoomReservedController conferenceRoomReservedController;
    UserController userController;

    public ChooseRoomReservationDatesView(ConferenceRoomReservedController conferenceRoomReservedController, UserController userController) {
        this.conferenceRoomReservedController = conferenceRoomReservedController;
        this.userController = userController;
        if(ComponentUtil.getData(UI.getCurrent(), ConferenceRoom.class) == null) {
            UI.getCurrent().navigate(MakeSeatReservationView.class);
        }
        conferenceRoom = ComponentUtil.getData(UI.getCurrent(), ConferenceRoom.class);

        addClassName("delete-seat-from-office");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        cancel.addClickListener(e -> UI.getCurrent().navigate(MakeSeatReservationView.class));
        reserve.addClickListener(e -> {
            ConferenceRoomReserved conferenceRoomReserved = new ConferenceRoomReserved();
            conferenceRoomReserved.setConferenceRoom(conferenceRoom);
            conferenceRoomReserved.setFromDate(fromDate.getValue());
            conferenceRoomReserved.setToDate(toDate.getValue());
            conferenceRoomReserved.setStatus(ReservationStatus.ACTIVE);
            conferenceRoomReserved.setUser(userController.getUser(UserInstance.getInstance().getUsername()));

            try {
                conferenceRoomReservedController.createReservation(conferenceRoomReserved);
            } catch (BadRequestException ex) {
                throw new RuntimeException(ex);
            }

            UI.getCurrent().navigate(YourReservationsView.class);
        });
    }

    private Component createTitle() {
        return new H3("Make Reservation");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();

        fromDate.setWidth("120px");
        fromDate.setLabel("Starting date");

        toDate.setWidth("120px");
        toDate.setLabel("Ending date");

        formLayout.add(fromDate, toDate);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        reserve.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(reserve);
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
