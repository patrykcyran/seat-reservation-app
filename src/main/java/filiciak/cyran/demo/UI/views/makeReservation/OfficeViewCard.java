package filiciak.cyran.demo.UI.views.makeReservation;

import com.vaadin.flow.component.html.*;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import filiciak.cyran.demo.Entities.Office;

public class OfficeViewCard extends ListItem {




    public OfficeViewCard(Office office) {
        addClassNames(Background.CONTRAST_5, Display.FLEX, FlexDirection.COLUMN, AlignItems.START, Padding.MEDIUM,
                BorderRadius.LARGE);

        Div div = new Div();
        div.addClassNames(Background.CONTRAST, Display.FLEX, AlignItems.CENTER, JustifyContent.CENTER,
                Margin.Bottom.MEDIUM, Overflow.HIDDEN, BorderRadius.MEDIUM, Width.FULL);
        div.setHeight("160px");

        Span header = new Span();
        header.addClassNames(FontSize.XLARGE, FontWeight.SEMIBOLD);
        header.setText(office.getName());

        Span address = new Span();
        address.addClassNames(FontSize.SMALL, TextColor.SECONDARY);
        address.setText(office.getAddress().toString());


        add(div, header, address);
    }
}
