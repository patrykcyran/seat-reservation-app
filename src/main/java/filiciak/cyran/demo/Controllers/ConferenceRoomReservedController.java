package filiciak.cyran.demo.Controllers;


import filiciak.cyran.demo.Entities.ConferenceRoomReserved;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.Services.ConferenceRoomReservedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/roomReserved")
public class ConferenceRoomReservedController {
    private final Logger log = LoggerFactory.getLogger(ConferenceRoomReservedController.class);

    private final ConferenceRoomReservedService conferenceRoomReservedService;


    public ConferenceRoomReservedController(ConferenceRoomReservedService conferenceRoomReservedService) {
        this.conferenceRoomReservedService = conferenceRoomReservedService;
    }


    @PostMapping
    public ResponseEntity<ConferenceRoomReserved> createReservation(@RequestBody ConferenceRoomReserved conferenceRoomReserved) throws BadRequestException {
        log.debug("REST request to create reservation : {}", conferenceRoomReserved);
        ConferenceRoomReserved createdReservation = conferenceRoomReservedService.save(conferenceRoomReserved);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }


    @PutMapping("/cancel/{id}")
    public ResponseEntity<ConferenceRoomReserved> cancelReservation(@PathVariable Integer id) throws BadRequestException {
        log.debug("REST request to cancel reservation");
        ConferenceRoomReserved canceledReservation = conferenceRoomReservedService.cancel(id);
        return new ResponseEntity<>(canceledReservation, HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<ConferenceRoomReserved> allForUser() {
        log.debug("REST request to get all of Seats reservation for current user");
        return conferenceRoomReservedService.findAllByUser();
    }

    @GetMapping("/{id}")
    public ConferenceRoomReserved getSeatForUser(@PathVariable Integer id) throws BadRequestException {
        log.debug("REST request to get Seats reservation by id");
        return conferenceRoomReservedService.findById(id);
    }
}
