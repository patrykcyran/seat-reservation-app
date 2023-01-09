package filiciak.cyran.demo.UI.views;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.applayout.AppLayout;
import com.vaadin.flow.component.applayout.DrawerToggle;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.html.Footer;
import com.vaadin.flow.component.html.H1;
import com.vaadin.flow.component.html.H2;
import com.vaadin.flow.component.html.Header;
import com.vaadin.flow.component.orderedlayout.Scroller;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.theme.lumo.LumoUtility;
import filiciak.cyran.demo.Entities.User;
import filiciak.cyran.demo.Entities.UserInstance;
import filiciak.cyran.demo.UI.components.AppNav;
import filiciak.cyran.demo.UI.components.AppNavItem;
import filiciak.cyran.demo.UI.views.adminViews.equipment.ManageEquipmentView;
import filiciak.cyran.demo.UI.views.adminViews.office.ManageOfficeView;
import filiciak.cyran.demo.UI.views.adminViews.room.ManageRoomView;
import filiciak.cyran.demo.UI.views.adminViews.seat.ManageSeatView;
import filiciak.cyran.demo.UI.views.login.LoginView;
import filiciak.cyran.demo.UI.views.makeRoomReservation.MakeRoomReservationView;
import filiciak.cyran.demo.UI.views.makeSeatReservation.MakeSeatReservationView;
import filiciak.cyran.demo.UI.views.yourReservations.YourReservationsView;

public class MainLayout extends AppLayout {

    private H2 viewTitle;

    public MainLayout() {
        setPrimarySection(Section.DRAWER);
        addDrawerContent();
        addHeaderContent();
    }

    private void addHeaderContent() {
        DrawerToggle toggle = new DrawerToggle();
        toggle.getElement().setAttribute("aria-label", "Menu toggle");

        viewTitle = new H2();
        viewTitle.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);

        addToNavbar(true, toggle, viewTitle);
    }

    private void addDrawerContent() {
        H1 appName = new H1("Seat Reservation App");
        appName.addClassNames(LumoUtility.FontSize.LARGE, LumoUtility.Margin.NONE);
        Header header = new Header(appName);

        Scroller scroller = new Scroller(createNavigation());

        Button logOutButton = new Button();
        logOutButton.addClassNames(LumoUtility.AlignItems.CENTER);
        logOutButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        logOutButton.setText("Log out");

        logOutButton.addClickListener(e -> {
            UserInstance.logOut();
            UI.getCurrent().navigate(LoginView.class);
        });

        addToDrawer(header, scroller, logOutButton, createFooter());
    }

    private AppNav createNavigation() {
        AppNav nav = new AppNav();

        if (!UserInstance.getInstance().isAdmin()) {
            nav.addItem(new AppNavItem("Your Reservations", YourReservationsView.class, "la la-list"));
            nav.addItem(new AppNavItem("Make Seat Reservation", MakeSeatReservationView.class, "la la-laptop"));
            nav.addItem(new AppNavItem("Make Room Reservation", MakeRoomReservationView.class, "la la-laptop"));
        } else {
            nav.addItem(new AppNavItem("Manage Seats", ManageSeatView.class, "la la-list"));
            nav.addItem(new AppNavItem("Manage Rooms", ManageRoomView.class, "la la-list"));
            nav.addItem(new AppNavItem("Manage Offices", ManageOfficeView.class, "la la-list"));
            nav.addItem(new AppNavItem("Manage Equipment", ManageEquipmentView.class, "la la-list"));
        }
        return nav;
    }

    private Footer createFooter() {

        Footer layout = new Footer();

        return layout;
    }

    @Override
    protected void afterNavigation() {
        super.afterNavigation();
        if (!UserInstance.getInstance().isLogged()) {
            UI.getCurrent().close();
            UI.getCurrent().navigate(LoginView.class);
        }
        viewTitle.setText(getCurrentPageTitle());
    }

    private String getCurrentPageTitle() {
        PageTitle title = getContent().getClass().getAnnotation(PageTitle.class);
        return title == null ? "" : title.value();
    }
}