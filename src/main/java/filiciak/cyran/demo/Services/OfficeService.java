package filiciak.cyran.demo.Services;

import filiciak.cyran.demo.Entities.Office;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.Repositories.OfficeRepository;
import filiciak.cyran.demo.Repositories.SeatRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class OfficeService {

    private final Logger log = LoggerFactory.getLogger(OfficeService.class);
    private final OfficeRepository officeRepository;
    private final SeatRepository seatRepository;

    public OfficeService(OfficeRepository officeRepository, SeatRepository seatRepository) {
        this.officeRepository = officeRepository;
        this.seatRepository = seatRepository;
    }

    public Office save(Office office) throws BadRequestException {
        if (office.getId() != null) {
            throw new BadRequestException("An office cannot already have an ID");
        }
        log.debug("Request to save office : {} ", office);
        return officeRepository.save(office);
    }

    public Office update(Office office) throws BadRequestException {
        if(office.getId() == null){
            throw new BadRequestException("Office does not exist");
        }
        if(!officeRepository.existsById(office.getId())){
            throw new BadRequestException("Office with id " + office.getId() + " does not exist");
        }
        return officeRepository.save(office);
    }
    public List<Office> findAll(){
        log.debug("Request to get all Office");
        return officeRepository.findAll();
    }
    public Office findById(Integer id) {
        log.debug("Request to get Office : {}", id);

        return officeRepository.findById(id).get();
    }
    public void delete(Integer id) throws BadRequestException {
        if (id == null){
            throw new BadRequestException("Invalid ID");
        }
        if (!officeRepository.existsById(id)){
            throw new BadRequestException("Office does not exist");
        }
        if(!seatRepository.findSeatsByOfficeId(id).isEmpty()){
            throw new BadRequestException("Office has children (first delete seats)");
        }
        log.debug("Request to delete Office : {} ", id);
        officeRepository.deleteById(id);
    }
}
