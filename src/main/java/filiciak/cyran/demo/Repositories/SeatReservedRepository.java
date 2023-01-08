package filiciak.cyran.demo.Repositories;

import filiciak.cyran.demo.Entities.SeatReserved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface SeatReservedRepository extends JpaRepository<SeatReserved, Integer> {

    @Query("select s from SeatReserved s JOIN User u on s.user = u where u.username = :username")
    List<SeatReserved> findSeatsByUser(@Param("username") String username);

}


