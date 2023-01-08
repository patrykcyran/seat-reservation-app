package filiciak.cyran.demo.Services;

import filiciak.cyran.demo.Entities.Equipment;
import filiciak.cyran.demo.Entities.Seat;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.Repositories.EquipmentRepository;
import filiciak.cyran.demo.Repositories.OfficeRepository;
import filiciak.cyran.demo.Repositories.SeatRepository;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import org.slf4j.Logger;

import java.util.List;

@Service
public class SeatService {

    private final Logger log = LoggerFactory.getLogger(SeatService.class);
    private final SeatRepository seatRepository;
    private final OfficeRepository officeRepository;
    private final EquipmentRepository equipmentRepository;
    private final EquipmentService equipmentService;

    public SeatService(SeatRepository seatRepository, OfficeRepository officeRepository, EquipmentRepository equipmentRepository, EquipmentService equipmentService) {
        this.seatRepository = seatRepository;
        this.officeRepository = officeRepository;
        this.equipmentRepository = equipmentRepository;
        this.equipmentService = equipmentService;
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

    public Seat update(Seat seat) throws BadRequestException {
        if(seat.getId() == null){
            throw new BadRequestException("Seat does not exist");
        }
        if(!seatRepository.existsById(seat.getId())){
            throw new BadRequestException("Seat with id " + seat.getId() + " does not exist");
        }
        return seatRepository.save(seat);
    }

    public Seat findSeatBySeatNumberAndOfficeId(Integer seatNumber, Integer officeId) {
        return seatRepository.findSeatBySeatNumberAndOfficeId(seatNumber, officeId);
    }

    public Seat addEquipmentById(Integer seatId, Integer equipmentId) throws BadRequestException {
        idValidation(seatId, equipmentId);
        Seat seat = seatRepository.getReferenceById(seatId);
        Equipment eq = equipmentRepository.getReferenceById(equipmentId);
        List<Equipment> newEq = seat.getEquipments();
        newEq.add(eq);
        seat.setEquipments(newEq);
        return seatRepository.save(seat);
    }

    public Seat deleteEquipmentById(Integer seatId, Integer equipmentId) throws  BadRequestException{
        idValidation(seatId,equipmentId);
        Seat seat = seatRepository.getReferenceById(seatId);
        Equipment eq = equipmentRepository.getReferenceById(equipmentId);
        return deleteEq(seat, eq);
    }
    public Seat deleteEquipment(Seat seat, Equipment equipment) {
        return deleteEq(seat, equipment);
    }

    private void idValidation(Integer seatId, Integer equipmentId) throws BadRequestException {
        if (seatId == null){
            throw new BadRequestException("Seat does not exist");
        }
        if(!seatRepository.existsById(seatId)){
            throw new BadRequestException("Seat with id " + seatId + " does not exist");
        }
        if(equipmentService.findById(equipmentId) == null){
            throw new BadRequestException("Equipment with id " + equipmentId + " does not exist");
        }
    }

    private Seat deleteEq(Seat seat, Equipment equipment) {
        Seat deletedEqSeat = seat;
        List<Equipment> newEw = seat.getEquipments();
        newEw.remove(equipment);
        deletedEqSeat.setEquipments(newEw);
        return seatRepository.save(deletedEqSeat);
    }

    public List<Seat> findAll(){
        log.debug("Request to get all Seats");
        return seatRepository.findAll();
    }

    public Seat findById(Integer id) throws BadRequestException {
        if(!seatRepository.existsById(id)){
            throw new BadRequestException("Seat with id " + id + "  does not exist");
        }
        log.debug("Request to get Seat : {}", id);
        return seatRepository.findById(id).get();
    }

    public List<Seat> findAllByOffice(Integer officeId){
        log.debug("Request to get all Seats from {} ", officeRepository.findById(officeId).get());
        return seatRepository.findSeatsByOfficeId(officeId);
    }

    public List<Seat> findAllFreeByOffice(Integer officeId) {
        log.debug("Request to get all free Seats from {} ", officeRepository.findById(officeId).get());
        return seatRepository.findFreeSeatsByOfficeId(officeId);
    }

    public void delete(Integer id) throws BadRequestException {
        if(!seatRepository.existsById(id)){
            throw new BadRequestException("Seat with id " + id + "  does not exist");
        }
        log.debug("Request to delete Seat : {} ", id);

        seatRepository.deleteById(id);
    }

    public List<String> findEquipmentBySeatId(Integer id) throws BadRequestException{
        if(!seatRepository.existsById(id)) {
            throw new BadRequestException("Seat with id " + id + "  does not exist");
        }
        log.debug("Request to get equipment for Seat : {} ", id);

        return seatRepository.findEquipmentBySeatId(id);
    }

    public Seat findSeatWithHighestNumber() {
        return seatRepository.findSeatWithHighestNumber();
    }
}
