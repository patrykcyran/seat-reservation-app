package filiciak.cyran.demo.UI.views.yourReservations;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.icon.VaadinIcon;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.router.RouteAlias;
import com.vaadin.flow.theme.lumo.LumoUtility;
import filiciak.cyran.demo.Controllers.ConferenceRoomReservedController;
import filiciak.cyran.demo.Controllers.OfficeController;
import filiciak.cyran.demo.Controllers.SeatController;
import filiciak.cyran.demo.Controllers.SeatReservedController;
import filiciak.cyran.demo.Entities.*;
import filiciak.cyran.demo.UI.views.MainLayout;
import filiciak.cyran.demo.UI.views.login.LoginView;
import filiciak.cyran.demo.UI.views.makeSeatReservation.OfficeWithSeatsViewCard;

import java.util.Arrays;
import java.util.List;

@PageTitle("Your Reservations")
@Route(value = "ongoing-reservations", layout = MainLayout.class)
@RouteAlias(value = "", layout = MainLayout.class)
public class YourReservationsView extends Div implements AfterNavigationObserver {

    Grid<SeatReserved> grid1 = new Grid<>();
    Grid<ConferenceRoomReserved> grid2 = new Grid<>();
    SeatReservedController seatReservedController;
    ConferenceRoomReservedController conferenceRoomReservedController;
    private OrderedList seatsContainer;
    private OrderedList roomsContainer;

    public YourReservationsView(SeatReservedController seatReservedController, ConferenceRoomReservedController conferenceRoomReservedController) {
        this.seatReservedController = seatReservedController;
        this.conferenceRoomReservedController = conferenceRoomReservedController;

        constructUI();

        seatReservedController.allForUser().stream()
                .filter(seat -> seat.getStatus().equals(ReservationStatus.ACTIVE))
                .forEach(seat -> seatsContainer.add(new SeatReservedViewCard(seat, seatReservedController)));
        conferenceRoomReservedController.allForUser().stream()
                .filter(room -> room.getStatus().equals(ReservationStatus.ACTIVE))
                .forEach(room -> roomsContainer.add(new RoomReservedViewCard(room, conferenceRoomReservedController)));
    }

    private void constructUI() {
        addClassNames("make-seat-reservation-view");
        addClassNames(LumoUtility.MaxWidth.SCREEN_LARGE, LumoUtility.Margin.Horizontal.AUTO, LumoUtility.Padding.Bottom.LARGE, LumoUtility.Padding.Horizontal.LARGE);

        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.BETWEEN);

        VerticalLayout headerContainer = new VerticalLayout();
        H2 header = new H2("Seat Reservations");
        header.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.NONE, LumoUtility.FontSize.XXXLARGE);
        headerContainer.add(header);


        seatsContainer = new OrderedList();
        seatsContainer.addClassNames(LumoUtility.Gap.MEDIUM, LumoUtility.Display.GRID, LumoUtility.ListStyleType.NONE, LumoUtility.Margin.LARGE, LumoUtility.Padding.NONE);

        HorizontalLayout container2 = new HorizontalLayout();
        container2.addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.BETWEEN);

        VerticalLayout headerContainer2 = new VerticalLayout();
        H2 header2 = new H2("Room Reservations");
        header2.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.NONE, LumoUtility.FontSize.XXXLARGE);
        headerContainer2.add(header2);


        roomsContainer = new OrderedList();
        roomsContainer.addClassNames(LumoUtility.Gap.MEDIUM, LumoUtility.Display.GRID, LumoUtility.ListStyleType.NONE, LumoUtility.Margin.LARGE, LumoUtility.Padding.NONE);

        container.add(headerContainer);
        container2.add(headerContainer2);
        add(container, seatsContainer,container2, roomsContainer);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if (!UserInstance.getInstance().isLogged()) {
            UI.getCurrent().navigate(LoginView.class);
        }

        grid1.setItems(seatReservedController.allForUser());
        grid1.setItems(seatReservedController.allForUser());
    }

}
