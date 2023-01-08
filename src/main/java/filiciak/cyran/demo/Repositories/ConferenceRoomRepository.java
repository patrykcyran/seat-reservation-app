package filiciak.cyran.demo.Repositories;

import filiciak.cyran.demo.Entities.ConferenceRoom;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import javax.persistence.criteria.CriteriaBuilder;
import java.util.List;

@Repository
public interface ConferenceRoomRepository extends JpaRepository<ConferenceRoom, Integer> {

    @Query("select r from ConferenceRoom r where r.officeId = :office_id")
    List<ConferenceRoom> findRoomsByOfficeId(@Param("office_id") Integer officeId);

    @Query("select r from ConferenceRoom r where r.name = :room_name and r.officeId = :office_id")
    ConferenceRoom findRoomByRoomNumberAndOfficeId(@Param("room_name") String roomName, @Param("office_id") Integer officeId);

    @Query(
            value = "SELECT name FROM equipment JOIN conference_room_equipments ON equipment.id = conference_room_equipments.equipments_id WHERE conference_room_id = :room_id",
            nativeQuery = true)
    List<String> findEquipmentByRoomId(@Param("room_id") Integer seatId);

    @Query("select r from ConferenceRoom r where r.officeId = :office_id and r.status = 'FREE'")
    List<ConferenceRoom> findFreeRoomsByOfficeId(@Param("office_id") Integer officeId);
}
