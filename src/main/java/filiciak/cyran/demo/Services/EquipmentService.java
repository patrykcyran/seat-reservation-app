package filiciak.cyran.demo.Services;

import filiciak.cyran.demo.Entities.ConferenceRoom;
import filiciak.cyran.demo.Entities.Equipment;
import filiciak.cyran.demo.Entities.Seat;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.Repositories.EquipmentRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.context.annotation.Lazy;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EquipmentService {

    private final Logger log = LoggerFactory.getLogger(EquipmentService.class);
    private final EquipmentRepository equipmentRepository;

    private final SeatService seatService;
    private final ConferenceRoomService conferenceRoomService;

    private boolean doesContain = false;

    @Autowired
    public EquipmentService(EquipmentRepository equipmentRepository, @Lazy SeatService seatService, @Lazy ConferenceRoomService conferenceRoomService) {
        this.equipmentRepository = equipmentRepository;
        this.seatService = seatService;
        this.conferenceRoomService = conferenceRoomService;
    }


    public Equipment save(Equipment equipment) throws BadRequestException {
        if(equipment.getId() != null){
            throw new BadRequestException("A new equipment cannot already have an ID");
        }
        log.debug("Request to save equipment : {} ", equipment);
        return equipmentRepository.save(equipment);
    }
    public Equipment update(Equipment equipment) throws BadRequestException {
        if(equipment.getId() == null){
            throw new BadRequestException("Equipment does not exist");
        }
        if(!equipmentRepository.existsById(equipment.getId())){
            throw new BadRequestException("Equipment with id " + equipment.getId() + "does not exist");
        }
        return equipmentRepository.save(equipment);
    }
    public List<Equipment> findAll(){
        log.debug("Request to get all Equipment");
        return equipmentRepository.findAll();
    }
    public Equipment findById(Integer id) throws BadRequestException {
        if(!equipmentRepository.existsById(id)){
            throw new BadRequestException("Equipment with id " + id + "  does not exist");
        }
        log.debug("Request to get Equipment : {}", id);
        return equipmentRepository.findById(id).get();
    }

    public Equipment findByName(String name) throws BadRequestException {
        if(equipmentRepository.findAll().stream().noneMatch(e -> e.getName().equals(name))){
            throw new BadRequestException("Equipment with name " + name + "  does not exist");
        }
        log.debug("Request to get Equipment name : {}", name);
        return equipmentRepository.findByName(name);
    }

    public void delete(Integer id) throws BadRequestException {
        if(!equipmentRepository.existsById(id)){
            throw new BadRequestException("Equipment with id " + id + "  does not exist");
        }
        Equipment eq = equipmentRepository.findById(id).get();
        List<Seat> seats = seatService.findAll();
        List<ConferenceRoom> conferenceRooms = conferenceRoomService.findAll();

        for (Seat seat : seats) {
            doesContain = seat.getEquipments().contains(eq);
            if(doesContain){
                seatService.deleteEquipment(seat, eq);
                doesContain = false;
            }
        }
        for (ConferenceRoom conferenceRoom : conferenceRooms){
            doesContain = conferenceRoom.getEquipments().contains(eq);
            if(doesContain){
                conferenceRoomService.deleteEquipment(conferenceRoom, eq);
                doesContain = false;
            }
        }
        log.debug("Request to delete Equipment : {} ", id);
        equipmentRepository.deleteById(id);
    }
}

