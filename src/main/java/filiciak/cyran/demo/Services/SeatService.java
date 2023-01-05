package filiciak.cyran.demo.Services;

import filiciak.cyran.demo.Entities.Office;
import filiciak.cyran.demo.Entities.Seat;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.Repositories.SeatRepository;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

@Service
public class SeatService {

    private final Logger log = LoggerFactory.getLogger(SeatService.class);
    private final SeatRepository seatRepository;

    public SeatService(SeatRepository seatRepository) {
        this.seatRepository = seatRepository;
    }

    public Seat save(Seat seat) throws BadRequestException {
        if(seat.getId() != null){
            throw new BadRequestException("A new seat cannot already have an ID");
        }
        log.debug("Request to save seat : {} ", seat);
        return seatRepository.save(seat);
    }

    public Seat update(Seat seat, Integer id) throws BadRequestException {
        if(seat.getId() == null){
            throw new BadRequestException("Seat does not exist");
        }
        if(seat.getId() != id){
            throw new BadRequestException("Invalid id");
        }
        if(!seatRepository.existsById(id)){
            throw new BadRequestException("Seat with id " + id + " does not exist");
        }
        return seatRepository.save(seat);
    }

    public List<Seat> findAll(){
        log.debug("Request to get all Seats");
        return seatRepository.findAll();
    }

    public Optional<Seat> findById(Integer id) {
        log.debug("Request to get Seat : {}", id);
        return seatRepository.findById(id);
    }

    public List<Seat> findAllByOffice(Office office){
        log.debug("Request to get all Seats from {} ", office);
        return seatRepository.findSeatsByOfficeId(office.getId());
    }

    public void delete(Integer id) throws BadRequestException {
        Seat seat = seatRepository.getReferenceById(id);
        if(!seat.getEquipments().isEmpty()){
            throw new BadRequestException("Seat has children (first delete equipment)");
        }
        log.debug("Request to delete Seat : {} ", id);
        seatRepository.deleteById(id);
    }

}
