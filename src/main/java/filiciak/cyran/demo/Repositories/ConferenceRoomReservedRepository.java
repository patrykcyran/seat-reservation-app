package filiciak.cyran.demo.Repositories;

import filiciak.cyran.demo.Entities.ConferenceRoomReserved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface ConferenceRoomReservedRepository extends JpaRepository<ConferenceRoomReserved, Integer> {

    @Query("select c from ConferenceRoomReserved c JOIN User u on c.user = u where u.username = :username")
    List<ConferenceRoomReserved> findRoomByUser(@Param("username") String username);

}
