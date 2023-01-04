package filiciak.cyran.demo.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Data
@Entity
@Table
public class Equipment {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;
}
