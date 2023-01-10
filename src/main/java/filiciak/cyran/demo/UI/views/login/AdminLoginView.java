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
import com.vaadin.flow.component.textfield.PasswordField;
import com.vaadin.flow.component.textfield.TextField;
import com.vaadin.flow.router.PageTitle;
import com.vaadin.flow.router.Route;
import filiciak.cyran.demo.Controllers.UserController;
import filiciak.cyran.demo.Entities.User;
import filiciak.cyran.demo.Entities.UserInstance;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.UI.views.MainLayout;
import filiciak.cyran.demo.UI.views.adminViews.seat.ManageSeatView;
import filiciak.cyran.demo.UI.views.yourReservations.YourReservationsView;

@PageTitle("AdminLogin")
@Route(value = "adminLogin")
@Uses(Icon.class)
public class AdminLoginView extends Div {

    private final String PASSWORD = "admin";
    private PasswordField passwordField = new PasswordField("password");
    private Button loginButton = new Button("Log in as admin");
    private Button backButton = new Button("Go back to normal log in");

    UserController userController;

    public AdminLoginView(UserController userController) {
        this.userController = userController;
        addClassName("admin-form-view");

        add(createTitle());
        add(createFormLayout());
        add(createButtonLayout());
        add(createBackButtonLayout());

        backButton.addClickListener(e -> UI.getCurrent().navigate(LoginView.class));

        loginButton.addClickListener(e -> {
           String password;
           password = passwordField.getValue();
           if (password.equals(PASSWORD)) {
               try {
                   loginAdmin();
               } catch (BadRequestException ex) {
                   throw new RuntimeException(ex);
               }
               UserInstance.getInstance().setAdmin(true);
               UI.getCurrent().navigate(ManageSeatView.class);
           } else {
               Notification.show("Wrong admin password", 5000, Notification.Position.MIDDLE);
           }
        });
    }

    private void loginAdmin() throws BadRequestException {
        String username = "admin";
        UserInstance.setUser(username);
        if (!userController.userExists(username)) {
            userController.createUser(new User(username), "admin");
        }
    }

    private Component createTitle() {
        return new H3("Admin log in");
    }

    private Component createFormLayout() {
        FormLayout formLayout = new FormLayout();
        formLayout.add(passwordField);
        return formLayout;
    }

    private Component createButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        loginButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(loginButton);
        return buttonLayout;
    }

    private Component createBackButtonLayout() {
        HorizontalLayout buttonLayout = new HorizontalLayout();
        buttonLayout.addClassName("button-layout");
        backButton.addThemeVariants(ButtonVariant.LUMO_PRIMARY);
        buttonLayout.add(backButton);
        return buttonLayout;
    }
}
