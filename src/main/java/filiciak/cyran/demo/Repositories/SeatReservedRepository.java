package filiciak.cyran.demo.Repositories;

import filiciak.cyran.demo.Entities.SeatReserved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SeatReservedRepository extends JpaRepository<SeatReserved, Integer> {
}


