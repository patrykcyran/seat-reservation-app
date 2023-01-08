package filiciak.cyran.demo.UI.views.makeSeatReservation;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.theme.lumo.LumoUtility.*;
import filiciak.cyran.demo.Controllers.SeatController;
import filiciak.cyran.demo.Entities.AvailabilityStatus;
import filiciak.cyran.demo.Entities.Office;
import filiciak.cyran.demo.Entities.Seat;
import filiciak.cyran.demo.Exceptions.BadRequestException;

import java.util.List;

public class OfficeWithSeatsViewCard extends ListItem {

    OrderedList seatsContainer;
    SeatController seatController;
    boolean isShowed = false;

    public OfficeWithSeatsViewCard(Office office, SeatController seatController) {
        this.seatController = seatController;
        seatsContainer = new OrderedList();
        seatsContainer.addClassNames(Gap.MEDIUM, Display.GRID, ListStyleType.NONE, Margin.LARGE, Padding.NONE);

        addClassNames(Background.CONTRAST_5, Display.FLEX, FlexDirection.COLUMN, AlignItems.START, Padding.MEDIUM,
                BorderRadius.LARGE);

        Div div = new Div();
        div.addClassNames(Background.CONTRAST, Display.FLEX, AlignItems.CENTER, JustifyContent.CENTER,
                Margin.Bottom.MEDIUM, Overflow.HIDDEN, BorderRadius.MEDIUM, Width.FULL);
        div.setHeight("60px");
        div.setWidth("40px");

        Span header = new Span();
        header.addClassNames(FontSize.XLARGE, FontWeight.SEMIBOLD);
        header.setText(office.getName());

        Span address = new Span();
        address.addClassNames(FontSize.SMALL, TextColor.SECONDARY);
        address.setText(office.getAddress().getStreet());

        Button button = new Button();
        button.addClassNames(AlignItems.END);
        button.setText("Choose");

        Div div2 = new Div();
        div.setHeight("20px");
        div.setWidth("10px");

        Div div3 = new Div();
        div.setHeight("20px");
        div.setWidth("10px");

        Div div4 = new Div();
        div.setHeight("20px");
        div.setWidth("10px");

        button.addClickListener(e -> {
            if (isShowed) {
                hideSeats();
            } else {
                showSeats(office);
            }
        });

        add(div, header, div2, address, div3, button, div4, seatsContainer);
    }

    private void showSeats(Office office) {
        seatController.allFreeFromOffice(office.getId())
                .forEach(seat -> {
                    try {
                        seatsContainer.add(new SeatViewCard(seat, seatController));
                    } catch (BadRequestException e) {
                        throw new RuntimeException(e);
                    }
                });
        isShowed = true;
    }

    private void hideSeats() {
        seatsContainer.removeAll();
        isShowed = false;
    }
}
