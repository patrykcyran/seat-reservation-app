package filiciak.cyran.demo.Controllers;

import filiciak.cyran.demo.Entities.ConferenceRoom;
import filiciak.cyran.demo.Entities.Seat;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.Services.ConferenceRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.repository.query.Param;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class ConferenceRoomController {
    private final Logger log = LoggerFactory.getLogger(ConferenceRoomController.class);
    private final ConferenceRoomService conferenceRoomService;

    public ConferenceRoomController(ConferenceRoomService conferenceRoomService) {this.conferenceRoomService = conferenceRoomService;}

    @PostMapping
    public ResponseEntity<ConferenceRoom> createRoom(@RequestBody ConferenceRoom conferenceRoom, @RequestHeader String authorization) throws BadRequestException {
        if (authorization.equals("admin")) {
            log.debug("REST request to save Room : {}", conferenceRoom);
            ConferenceRoom createdConferenceRoom = conferenceRoomService.save(conferenceRoom);
            return new ResponseEntity<>(createdConferenceRoom, HttpStatus.CREATED);
        } else throw new BadRequestException("Authorization header is invalid.");
    }

    @PutMapping()
    public ResponseEntity<ConferenceRoom> updateRoom(@RequestBody ConferenceRoom conferenceRoom, @RequestHeader String authorization) throws BadRequestException {
        if (authorization.equals("admin")) {
            log.debug("REST request to update Room : {}", conferenceRoom);
            ConferenceRoom updatedConferenceRoom = conferenceRoomService.update(conferenceRoom);
            return new ResponseEntity<>(updatedConferenceRoom, HttpStatus.OK);
        } else throw new BadRequestException("Authorization header is invalid.");
    }

    @PutMapping("/addEq/{conferenceRoomId}/{equipmentId}")
    public ResponseEntity<ConferenceRoom> addEquipment(@PathVariable Integer conferenceRoomId, @PathVariable Integer equipmentId, @RequestHeader String authorization) throws BadRequestException{
        if (authorization.equals("admin")) {
            log.debug("REST request to add equipment to Conference Room");
            ConferenceRoom ConfRoomWithAddedEq = conferenceRoomService.addEquipmentById(conferenceRoomId, equipmentId);
            return new ResponseEntity<>(ConfRoomWithAddedEq, HttpStatus.OK);
        } else throw new BadRequestException("Authorization header is invalid.");
    }
    @PutMapping("/deleteEq/{conferenceRoomId}/{equipmentId}")
    public ResponseEntity<ConferenceRoom> deleteEquipment(@PathVariable Integer conferenceRoomId, @PathVariable Integer equipmentId, @RequestHeader String authorization) throws BadRequestException{
        if (authorization.equals("admin")) {
            log.debug("REST request to delete equipment of Conference Room");
            ConferenceRoom ConfRoomWithDeletedEq = conferenceRoomService.deleteEquipmentById(conferenceRoomId, equipmentId);
            return new ResponseEntity<>(ConfRoomWithDeletedEq, HttpStatus.OK);
        } else throw new BadRequestException("Authorization header is invalid.");
    }

    @GetMapping("/allFromOffice/{officeId}")
    public List<ConferenceRoom> allFromOffice(@PathVariable Integer officeId) {
        log.debug("REST request to get all of Rooms from the office");
        return conferenceRoomService.findAllByOffice(officeId);
    }

    public List<ConferenceRoom> allFreeFromOffice(@PathVariable Integer officeId) {
        log.debug("REST request to get all free Rooms from the office");
        return conferenceRoomService.findAllFreeByOffice(officeId);
    }

    @GetMapping("/all")
    public List<ConferenceRoom> all() {
        log.debug("REST request to get all of Rooms");
        return conferenceRoomService.findAll();
    }

    @GetMapping("/{id}")
    public ConferenceRoom getRoom(@PathVariable Integer id) {
        log.debug("REST request to get Room : {}", id);
        return conferenceRoomService.findById(id);
    }

    @GetMapping("/{name}/{id}")
    public ConferenceRoom getRoomByNameAndOfficeId(@PathVariable String name, @PathVariable Integer officeId) {
        return conferenceRoomService.findRoomByNameAndOfficeId(name, officeId);
    }

    @DeleteMapping("/{id}")
    public void deleteRoom(@PathVariable Integer id, @RequestHeader String authorization) throws BadRequestException {
        if (authorization.equals("admin")) {
            log.debug("REST request to delete Room : {}", id);
            conferenceRoomService.delete(id);
        } else throw new BadRequestException("Authorization header is invalid.");
    }



    @GetMapping("/eq/{id}")
    public List<String> getEquipment(@PathVariable Integer id) throws BadRequestException {
        log.debug("REST request to get equipment for Room : {}", id);
        return conferenceRoomService.findEquipmentByRoomId(id);
    }
}
