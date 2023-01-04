package filiciak.cyran.demo.Entities;

import jakarta.persistence.*;
import lombok.Data;

@Entity
@Data
@Table(name = "seat")
public class Seat {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
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

    public Seat() {}

    public Seat(int id) {
        this.setId(id);
    }
}
