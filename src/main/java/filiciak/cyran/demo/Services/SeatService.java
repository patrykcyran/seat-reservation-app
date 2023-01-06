package filiciak.cyran.demo.Services;

import filiciak.cyran.demo.Entities.Office;
import filiciak.cyran.demo.Entities.Seat;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.Repositories.OfficeRepository;
import filiciak.cyran.demo.Repositories.SeatRepository;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import java.util.List;
import java.util.Optional;

@Service
public class SeatService {

    private final Logger log = LoggerFactory.getLogger(SeatService.class);
    private final SeatRepository seatRepository;
    private final OfficeRepository officeRepository;

    public SeatService(SeatRepository seatRepository, OfficeRepository officeRepository) {
        this.seatRepository = seatRepository;
        this.officeRepository = officeRepository;
    }

    public Seat save(Seat seat) throws BadRequestException {
        if(seat.getId() != null){
            throw new BadRequestException("A new seat cannot already have an ID");
        }
        if(!officeRepository.existsById(seat.getOfficeID())){
            throw new BadRequestException("An office does not exist");
        }
        if(!(seatRepository.findSeatBySeatNumberAndOfficeId(seat.getSeatNumber(), seat.getOfficeID()) == null)){
            throw new BadRequestException("Seat with this seatNumber exist in this office");
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

    public Seat findById(Integer id) {
        log.debug("Request to get Seat : {}", id);
        return seatRepository.findById(id).get();
    }

    public List<Seat> findAllByOffice(Integer officeId){
        log.debug("Request to get all Seats from {} ", officeRepository.findById(officeId).get());
        return seatRepository.findSeatsByOfficeId(officeId);
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
