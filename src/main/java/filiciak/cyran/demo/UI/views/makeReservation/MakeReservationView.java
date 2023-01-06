package filiciak.cyran.demo.UI.views.makeReservation;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import filiciak.cyran.demo.Entities.User;
import filiciak.cyran.demo.UI.views.MainLayout;
import filiciak.cyran.demo.UI.views.login.LoginView;

import java.util.List;

@PageTitle("Make Reservation")
@Route(value = "make-reservation", layout = MainLayout.class)
public class MakeReservationView extends Div implements AfterNavigationObserver {

    Grid<String> grid = new Grid<>();

    public MakeReservationView() {
        addClassName("make-reservation-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        add(grid);
    }


    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if (!User.getInstance().isLogged()) {
            UI.getCurrent().navigate(LoginView.class);
        }
        grid.setItems(List.of());
    }


}
