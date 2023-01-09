package filiciak.cyran.demo.UI.views.adminViews.manageObjects;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import filiciak.cyran.demo.Controllers.ConferenceRoomController;
import filiciak.cyran.demo.Controllers.SeatController;
import filiciak.cyran.demo.Entities.ConferenceRoom;
import filiciak.cyran.demo.Entities.Office;
import filiciak.cyran.demo.Entities.Seat;
import filiciak.cyran.demo.Entities.UserInstance;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.UI.views.MainLayout;
import filiciak.cyran.demo.UI.views.adminViews.office.ManageOfficeView;
import filiciak.cyran.demo.UI.views.login.LoginView;

import java.util.ArrayList;
import java.util.List;
import java.util.Set;

@PageTitle("Delete Room From Office")
@Route(value = "delete-room-from-office", layout = MainLayout.class)
public class DeleteRoomsFromOfficeView extends Div implements AfterNavigationObserver {

    private CheckboxGroup<String> roomsToDelete = new CheckboxGroup<>();
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete");
    Office office;
    ConferenceRoomController conferenceRoomController;

    public DeleteRoomsFromOfficeView(ConferenceRoomController conferenceRoomController) {
        this.conferenceRoomController = conferenceRoomController;
        if(ComponentUtil.getData(UI.getCurrent(), Office.class) == null) {
            UI.getCurrent().navigate(ManageOfficeView.class);
        }
        office = ComponentUtil.getData(UI.getCurrent(), Office.class);
        addClassName("delete-room-from-office");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        cancel.addClickListener(e -> UI.getCurrent().navigate(ManageOfficeView.class));
        delete.addClickListener(e -> {

            Set<String> selectedRoomSet = roomsToDelete.getSelectedItems();
            List<String> selectedRoomsList = selectedRoomSet.stream().toList();
            selectedRoomsList.forEach(r -> {
                try {
                    conferenceRoomController.deleteRoom((conferenceRoomController.getRoomByNameAndOfficeId(r, office.getId()).getId()),"admin");
                } catch (BadRequestException ex) {
                    throw new RuntimeException(ex);
                }
            });

            UI.getCurrent().navigate(ManageOfficeView.class);
        });
    }

    private Component createTitle() {
        return new H3("Delete Rooms");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();

        List<ConferenceRoom> roomsList = conferenceRoomController.allFromOffice(office.getId());
        List<String> rooms = new ArrayList<>();
        roomsList.forEach(s -> rooms.add(s.getName()));

        roomsToDelete.setWidth("120px");
        roomsToDelete.setLabel("Select rooms to delete");
        roomsToDelete.setItems(rooms);

        formLayout.add(roomsToDelete);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        delete.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(delete);
        buttonLayout.add(cancel);
        return buttonLayout;
    }

    @Override
    public void afterNavigation(AfterNavigationEvent afterNavigationEvent) {
        if (!UserInstance.getInstance().isLogged() && !UserInstance.getInstance().isAdmin()) {
            UI.getCurrent().navigate(LoginView.class);
        }
    }
}
