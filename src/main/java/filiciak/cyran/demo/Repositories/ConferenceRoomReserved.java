package filiciak.cyran.demo.Repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ConferenceRoomReserved extends JpaRepository<ConferenceRoomReserved, Integer> {
}
