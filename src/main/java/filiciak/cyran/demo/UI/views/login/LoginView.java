package filiciak.cyran.demo.UI.views.login;

import com.vaadin.flow.component.Component;
import com.vaadin.flow.component.UI;
import com.vaadin.flow.component.button.Button;
import com.vaadin.flow.component.button.ButtonVariant;
import com.vaadin.flow.component.dependency.Uses;
import com.vaadin.flow.component.formlayout.FormLayout;
import com.vaadin.flow.component.html.Div;
import com.vaadin.flow.component.html.H3;
import com.vaadin.flow.component.icon.Icon;
import com.vaadin.flow.component.notification.Notification;
import com.vaadin.flow.component.orderedlayout.HorizontalLayout;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.data.binder.Binder;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import filiciak.cyran.demo.Entities.User;
import filiciak.cyran.demo.UI.views.yourReservations.YourReservationsView;

@PageTitle("Login")
@Route(value = "login")
@Uses(Icon.class)
public class LoginView extends Div {

    private TextField firstNameTextField = new TextField("First name");
    private TextField lastNameTextField = new TextField("Last name");
    private Button loginButton = new Button("Login");

    public LoginView() {
        addClassName("person-form-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());


        loginButton.addClickListener(e -> {
            String firstName;
            String lastName;
            firstName = firstNameTextField.getValue();
            lastName = lastNameTextField.getValue();
            if (validateLoginInput(firstName, lastName)) {
                User.setUser(firstName, lastName);
                UI.getCurrent().navigate(YourReservationsView.class);
            } else {
                Notification.show("Both fields must be populated to log in", 5000, Notification.Position.MIDDLE);
            }
        });
    }

    private boolean validateLoginInput(String firstName, String lastName) {
        return !firstName.isBlank() && !lastName.isBlank();
    }

    private Component createTitle() {
        return new H3("Login information");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(firstNameTextField, lastNameTextField);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(loginButton);
        return buttonLayout;
    }


}
