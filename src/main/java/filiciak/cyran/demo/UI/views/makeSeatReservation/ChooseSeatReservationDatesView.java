package filiciak.cyran.demo.UI.views.makeSeatReservation;

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
import filiciak.cyran.demo.Controllers.SeatReservedController;
import filiciak.cyran.demo.Controllers.UserController;
import filiciak.cyran.demo.Entities.ReservationStatus;
import filiciak.cyran.demo.Entities.Seat;
import filiciak.cyran.demo.Entities.SeatReserved;
import filiciak.cyran.demo.Entities.UserInstance;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.UI.views.MainLayout;
import filiciak.cyran.demo.UI.views.login.LoginView;
import filiciak.cyran.demo.UI.views.yourReservations.YourReservationsView;

@PageTitle("Reserve Seat")
@Route(value = "reserve-seat", layout = MainLayout.class)
public class ChooseSeatReservationDatesView extends Div implements AfterNavigationObserver {

    private DatePicker fromDate = new DatePicker();
    private DatePicker toDate = new DatePicker();
    private Button reserve = new Button("Reserve");
    private Button cancel = new Button("Cancel");

    Seat seat;
    SeatReservedController seatReservedController;
    UserController userController;

    public ChooseSeatReservationDatesView(SeatReservedController seatReservedController, UserController userController) {
        this.seatReservedController = seatReservedController;
        this.userController = userController;
        if(ComponentUtil.getData(UI.getCurrent(), Seat.class) == null) {
            UI.getCurrent().navigate(MakeSeatReservationView.class);
        }
        seat = ComponentUtil.getData(UI.getCurrent(), Seat.class);

        addClassName("delete-seat-from-office");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        cancel.addClickListener(e -> UI.getCurrent().navigate(MakeSeatReservationView.class));
        reserve.addClickListener(e -> {
            SeatReserved seatReserved = new SeatReserved();
            seatReserved.setSeat(seat);
            seatReserved.setFromDate(fromDate.getValue());
            seatReserved.setToDate(toDate.getValue());
            seatReserved.setStatus(ReservationStatus.ACTIVE);
            seatReserved.setUser(userController.getUser(UserInstance.getInstance().getUsername()));

            try {
                seatReservedController.createReservation(seatReserved);
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
