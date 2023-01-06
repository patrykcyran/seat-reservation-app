package filiciak.cyran.demo.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;

@NoArgsConstructor
@Data
public class User {
    private static User INSTANCE = new User();
    String firstName;
    String lastName;
    boolean isLogged;

    public static User getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new User();
        }

        return INSTANCE;
    }

    public static void setUser(String firstName, String lastName) {
        if (!INSTANCE.isLogged) {
            INSTANCE.setFirstName(firstName);
            INSTANCE.setLastName(lastName);
            INSTANCE.setLogged(true);
        }
    }

}
