package filiciak.cyran.demo.UI.views.yourReservations;

import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.ListItem;
import com.vaadin.flow.component.html.Span;
import com.vaadin.flow.theme.lumo.LumoUtility;
import filiciak.cyran.demo.Controllers.ConferenceRoomReservedController;
import filiciak.cyran.demo.Controllers.SeatReservedController;
import filiciak.cyran.demo.Entities.ConferenceRoomReserved;
import filiciak.cyran.demo.Entities.ReservationStatus;
import filiciak.cyran.demo.Entities.SeatReserved;
import filiciak.cyran.demo.Exceptions.BadRequestException;

import java.util.ArrayList;
import java.util.List;

public class RoomReservedViewCard extends ListItem {

    ConferenceRoomReservedController conferenceRoomReservedController;

    public RoomReservedViewCard(ConferenceRoomReserved conferenceRoomReserved, ConferenceRoomReservedController conferenceRoomReservedController) {
        this.conferenceRoomReservedController = conferenceRoomReservedController;

        addClassNames(LumoUtility.Background.CONTRAST_5, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.AlignItems.START, LumoUtility.Padding.MEDIUM,
                LumoUtility.BorderRadius.LARGE);

        Div div = new Div();
        div.addClassNames(LumoUtility.Background.CONTRAST, LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.CENTER,
                LumoUtility.Margin.Bottom.MEDIUM, LumoUtility.Overflow.HIDDEN, LumoUtility.BorderRadius.MEDIUM, LumoUtility.Width.FULL);
        div.setHeight("60px");
        div.setWidth("40px");

        Span header = new Span();
        header.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.FontWeight.SEMIBOLD);
        header.setText("Room name: " + conferenceRoomReserved.getConferenceRoom().getName());

        Span equipment = new Span();
        equipment.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.FontWeight.SEMIBOLD);
        List<String> equipmentList = new ArrayList<>();
        conferenceRoomReserved.getConferenceRoom().getEquipments().forEach(e -> equipmentList.add(e.getName()));
        StringBuilder stringBuilder = new StringBuilder("Equipment: \n");
        equipmentList.forEach(s -> stringBuilder.append(s).append("\n"));
        equipment.setText("\n" + stringBuilder);

        Button button = new Button();
        button.addClassNames(LumoUtility.AlignItems.END);
        button.setText("Cancel");


        Span fromDate = new Span();
        fromDate.setText("From date: " + conferenceRoomReserved.getFromDate());

        Span toDate = new Span();
        toDate.setText("To date: " + conferenceRoomReserved.getToDate());

        Span status = new Span();
        status.setText("Status: " + conferenceRoomReserved.getStatus());

        Div div2 = new Div();
        div2.setHeight("20px");
        div2.setWidth("10px");

        Div div3 = new Div();
        div3.setHeight("20px");
        div3.setWidth("10px");

        Div div4 = new Div();
        div4.setHeight("20px");
        div4.setWidth("10px");

        Div div5 = new Div();
        div5.setHeight("20px");
        div5.setWidth("10px");

        Div div6 = new Div();
        div6.setHeight("20px");
        div6.setWidth("10px");

        button.addClickListener(e -> {
            try {
                this.conferenceRoomReservedController.cancelReservation(conferenceRoomReserved.getId());
                UI.getCurrent().getPage().reload();
            } catch (BadRequestException ex) {
                throw new RuntimeException(ex);
            }
        });

        add(div, header, div2, fromDate, div3, toDate, div4, status, div5, equipment, div6, button);
    }
}
