package filiciak.cyran.demo.Services;

import filiciak.cyran.demo.Entities.ConferenceRoom;
import filiciak.cyran.demo.Entities.Equipment;
import filiciak.cyran.demo.Entities.Seat;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.Repositories.ConferenceRoomRepository;
import filiciak.cyran.demo.Repositories.EquipmentRepository;
import filiciak.cyran.demo.Repositories.OfficeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;


@Service
public class ConferenceRoomService {

    private final Logger log = LoggerFactory.getLogger(ConferenceRoomService.class);
    private final ConferenceRoomRepository conferenceRoomRepository;
    private final OfficeRepository officeRepository;
    private final EquipmentRepository equipmentRepository;
    private final EquipmentService equipmentService;

    public ConferenceRoomService(ConferenceRoomRepository conferenceRoomRepository, OfficeRepository officeRepository, EquipmentRepository equipmentRepository, EquipmentService equipmentService) {
        this.conferenceRoomRepository = conferenceRoomRepository;
        this.officeRepository = officeRepository;
        this.equipmentRepository = equipmentRepository;
        this.equipmentService = equipmentService;
    }

    public ConferenceRoom save(ConferenceRoom conferenceRoom) throws BadRequestException {
        if(conferenceRoom.getId() != null){
            throw new BadRequestException("A new room cannot already have an ID");
        }
        if(!officeRepository.existsById(conferenceRoom.getOfficeId())){
            throw new BadRequestException("An office does not exist");
        }
        if(!(conferenceRoomRepository.findRoomByRoomNumberAndOfficeId(conferenceRoom.getName(), conferenceRoom.getOfficeId()) == null)){
            throw new BadRequestException("Room with this name exist in this office");
        }
        log.debug("Request to save room : {} ", conferenceRoom);
        return conferenceRoomRepository.save(conferenceRoom);
    }

    public ConferenceRoom update(ConferenceRoom conferenceRoom) throws BadRequestException {
        if(conferenceRoom.getId() == null){
            throw new BadRequestException("Room does not exist");
        }
        if(!conferenceRoomRepository.existsById(conferenceRoom.getId())){
            throw new BadRequestException("Room with id " + conferenceRoom.getId() + " does not exist");
        }
        return conferenceRoomRepository.save(conferenceRoom);
    }

    public List<ConferenceRoom> findAll(){
        log.debug("Request to get all Rooms");
        return conferenceRoomRepository.findAll();
    }

    public ConferenceRoom findById(Integer id) {
        log.debug("Request to get Room : {}", id);
        return conferenceRoomRepository.findById(id).get();
    }

    public List<ConferenceRoom> findAllByOffice(Integer officeId){
        log.debug("Request to get all Rooms from {} ", officeRepository.findById(officeId).get());
        return conferenceRoomRepository.findRoomsByOfficeId(officeId);
    }

    public List<ConferenceRoom> findAllFreeByOffice(Integer officeId){
        log.debug("Request to get all free Rooms from {} ", officeRepository.findById(officeId).get());
        return conferenceRoomRepository.findFreeRoomsByOfficeId(officeId);
    }

    public ConferenceRoom findRoomByNameAndOfficeId(String name, Integer officeId) {
        return conferenceRoomRepository.findRoomByRoomNumberAndOfficeId(name, officeId);
    }

    public ConferenceRoom addEquipmentById(Integer conferenceRoomId, Integer equipmentId) throws BadRequestException {
        idValidation(conferenceRoomId, equipmentId);
        ConferenceRoom conferenceRoom = conferenceRoomRepository.getReferenceById(conferenceRoomId);
        Equipment eq = equipmentRepository.getReferenceById(equipmentId);
        List<Equipment> newEq = conferenceRoom.getEquipments();
        newEq.add(eq);
        conferenceRoom.setEquipments(newEq);
        return conferenceRoomRepository.save(conferenceRoom);
    }
    public ConferenceRoom deleteEquipmentById(Integer conferenceRoomId, Integer equipmentId) throws  BadRequestException{
        idValidation(conferenceRoomId, equipmentId);
        ConferenceRoom conferenceRoom = conferenceRoomRepository.getReferenceById(conferenceRoomId);
        Equipment eq = equipmentRepository.getReferenceById(equipmentId);
        return deleteEq(conferenceRoom, eq);
    }
    public ConferenceRoom deleteEquipment(ConferenceRoom conferenceRoom, Equipment equipment) {
        return deleteEq(conferenceRoom, equipment);
    }
    private void idValidation(Integer conferenceRoomId, Integer equipmentId) throws BadRequestException {
        if (conferenceRoomId == null){
            throw new BadRequestException("Seat does not exist");
        }
        if(!conferenceRoomRepository.existsById(conferenceRoomId)){
            throw new BadRequestException("Seat with id " + conferenceRoomId + " does not exist");
        }
        if(equipmentService.findById(equipmentId) == null){
            throw new BadRequestException("Equipment with id " + equipmentId + " does not exist");
        }
    }

    private ConferenceRoom deleteEq(ConferenceRoom conferenceRoom, Equipment equipment) {
        ConferenceRoom deletedEqConfRoom = conferenceRoom;
        List<Equipment> newEw = conferenceRoom.getEquipments();
        newEw.remove(equipment);
        deletedEqConfRoom.setEquipments(newEw);
        return conferenceRoomRepository.save(deletedEqConfRoom);
    }

    public void delete(Integer id) throws BadRequestException {
        if(!conferenceRoomRepository.existsById(id)) {
            throw new BadRequestException("Room with id " + id + "  does not exist");
        }
        log.debug("Request to delete Room : {} ", id);

        conferenceRoomRepository.deleteById(id);
    }

    public List<String> findEquipmentByRoomId(Integer id) throws BadRequestException{
        if(!conferenceRoomRepository.existsById(id)) {
            throw new BadRequestException("Room with id " + id + "  does not exist");
        }
        log.debug("Request to get equipment for Room : {} ", id);

        return conferenceRoomRepository.findEquipmentByRoomId(id);
    }
}
