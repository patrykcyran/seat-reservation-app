package filiciak.cyran.demo.Repositories;

import filiciak.cyran.demo.Entities.ConferenceRoom;
import filiciak.cyran.demo.Entities.Equipment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface EquipmentRepository extends JpaRepository<Equipment, Integer> {

    @Query("select e from Equipment e where e.name = :name")
    Equipment findByName(@Param("name") String name);
}
