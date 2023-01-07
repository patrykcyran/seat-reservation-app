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
        }
    }
}
