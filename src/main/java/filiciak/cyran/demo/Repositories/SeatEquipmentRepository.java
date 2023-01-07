package filiciak.cyran.demo.Repositories;

import filiciak.cyran.demo.Entities.SeatEquipment;
import filiciak.cyran.demo.Entities.SeatEquipmentId;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.web.bind.annotation.RequestHeader;

@Repository
public interface SeatEquipmentRepository extends JpaRepository<SeatEquipment, SeatEquipmentId> {

}
