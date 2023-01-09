package filiciak.cyran.demo.UI.views.adminViews.room;

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
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import filiciak.cyran.demo.Controllers.ConferenceRoomController;
import filiciak.cyran.demo.Entities.ConferenceRoom;
import filiciak.cyran.demo.Entities.UserInstance;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.UI.views.MainLayout;
import filiciak.cyran.demo.UI.views.login.LoginView;

@PageTitle("Manage Rooms")
@Route(value = "manage-room-view", layout = MainLayout.class)
public class ManageRoomView extends Div implements AfterNavigationObserver, HasComponents, HasStyle {
    Grid<ConferenceRoom> grid = new Grid<>();
    ConferenceRoomController conferenceRoomController;
    private OrderedList roomsContainer;

    public ManageRoomView(ConferenceRoomController conferenceRoomController) {
        this.conferenceRoomController = conferenceRoomController;

        constructUI();

        conferenceRoomController.all().forEach(conferenceRoom -> {
            try {
                roomsContainer.add(new ManageRoomViewCard(conferenceRoom, conferenceRoomController));
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void constructUI() {
        addClassNames("manage-room-view");
        addClassNames(LumoUtility.MaxWidth.SCREEN_LARGE, LumoUtility.Margin.Horizontal.AUTO, LumoUtility.Padding.Bottom.LARGE, LumoUtility.Padding.Horizontal.LARGE);

        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.BETWEEN);

        VerticalLayout headerContainer = new VerticalLayout();
        H2 header = new H2("Rooms");
        header.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.NONE, LumoUtility.FontSize.XXXLARGE);
        Paragraph description = new Paragraph("Choose room to manage");
        description.addClassNames(LumoUtility.Margin.Bottom.XLARGE, LumoUtility.Margin.Top.NONE, LumoUtility.TextColor.SECONDARY);
        headerContainer.add(header, description);

        Button button = new Button();
        button.addClassNames(LumoUtility.AlignItems.START);
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.setText("Add Room");

        button.addClickListener(e -> {UI.getCurrent().navigate(AddRoomView.class);});

        roomsContainer = new OrderedList();
        roomsContainer.addClassNames(LumoUtility.Gap.MEDIUM, LumoUtility.Display.GRID, LumoUtility.ListStyleType.NONE, LumoUtility.Margin.LARGE, LumoUtility.Padding.NONE);

        container.add(headerContainer);
        add(container, button, roomsContainer);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if (!UserInstance.getInstance().isLogged() && !UserInstance.getInstance().isAdmin()) {
            UI.getCurrent().navigate(LoginView.class);
        }

        grid.setItems(conferenceRoomController.all());
    }
}
