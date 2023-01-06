package filiciak.cyran.demo.Entities;

import javax.persistence.*;
import lombok.Data;

import java.util.List;

@Entity
@Data
@Table(name = "seat")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(unique = true, nullable = false)
    private Integer seatNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AvailabilityStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SeatType type;

    @OneToMany
    private List<Equipment> equipments;

    @ManyToOne
    @JoinColumn(name = "office")
    private Office office;

    public Seat() {}

    public Seat(int id) {
        this.setId(id);
    }
}
