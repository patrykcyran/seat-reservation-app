package filiciak.cyran.demo.Entities;

import javax.persistence.*;
import lombok.Data;

import java.io.Serializable;
import java.util.List;

@Entity
@Data
@Table(name = "seat")
public class Seat implements Serializable {

    private static final long serialVersionUID = 1L;

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(nullable = false)
    private Integer seatNumber;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private AvailabilityStatus status;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private SeatType type;

    @ManyToMany(fetch = FetchType.EAGER)
    private List<Equipment> equipments;

    @Column(nullable = false)
    private Integer officeID;

    public Seat() {}

    public Seat(int id) {
        this.setId(id);
    }
}
