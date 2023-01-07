package filiciak.cyran.demo.Entities;

import javax.persistence.Id;
import java.io.Serializable;
import java.util.Objects;

public class SeatEquipmentId implements Serializable {

    Integer seatId;

    Integer equipmentId;


    @Override
    public boolean equals(Object o) {
        if (this == o) return true;
        if (o == null || getClass() != o.getClass()) return false;
        SeatEquipmentId seatId = (SeatEquipmentId) o;
        return this.seatId.equals(seatId.seatId) &&
                equipmentId.equals(seatId.equipmentId);
    }

    @Override
    public int hashCode() {
        return Objects.hash(seatId, equipmentId);
    }
}
