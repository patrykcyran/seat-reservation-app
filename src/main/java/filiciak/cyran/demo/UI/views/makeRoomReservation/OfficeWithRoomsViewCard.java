package filiciak.cyran.demo.UI.views.makeRoomReservation;

import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.OrderedList;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.theme.lumo.LumoUtility;
import filiciak.cyran.demo.Controllers.ConferenceRoomController;
import filiciak.cyran.demo.Entities.AvailabilityStatus;
import filiciak.cyran.demo.Entities.ConferenceRoom;
import filiciak.cyran.demo.Entities.Office;
import filiciak.cyran.demo.Exceptions.BadRequestException;

import java.util.List;

public class OfficeWithRoomsViewCard extends ListItem {

    OrderedList roomsContainer;
    ConferenceRoomController conferenceRoomController;
    boolean isShowed = false;

    public OfficeWithRoomsViewCard(Office office, ConferenceRoomController conferenceRoomController) {
        this.conferenceRoomController = conferenceRoomController;
        roomsContainer = new OrderedList();
        roomsContainer.addClassNames(LumoUtility.Gap.MEDIUM, LumoUtility.Display.GRID, LumoUtility.ListStyleType.NONE, LumoUtility.Margin.LARGE, LumoUtility.Padding.NONE);

        addClassNames(LumoUtility.Background.CONTRAST_5, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.AlignItems.START, LumoUtility.Padding.MEDIUM,
                LumoUtility.BorderRadius.LARGE);

        Div div = new Div();
        div.addClassNames(LumoUtility.Background.CONTRAST, LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.CENTER,
                LumoUtility.Margin.Bottom.MEDIUM, LumoUtility.Overflow.HIDDEN, LumoUtility.BorderRadius.MEDIUM, LumoUtility.Width.FULL);
        div.setHeight("60px");
        div.setWidth("40px");

        Span header = new Span();
        header.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.FontWeight.SEMIBOLD);
        header.setText(office.getName());

        Span address = new Span();
        address.addClassNames(LumoUtility.FontSize.SMALL, LumoUtility.TextColor.SECONDARY);
        address.setText(office.getAddress().getStreet());

        Button button = new Button();
        button.addClassNames(LumoUtility.AlignItems.END);
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
                hideRooms();
            } else {
                showRooms(office);
            }
        });

        add(div, header, div2, address, div3, button, div4, roomsContainer);
    }

    private void showRooms(Office office) {
        conferenceRoomController.allFreeFromOffice(office.getId())
                .forEach(room -> {
                    try {
                        roomsContainer.add(new RoomViewCard(room, conferenceRoomController));
                    } catch (BadRequestException e) {
                        throw new RuntimeException(e);
                    }
                });
        isShowed = true;
    }

    private void hideRooms() {
        roomsContainer.removeAll();
        isShowed = false;
    }
}