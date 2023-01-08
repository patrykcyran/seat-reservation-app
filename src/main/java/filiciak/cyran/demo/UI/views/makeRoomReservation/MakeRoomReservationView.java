package filiciak.cyran.demo.UI.views.makeRoomReservation;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.html.Paragraph;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.component.select.Select;
import com.vaadin.flow.router.*;
import com.vaadin.flow.theme.lumo.LumoUtility;
import filiciak.cyran.demo.Controllers.ConferenceRoomController;
import filiciak.cyran.demo.Controllers.OfficeController;
import filiciak.cyran.demo.Entities.Office;
import filiciak.cyran.demo.Entities.User;
import filiciak.cyran.demo.Entities.UserInstance;
import filiciak.cyran.demo.UI.views.MainLayout;
import filiciak.cyran.demo.UI.views.login.LoginView;

@PageTitle("Make Room Reservation")
@Route(value = "make-room-reservation", layout = MainLayout.class)
public class MakeRoomReservationView extends Div implements AfterNavigationObserver, HasComponents, HasStyle, BeforeEnterObserver {


    Grid<Office> grid = new Grid<>();
    OfficeController officeController;
    ConferenceRoomController conferenceRoomController;
    private OrderedList officeContainer;

    public MakeRoomReservationView(OfficeController officeController, ConferenceRoomController conferenceRoomController) {
        this.officeController = officeController;
        this.conferenceRoomController = conferenceRoomController;

        constructUI();

        officeController.all().forEach(office -> officeContainer.add(new OfficeWithRoomsViewCard(office, conferenceRoomController)));
    }

    private void constructUI() {
        addClassNames("make-room-reservation-view");
        addClassNames(LumoUtility.MaxWidth.SCREEN_LARGE, LumoUtility.Margin.Horizontal.AUTO, LumoUtility.Padding.Bottom.LARGE, LumoUtility.Padding.Horizontal.LARGE);

        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.BETWEEN);

        VerticalLayout headerContainer = new VerticalLayout();
        H2 header = new H2("Offices");
        header.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.NONE, LumoUtility.FontSize.XXXLARGE);
        Paragraph description = new Paragraph("Choose office in which you want to make reservation");
        description.addClassNames(LumoUtility.Margin.Bottom.XLARGE, LumoUtility.Margin.Top.NONE, LumoUtility.TextColor.SECONDARY);
        headerContainer.add(header, description);

        Select<String> sortBy = new Select<>();
        sortBy.setLabel("Sort by");
        sortBy.setItems("Name", "Localization");
        sortBy.setValue("Name");

        officeContainer = new OrderedList();
        officeContainer.addClassNames(LumoUtility.Gap.MEDIUM, LumoUtility.Display.GRID, LumoUtility.ListStyleType.NONE, LumoUtility.Margin.LARGE, LumoUtility.Padding.NONE);

        container.add(headerContainer);
        add(container, officeContainer);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if (!UserInstance.getInstance().isLogged()) {
            UI.getCurrent().navigate(LoginView.class);
        }

        grid.setItems(officeController.all());
    }

    @Override
    public void beforeEnter(BeforeEnterEvent beforeEnterEvent) {
        if (!UserInstance.getInstance().isLogged()) {
            UI.getCurrent().navigate(LoginView.class);
        }
    }
}
