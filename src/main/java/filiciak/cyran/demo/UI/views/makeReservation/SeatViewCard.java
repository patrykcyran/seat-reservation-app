package filiciak.cyran.demo.UI.views.makeReservation;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.theme.lumo.LumoUtility;
import filiciak.cyran.demo.Controllers.SeatController;
import filiciak.cyran.demo.Entities.AvailabilityStatus;
import filiciak.cyran.demo.Entities.Seat;

public class SeatViewCard extends ListItem {

    public SeatViewCard(Seat seat) {
        if (!seat.getStatus().equals(AvailabilityStatus.FREE)) {
            return;
        }

        addClassNames(LumoUtility.Background.CONTRAST_5, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.AlignItems.START, LumoUtility.Padding.MEDIUM,
                LumoUtility.BorderRadius.LARGE);

        Div div = new Div();
        div.addClassNames(LumoUtility.Background.CONTRAST, LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.CENTER,
                LumoUtility.Margin.Bottom.MEDIUM, LumoUtility.Overflow.HIDDEN, LumoUtility.BorderRadius.MEDIUM, LumoUtility.Width.FULL);
        div.setHeight("60px");
        div.setWidth("40px");

        Span header = new Span();
        header.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.FontWeight.SEMIBOLD);
        header.setText("Seat number: " + seat.getSeatNumber());

        //TODO: Add equipment for seat

        Button button = new Button();
        button.addClassNames(LumoUtility.AlignItems.END);
        button.setText("Reserve");

        Div div2 = new Div();
        div.setHeight("20px");
        div.setWidth("10px");

        Div div3 = new Div();
        div.setHeight("20px");
        div.setWidth("10px");

        button.addClickListener(e -> reserveSeat(seat));

        add(div, header, div2, button);
    }

    private void reserveSeat(Seat seat) {

    }
}
