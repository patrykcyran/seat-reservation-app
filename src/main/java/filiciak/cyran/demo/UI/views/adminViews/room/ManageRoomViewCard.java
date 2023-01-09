package filiciak.cyran.demo.UI.views.adminViews.room;

import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.HasComponents;
import com.vaadin.flow.component.HasStyle;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.grid.Grid;
import com.vaadin.flow.component.html.*;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.orderedlayout.VerticalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import com.vaadin.flow.theme.lumo.LumoUtility;
import filiciak.cyran.demo.Controllers.ConferenceRoomController;
import filiciak.cyran.demo.Entities.AvailabilityStatus;
import filiciak.cyran.demo.Entities.ConferenceRoom;
import filiciak.cyran.demo.Entities.UserInstance;
import filiciak.cyran.demo.Exceptions.BadRequestException;

import java.util.List;

public class ManageRoomViewCard extends ListItem {

    ConferenceRoomController conferenceRoomController;

    public ManageRoomViewCard(ConferenceRoom conferenceRoom, ConferenceRoomController conferenceRoomController) throws BadRequestException {
        this.conferenceRoomController = conferenceRoomController;

        addClassNames(LumoUtility.Background.CONTRAST_5, LumoUtility.Display.FLEX, LumoUtility.FlexDirection.COLUMN, LumoUtility.AlignItems.START, LumoUtility.Padding.MEDIUM,
                LumoUtility.BorderRadius.LARGE);

        Div div = new Div();
        div.addClassNames(LumoUtility.Background.CONTRAST, LumoUtility.Display.FLEX, LumoUtility.AlignItems.CENTER, LumoUtility.JustifyContent.CENTER,
                LumoUtility.Margin.Bottom.MEDIUM, LumoUtility.Overflow.HIDDEN, LumoUtility.BorderRadius.MEDIUM, LumoUtility.Width.FULL);
        div.setHeight("60px");
        div.setWidth("40px");

        Span header = new Span();
        header.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.FontWeight.SEMIBOLD);
        header.setText("Room name: " + conferenceRoom.getName());

        Span status = new Span();
        status.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.FontWeight.SEMIBOLD);
        status.setText("Status " + conferenceRoom.getStatus().toString());

        Span equipment = new Span();
        equipment.addClassNames(LumoUtility.FontSize.XLARGE, LumoUtility.FontWeight.SEMIBOLD);
        List<String> equipmentList = conferenceRoomController.getEquipment(conferenceRoom.getId());
        StringBuilder stringBuilder = new StringBuilder("Equipment: \n");
        equipmentList.forEach(s -> stringBuilder.append(s).append("\n"));
        equipment.setText("\n" + stringBuilder);

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
            ComponentUtil.setData(UI.getCurrent(), ConferenceRoom.class, conferenceRoom);
            UI.getCurrent().navigate(ChangeSingleRoomView.class);
        });

        add(div, header, div2, status, div3, equipment, div4, button);
    }
}
