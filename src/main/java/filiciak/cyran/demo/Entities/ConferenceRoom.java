package filiciak.cyran.demo.Entities;

import javax.persistence.*;
import lombok.Data;

import java.util.List;

@Data
@Entity
@Table
public class ConferenceRoom {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AvailabilityStatus status;

    @OneToMany
    private List<Equipment> equipments;
}
