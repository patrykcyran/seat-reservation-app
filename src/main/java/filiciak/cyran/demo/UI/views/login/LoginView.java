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
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import filiciak.cyran.demo.Controllers.UserController;
import filiciak.cyran.demo.Entities.User;
import filiciak.cyran.demo.Entities.UserInstance;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.UI.views.yourReservations.YourReservationsView;

@PageTitle("Login")
@Route(value = "login")
@Uses(Icon.class)
public class LoginView extends Div {

    private TextField usernameTextField = new TextField("username");
    private Button loginButton = new Button("Log in");
    UserController userController;

    public LoginView(UserController userController) {
        this.userController = userController;
        addClassName("person-form-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());

        loginButton.addClickListener(e -> {
            String username;
            username = usernameTextField.getValue();
            if (validateLoginInput(username)) {
                try {
                    loginUser(username);
                } catch (BadRequestException ex) {
                    throw new RuntimeException(ex);
                }
                if (username.equals("admin")) {
                    UI.getCurrent().navigate(AdminLoginView.class);
                } else {
                    UI.getCurrent().navigate(YourReservationsView.class);
                }
            } else {
                Notification.show("You must provide username to log in", 5000, Notification.Position.MIDDLE);
            }
        });
    }

    private boolean validateLoginInput(String username) {
        return !username.isBlank();
    }

    private void loginUser(String username) throws BadRequestException {
        UserInstance.setUser(username);
        if (!userController.userExists(username)) {
            userController.createUser(new User(username), "admin");
        }
    }

    private Component createTitle() {
        return new H3("Log in information");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(usernameTextField);
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
