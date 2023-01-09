package filiciak.cyran.demo.UI.views.adminViews.equipment;

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
import filiciak.cyran.demo.Controllers.EquipmentController;
import filiciak.cyran.demo.Entities.Equipment;
import filiciak.cyran.demo.Entities.UserInstance;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.UI.views.MainLayout;
import filiciak.cyran.demo.UI.views.login.LoginView;

@PageTitle("Manage Equipment")
@Route(value = "manage-equipment-view", layout = MainLayout.class)
public class ManageEquipmentView extends Div implements AfterNavigationObserver, HasComponents, HasStyle {
    Grid<Equipment> grid = new Grid<>();
    EquipmentController equipmentController;
    private OrderedList equipmentContainer;

    public ManageEquipmentView(EquipmentController equipmentController) {
        this.equipmentController = equipmentController;

        constructUI();

        equipmentController.all().forEach(equipment -> {
            try {
                equipmentContainer.add(new ManageEquipmentViewCard(equipment, equipmentController));
            } catch (BadRequestException e) {
                throw new RuntimeException(e);
            }
        });
    }

    private void constructUI() {
        addClassNames("manage-equipment-view");
        addClassNames(LumoUtility.MaxWidth.SCREEN_LARGE, LumoUtility.Margin.Horizontal.AUTO, LumoUtility.Padding.Bottom.LARGE, LumoUtility.Padding.Horizontal.LARGE);

        HorizontalLayout container = new HorizontalLayout();
        container.addClassNames(LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.BETWEEN);

        VerticalLayout headerContainer = new VerticalLayout();
        H2 header = new H2("Equipment");
        header.addClassNames(LumoUtility.Margin.Bottom.NONE, LumoUtility.Margin.Top.NONE, LumoUtility.FontSize.XXXLARGE);
        Paragraph description = new Paragraph("Choose equipment to manage");
        description.addClassNames(LumoUtility.Margin.Bottom.XLARGE, LumoUtility.Margin.Top.NONE, LumoUtility.TextColor.SECONDARY);
        headerContainer.add(header, description);

        Button button = new Button();
        button.addClassNames(LumoUtility.AlignItems.START);
        button.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        button.setText("Add Equipment");

        button.addClickListener(e -> {
            UI.getCurrent().navigate(AddEquipmentView.class);});

        equipmentContainer = new OrderedList();
        equipmentContainer.addClassNames(LumoUtility.Gap.MEDIUM, LumoUtility.Display.GRID, LumoUtility.ListStyleType.NONE, LumoUtility.Margin.LARGE, LumoUtility.Padding.NONE);

        container.add(headerContainer);
        add(container, button, equipmentContainer);
    }

    @Override
    public void afterNavigation(AfterNavigationEvent event) {
        if (!UserInstance.getInstance().isLogged() && !UserInstance.getInstance().isAdmin()) {
            UI.getCurrent().navigate(LoginView.class);
        }

        grid.setItems(equipmentController.all());
    }
}
