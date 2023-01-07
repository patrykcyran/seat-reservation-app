package filiciak.cyran.demo.Controllers;

import filiciak.cyran.demo.Entities.ConferenceRoom;
import filiciak.cyran.demo.Entities.Seat;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.Services.ConferenceRoomService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/rooms")
public class ConferenceRoomController {
    private final Logger log = LoggerFactory.getLogger(SeatController.class);
    private final ConferenceRoomService conferenceRoomService;

    public ConferenceRoomController(ConferenceRoomService conferenceRoomService) {this.conferenceRoomService = conferenceRoomService;}

    @PostMapping
    public ResponseEntity<ConferenceRoom> createSeat(@RequestBody ConferenceRoom conferenceRoom, @RequestHeader String authorization) throws BadRequestException {
        if (authorization.equals("admin")) {
            log.debug("REST request to save Room : {}", conferenceRoom);
            ConferenceRoom createdConferenceRoom = conferenceRoomService.save(conferenceRoom);
            return new ResponseEntity<>(createdConferenceRoom, HttpStatus.CREATED);
        } else throw new BadRequestException("Authorization header is invalid.");
    }

    @PutMapping()
    public ResponseEntity<ConferenceRoom> updateSeat(@RequestBody ConferenceRoom conferenceRoom, @RequestHeader String authorization) throws BadRequestException {
        if (authorization.equals("admin")) {
            log.debug("REST request to update Room : {}", conferenceRoom);
            ConferenceRoom updatedConferenceRoom = conferenceRoomService.update(conferenceRoom);
            return new ResponseEntity<>(updatedConferenceRoom, HttpStatus.OK);
        } else throw new BadRequestException("Authorization header is invalid.");
    }

    @GetMapping("/allFromOffice/{officeId}")
    public List<ConferenceRoom> allFromOffice(@PathVariable Integer officeId) {
        log.debug("REST request to get all of Rooms from the office");
        return conferenceRoomService.findAllByOffice(officeId);
    }

    @GetMapping("/all")
    public List<ConferenceRoom> all() {
        log.debug("REST request to get all of Rooms");
        return conferenceRoomService.findAll();
    }

    @GetMapping("/{id}")
    public ConferenceRoom getSeat(@PathVariable Integer id) {
        log.debug("REST request to get Room : {}", id);
        return conferenceRoomService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteSeat(@PathVariable Integer id, @RequestHeader String authorization) throws BadRequestException {
        if (authorization.equals("admin")) {
            log.debug("REST request to delete Room : {}", id);
            conferenceRoomService.delete(id);
        } else throw new BadRequestException("Authorization header is invalid.");
    }
}
