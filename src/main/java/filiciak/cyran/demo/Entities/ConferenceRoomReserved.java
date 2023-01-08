package filiciak.cyran.demo.Entities;

import javax.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Data
@Entity
@Table
public class ConferenceRoomReserved {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(unique = true, nullable = false)
    private Integer id;

    @Column(nullable = false)
    private LocalDate fromDate;

    @Column(nullable = false)
    private LocalDate toDate;

    @Column(nullable = false)
    @Enumerated(EnumType.STRING)
    private ReservationStatus status;

    @ManyToOne
    @JoinColumn(name = "conferenceRoom_id")
    private ConferenceRoom conferenceRoom;

    @ManyToOne
    @JoinColumn(name = "user_id")
    private User user;
}
