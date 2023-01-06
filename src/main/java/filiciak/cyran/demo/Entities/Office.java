package filiciak.cyran.demo.Entities;

import javax.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class Office {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;
}
