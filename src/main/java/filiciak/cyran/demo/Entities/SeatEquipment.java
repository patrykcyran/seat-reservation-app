package filiciak.cyran.demo.Entities;

import javax.persistence.Entity;
import javax.persistence.Id;
import javax.persistence.IdClass;

@Entity
@IdClass(SeatEquipmentId.class)
public class SeatEquipment {
    @Id
    Integer seatId;
    @Id
    Integer equipmentId;
}
