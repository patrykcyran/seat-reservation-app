package filiciak.cyran.demo.UI.views.makeSeatReservation;

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
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import filiciak.cyran.demo.Controllers.OfficeController;
import filiciak.cyran.demo.Controllers.SeatController;
import filiciak.cyran.demo.Entities.Office;
import filiciak.cyran.demo.Entities.User;
import filiciak.cyran.demo.Entities.UserInstance;
import filiciak.cyran.demo.UI.views.MainLayout;
import filiciak.cyran.demo.UI.views.login.LoginView;

@PageTitle("Make Seat Reservation")
@Route(value = "make-seat-reservation", layout = MainLayout.class)
public class MakeSeatReservationView extends Div implements AfterNavigationObserver, HasComponents, HasStyle {

    Grid<Office> grid = new Grid<>();
    OfficeController officeController;
    SeatController seatController;
    private OrderedList officeContainer;

    public MakeSeatReservationView(OfficeController officeController, SeatController seatController) {
        this.officeController = officeController;
        this.seatController = seatController;

        constructUI();

        officeController.all().forEach(office -> officeContainer.add(new OfficeWithSeatsViewCard(office, seatController)));
    }

    private void constructUI() {
        addClassNames("make-seat-reservation-view");
        addClassNames(MaxWidth.SCREEN_LARGE, Margin.Horizontal.AUTO, Padding.Bottom.LARGE, Padding.Horizontal.LARGE);

        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames(AlignItems.CENTER, JustifyContent.BETWEEN);

        VerticalLayout headerContainer = new VerticalLayout();
        H2 header = new H2("Offices");
        header.addClassNames(Margin.Bottom.NONE, Margin.Top.NONE, FontSize.XXXLARGE);
        Paragraph description = new Paragraph("Choose office in which you want to make reservation");
        description.addClassNames(Margin.Bottom.XLARGE, Margin.Top.NONE, TextColor.SECONDARY);
        headerContainer.add(header, description);

        Select<String> sortBy = new Select<>();
        sortBy.setLabel("Sort by");
        sortBy.setItems("Name", "Localization");
        sortBy.setValue("Name");

        officeContainer = new OrderedList();
        officeContainer.addClassNames(Gap.MEDIUM, Display.GRID, ListStyleType.NONE, Margin.LARGE, Padding.NONE);

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

}
