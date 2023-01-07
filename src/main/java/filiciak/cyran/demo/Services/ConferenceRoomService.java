package filiciak.cyran.demo.Services;

import filiciak.cyran.demo.Entities.ConferenceRoom;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.Repositories.ConferenceRoomRepository;
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

    public ConferenceRoomService(ConferenceRoomRepository conferenceRoomRepository, OfficeRepository officeRepository) {
        this.conferenceRoomRepository = conferenceRoomRepository;
        this.officeRepository = officeRepository;
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

    public void delete(Integer id) throws BadRequestException {
        ConferenceRoom room = conferenceRoomRepository.getReferenceById(id);
        if(!room.getEquipments().isEmpty()){
            throw new BadRequestException("Room has children (first delete equipment)");
        }
        log.debug("Request to delete Room : {} ", id);
        conferenceRoomRepository.deleteById(id);
    }
}
