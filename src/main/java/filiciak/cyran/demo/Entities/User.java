package filiciak.cyran.demo.Entities;

import filiciak.cyran.demo.Controllers.UserController;
import lombok.Data;
import lombok.NoArgsConstructor;

import javax.persistence.*;

@NoArgsConstructor
@Data
@Entity
@Table
public class User {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(nullable = false, unique = true)
    String username;

    public User(String username) {
        this.username = username;
    }
}
