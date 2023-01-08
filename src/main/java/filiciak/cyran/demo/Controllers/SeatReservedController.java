package filiciak.cyran.demo.Controllers;

import filiciak.cyran.demo.Entities.SeatReserved;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.Services.SeatReservedService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seatReserved")
public class SeatReservedController {

    private final Logger log = LoggerFactory.getLogger(SeatReservedController.class);

    private final SeatReservedService seatReservedService;


    public SeatReservedController(SeatReservedService seatReservedService) {
        this.seatReservedService = seatReservedService;
    }


    @PostMapping
    public ResponseEntity<SeatReserved> createReservation(@RequestBody SeatReserved seatReserved) throws BadRequestException {
        log.debug("REST request to create reservation : {}", seatReserved);
        SeatReserved createdReservation = seatReservedService.save(seatReserved);
        return new ResponseEntity<>(createdReservation, HttpStatus.CREATED);
    }


    @PutMapping("/cancel/{id}")
    public ResponseEntity<SeatReserved> cancelReservation(@PathVariable Integer id) throws BadRequestException {
        log.debug("REST request to cancel reservation");
        SeatReserved canceledReservation = seatReservedService.cancel(id);
        return new ResponseEntity<>(canceledReservation, HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<SeatReserved> allForUser() {
        log.debug("REST request to get all of Seats reservation for current user");
        return seatReservedService.findAllByUser();
    }

    @GetMapping("/{id}")
    public SeatReserved getSeatForUser(@PathVariable Integer id) throws BadRequestException {
        log.debug("REST request to get Seats reservation by id");
        return seatReservedService.findById(id);
    }
}
