package filiciak.cyran.demo.UI.views.adminViews.office;

import com.vaadin.flow.component.*;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import filiciak.cyran.demo.Controllers.EquipmentController;
import filiciak.cyran.demo.Controllers.OfficeController;
import filiciak.cyran.demo.Entities.*;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.UI.views.MainLayout;
import filiciak.cyran.demo.UI.views.login.LoginView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@PageTitle("Manage Offices")
@Route(value = "manage-office-view", layout = MainLayout.class)
public class ManageOfficeView extends Div implements AfterNavigationObserver, HasComponents, HasStyle {
    Grid<Office> grid = new Grid<>();
    OfficeController officeController;
    private OrderedList officesContainer;

    public ManageOfficeView(OfficeController officeController) {
        this.officeController = officeController;

        constructUI();

        officeController.all().forEach(office -> {
            try {
                officesContainer.add(new ManageOfficeViewCard(office, officeController));
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void constructUI() {
        addClassNames("make-office-reservation-view");
        addClassNames(LumoUtility.MaxWidth.SCREEN_LARGE, LumoUtility.Margin.Horizontal.AUTO, LumoUtility.Padding.Bottom.LARGE, LumoUtility.Padding.Horizontal.LARGE);

        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.BETWEEN);

        VerticalLayout headerContainer = new VerticalLayout();
        H2 header = new H2("Offices");
        header.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.NONE, LumoUtility.FontSize.XXXLARGE);
        Paragraph description = new Paragraph("Choose office to manage");
        description.addClassNames(LumoUtility.Margin.Bottom.XLARGE, LumoUtility.Margin.Top.NONE, LumoUtility.TextColor.SECONDARY);
        headerContainer.add(header, description);

        Button button = new Button();
        button.addClassNames(LumoUtility.AlignItems.START);
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.setText("Add Office");

        button.addClickListener(e -> {UI.getCurrent().navigate(AddOfficeView.class);});

        officesContainer = new OrderedList();
        officesContainer.addClassNames(LumoUtility.Gap.MEDIUM, LumoUtility.Display.GRID, LumoUtility.ListStyleType.NONE, LumoUtility.Margin.LARGE, LumoUtility.Padding.NONE);

        container.add(headerContainer);
        add(container, button, officesContainer);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if (!UserInstance.getInstance().isLogged() && !UserInstance.getInstance().isAdmin()) {
            UI.getCurrent().navigate(LoginView.class);
        }

        grid.setItems(officeController.all());
    }
}
