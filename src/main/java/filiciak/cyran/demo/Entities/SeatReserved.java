package filiciak.cyran.demo.Entities;

import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDateTime;

@Data
@Entity
@Table
public class SeatReserved {

    @Id
    @GeneratedValue(strategy = GenerationType.SEQUENCE)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(nullable = false)
    private String name;

    @Column(nullable = false)
    private LocalDateTime fromDate;

    @Column(nullable = false)
    private LocalDateTime toDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @ManyToOne
    @JoinColumn(name = "seat_id")
    private Seat seat;
}
