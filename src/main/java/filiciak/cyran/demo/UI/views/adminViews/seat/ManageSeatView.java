package filiciak.cyran.demo.UI.views.adminViews.seat;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
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
import com.vaadin.flow.theme.lumo.LumoUtility;
import filiciak.cyran.demo.Controllers.SeatController;
import filiciak.cyran.demo.Entities.Office;
import filiciak.cyran.demo.Entities.Seat;
import filiciak.cyran.demo.Entities.UserInstance;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.UI.views.MainLayout;
import filiciak.cyran.demo.UI.views.login.LoginView;

@PageTitle("Manage Seats")
@Route(value = "manage-seat-view", layout = MainLayout.class)
public class ManageSeatView extends Div implements AfterNavigationObserver, HasComponents, HasStyle {
    Grid<Seat> grid = new Grid<>();
    SeatController seatController;
    private OrderedList seatsContainer;

    public ManageSeatView(SeatController seatController) {
        this.seatController = seatController;

        constructUI();

        seatController.all().forEach(seat -> {
            try {
                seatsContainer.add(new ManageSeatViewCard(seat, seatController));
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void constructUI() {
        addClassNames("make-room-reservation-view");
        addClassNames(LumoUtility.MaxWidth.SCREEN_LARGE, LumoUtility.Margin.Horizontal.AUTO, LumoUtility.Padding.Bottom.LARGE, LumoUtility.Padding.Horizontal.LARGE);

        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.BETWEEN);

        VerticalLayout headerContainer = new VerticalLayout();
        H2 header = new H2("Seats");
        header.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.NONE, LumoUtility.FontSize.XXXLARGE);
        Paragraph description = new Paragraph("Choose seat to manage");
        description.addClassNames(LumoUtility.Margin.Bottom.XLARGE, LumoUtility.Margin.Top.NONE, LumoUtility.TextColor.SECONDARY);
        headerContainer.add(header, description);

        Button button = new Button();
        button.addClassNames(LumoUtility.AlignItems.START);
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.setText("Add Seat");

        button.addClickListener(e -> {UI.getCurrent().navigate(AddSeatView.class);});

        seatsContainer = new OrderedList();
        seatsContainer.addClassNames(LumoUtility.Gap.MEDIUM, LumoUtility.Display.GRID, LumoUtility.ListStyleType.NONE, LumoUtility.Margin.LARGE, LumoUtility.Padding.NONE);

        container.add(headerContainer);
        add(container, button, seatsContainer);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if (!UserInstance.getInstance().isLogged() && !UserInstance.getInstance().isAdmin()) {
            UI.getCurrent().navigate(LoginView.class);
        }

        grid.setItems(seatController.all());
    }
}
