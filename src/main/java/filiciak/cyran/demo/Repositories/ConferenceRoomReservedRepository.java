package filiciak.cyran.demo.Repositories;

import filiciak.cyran.demo.Entities.ConferenceRoomReserved;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConferenceRoomReservedRepository extends JpaRepository<ConferenceRoomReserved, Integer> {
}
