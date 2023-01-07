package filiciak.cyran.demo.Repositories;

import filiciak.cyran.demo.Entities.Equipment;
import filiciak.cyran.demo.Entities.Office;
import filiciak.cyran.demo.Entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {

    @Query("select s from Seat s where s.officeID = :office_id")
    List<Seat> findSeatsByOfficeId(@Param("office_id") Integer officeId);

    @Query ("select s from Seat s where s.seatNumber = :seat_number and s.officeID = :office_id")
    Seat findSeatBySeatNumberAndOfficeId(@Param("seat_number") Integer seatNumber, @Param("office_id") Integer officeId);

}
