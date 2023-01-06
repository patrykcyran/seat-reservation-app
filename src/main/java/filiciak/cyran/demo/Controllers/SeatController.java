package filiciak.cyran.demo.Controllers;

import filiciak.cyran.demo.Entities.Office;
import filiciak.cyran.demo.Entities.Seat;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.Services.OfficeService;
import filiciak.cyran.demo.Services.SeatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/seats")
public class SeatController {

    private final Logger log = LoggerFactory.getLogger(SeatController.class);

    private final SeatService seatService;
    private final OfficeService officeService;

    public SeatController(SeatService seatService, OfficeService officeService) {
        this.seatService = seatService;
        this.officeService = officeService;
    }


    @PostMapping
    public ResponseEntity<Seat> createSeat(@RequestBody Seat seat) throws BadRequestException {
        log.debug("REST request to save Seat : {}", seat);
        Seat createdSeat = seatService.save(seat);
        return new ResponseEntity<>(createdSeat, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Seat> updateSeat(@PathVariable Integer id, @RequestBody Seat seat) throws BadRequestException {
        log.debug("REST request to update Seat : {}, {}", id, seat);
        Seat updatedSeat = seatService.update(seat, id);
        return new ResponseEntity<>(updatedSeat, HttpStatus.OK);
    }

    @GetMapping("/allFromOffice/{officeId}")
    public List<Seat> allFromOffice(@PathVariable Integer officeId){
        log.debug("REST request to get all of Seats from the office");
        return seatService.findAllByOffice(officeId);
    }
    @GetMapping("/all")
    public List<Seat> all() {
        log.debug("REST request to get all of Seats");
        return seatService.findAll();
    }

    @GetMapping("/{id}")
    public Seat getSeat(@PathVariable Integer id){
        log.debug("REST request to get Seat : {}", id);
        return seatService.findById(id);
    }
}
