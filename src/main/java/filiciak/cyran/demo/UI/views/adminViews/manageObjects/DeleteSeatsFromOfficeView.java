package filiciak.cyran.demo.UI.views.adminViews.manageObjects;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.ComponentUtil;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.checkbox.CheckboxGroup;
import com.vaadin.flow.component.combobox.ComboBox;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.router.AfterNavigationEvent;
import com.vaadin.flow.router.AfterNavigationObserver;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import filiciak.cyran.demo.Controllers.SeatController;
import filiciak.cyran.demo.Entities.AvailabilityStatus;
import filiciak.cyran.demo.Entities.Office;
import filiciak.cyran.demo.Entities.Seat;
import filiciak.cyran.demo.Entities.UserInstance;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.UI.views.MainLayout;
import filiciak.cyran.demo.UI.views.adminViews.office.ManageOfficeView;
import filiciak.cyran.demo.UI.views.adminViews.room.ManageRoomView;
import filiciak.cyran.demo.UI.views.adminViews.seat.ManageSeatView;
import filiciak.cyran.demo.UI.views.login.LoginView;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

@PageTitle("Delete Seat From Office")
@Route(value = "delete-seat-from-office", layout = MainLayout.class)
public class DeleteSeatsFromOfficeView extends Div implements AfterNavigationObserver {

    private CheckboxGroup<String> seatsToDelete = new CheckboxGroup<>();
    private Button cancel = new Button("Cancel");
    private Button delete = new Button("Delete");
    Office office;
    SeatController seatController;

    public DeleteSeatsFromOfficeView(SeatController seatController) {
        this.seatController = seatController;
        if(ComponentUtil.getData(UI.getCurrent(), Office.class) == null) {
            UI.getCurrent().navigate(ManageOfficeView.class);
        }
        office = ComponentUtil.getData(UI.getCurrent(), Office.class);
        addClassName("delete-seat-from-office");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        cancel.addClickListener(e -> UI.getCurrent().navigate(ManageOfficeView.class));
        delete.addClickListener(e -> {

            Set<String> selectedSeatsSet = seatsToDelete.getSelectedItems();
            List<String> selectedSeatsList = selectedSeatsSet.stream().toList();
            selectedSeatsList.forEach(s -> {
                try {
                    seatController.deleteSeat((seatController.getSeatByNumberAndOfficeId(Integer.valueOf(s), office.getId()).getId()),"admin");
                } catch (BadRequestException ex) {
                    throw new RuntimeException(ex);
                }
            });

            UI.getCurrent().navigate(ManageOfficeView.class);
        });
    }

    private Component createTitle() {
        return new H3("Delete seats");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();

        List<Seat> seatsList = seatController.allFromOffice(office.getId());
        List<String> seats = new ArrayList<>();
        seatsList.forEach(s -> seats.add(s.getSeatNumber().toString()));

        seatsToDelete.setWidth("120px");
        seatsToDelete.setLabel("Select seats to delete");
        seatsToDelete.setItems(seats);

        formLayout.add(seatsToDelete);
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
