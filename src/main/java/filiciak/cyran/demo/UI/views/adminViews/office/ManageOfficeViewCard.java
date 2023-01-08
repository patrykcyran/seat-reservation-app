package filiciak.cyran.demo.UI.views.adminViews.office;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.theme.lumo.LumoUtility;
import filiciak.cyran.demo.Controllers.OfficeController;
import filiciak.cyran.demo.Entities.Office;
import filiciak.cyran.demo.Exceptions.BadRequestException;

import java.util.List;

public class ManageOfficeViewCard extends ListItem {

    OfficeController officeController;

    public ManageOfficeViewCard(Office office, OfficeController officeController) throws BadRequestException {
        this.officeController = officeController;

        addClassNames(LumoUtility.Background.CONTRAST_5, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.AlignItems.START, LumoUtility.Padding.MEDIUM,
                LumoUtility.BorderRadius.LARGE);

        Div div = new Div();
        div.addClassNames(LumoUtility.Background.CONTRAST, LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.CENTER,
                LumoUtility.Margin.Bottom.MEDIUM, LumoUtility.Overflow.HIDDEN, LumoUtility.BorderRadius.MEDIUM, LumoUtility.Width.FULL);
        div.setHeight("60px");
        div.setWidth("40px");

        Span header = new Span();
        header.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.FontWeight.SEMIBOLD);
        header.setText("Office name: " + office.getName());


        Button button = new Button();
        button.addClassNames(LumoUtility.AlignItems.END);
        button.setText("Manage");

        Div div2 = new Div();
        div2.setHeight("20px");
        div2.setWidth("10px");

        Div div3 = new Div();
        div3.setHeight("20px");
        div3.setWidth("10px");

        Div div4 = new Div();
        div4.setHeight("20px");
        div4.setWidth("10px");


        button.addClickListener(e -> {
            ComponentUtil.setData(UI.getCurrent(), Office.class, office);
            UI.getCurrent().navigate(ChangeSingleOfficeView.class);
        });

        add(div, header, div4, button);
    }
}