package filiciak.cyran.demo.Repositories;

import filiciak.cyran.demo.Entities.Office;
import filiciak.cyran.demo.Entities.Seat;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

@Repository
public interface OfficeRepository extends JpaRepository<Office, Integer> {

    @Query("select o from Office o where o.address.id = :address_id")
    Office getOfficeByAddressId(@Param("address_id") Integer addressId);

    @Query("select o from Office o where o.name = :name")
    Office findOfficeByName(@Param("name") String name);
}
