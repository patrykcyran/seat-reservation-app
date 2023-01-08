package filiciak.cyran.demo.Entities;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    public Equipment(String name) {
        this.name = name;
    }

    public Equipment() {

    }
}
