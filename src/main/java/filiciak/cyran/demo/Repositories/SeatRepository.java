package filiciak.cyran.demo.Repositories;

import filiciak.cyran.demo.Entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatRepository extends JpaRepository<Seat, Integer> {

}
