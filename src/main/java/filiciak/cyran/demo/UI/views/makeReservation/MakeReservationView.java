package filiciak.cyran.demo.UI.views.makeReservation;

import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.grid.GridVariant;
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
import filiciak.cyran.demo.Entities.Office;
import filiciak.cyran.demo.Entities.User;
import filiciak.cyran.demo.UI.views.MainLayout;
import filiciak.cyran.demo.UI.views.login.LoginView;

import java.util.List;

@PageTitle("Make Reservation")
@Route(value = "make-reservation", layout = MainLayout.class)
public class MakeReservationView extends Div implements AfterNavigationObserver, HasComponents, HasStyle {

    Grid<Office> grid = new Grid<>();
    OfficeController officeController;
    private OrderedList officeContainer;

    public MakeReservationView(OfficeController officeController) {
        this.officeController = officeController;

        constructUI();

        officeController.all().forEach(office -> officeContainer.add(new OfficeViewCard(office)));

/*        addClassName("make-reservation-view");
        setSizeFull();
        grid.setHeight("100%");
        grid.addThemeVariants(GridVariant.LUMO_NO_BORDER, GridVariant.LUMO_NO_ROW_BORDERS);
        grid.addComponentColumn(office -> createCard(office));
        add(grid);*/
    }

    private void constructUI() {
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
        officeContainer.addClassNames(Gap.MEDIUM, Display.GRID, ListStyleType.NONE, Margin.NONE, Padding.NONE);

        container.add(headerContainer, sortBy);
        add(container, officeContainer);
    }

    private HorizontalLayout createCard(Office office) {
        HorizontalLayout card = new HorizontalLayout();
        card.addClassName("card");
        card.setSpacing(false);
        card.getThemeList().add("spacing-s");

        HorizontalLayout header = new HorizontalLayout();
        header.addClassName("header");
        header.setSpacing(false);
        header.getThemeList().add("spacing-s");

        card.add(office.getName());
        return card;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if (!User.getInstance().isLogged()) {
            UI.getCurrent().navigate(LoginView.class);
        }

        grid.setItems(officeController.all());
    }

}
