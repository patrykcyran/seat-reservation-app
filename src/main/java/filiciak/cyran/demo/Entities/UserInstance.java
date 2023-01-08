package filiciak.cyran.demo.Entities;

import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.Column;

@Data
public class UserInstance {
    private static UserInstance INSTANCE = new UserInstance();

    private Integer id;
    String username;
    boolean isLogged;
    boolean isAdmin;

    public static UserInstance getInstance() {
        if (INSTANCE == null) {
            INSTANCE = new UserInstance();
        }

        return INSTANCE;
    }

    public static void setUser(String username) {
        if (!INSTANCE.isLogged) {
            INSTANCE.setUsername(username);
            INSTANCE.setLogged(true);
            INSTANCE.setAdmin(false);
        }
    }

    public static void logOut() {
        INSTANCE.setUsername(null);
        INSTANCE.setLogged(false);
        INSTANCE.setAdmin(false);

    }
}
