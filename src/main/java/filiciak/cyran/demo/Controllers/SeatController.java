package filiciak.cyran.demo.Controllers;

import filiciak.cyran.demo.Entities.Seat;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.Services.SeatService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/seat")
public class SeatController {

    private final Logger log = LoggerFactory.getLogger(SeatController.class);

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }


    @PostMapping
    public ResponseEntity<Seat> createSeat(@RequestBody Seat seat, @RequestHeader String authorization) throws BadRequestException {
        if (authorization.equals("admin")) {
            log.debug("REST request to save Seat : {}", seat);
            Seat createdSeat = seatService.save(seat);
            return new ResponseEntity<>(createdSeat, HttpStatus.CREATED);
        } else throw new BadRequestException("Authorization header is invalid.");
    }

    @PutMapping()
    public ResponseEntity<Seat> updateSeat(@RequestBody Seat seat, @RequestHeader String authorization) throws BadRequestException {
        if (authorization.equals("admin")) {
            log.debug("REST request to update Seat : {}", seat);
            Seat updatedSeat = seatService.update(seat);
            return new ResponseEntity<>(updatedSeat, HttpStatus.OK);
        } else throw new BadRequestException("Authorization header is invalid.");
    }

    @PutMapping("/addEq/{seatId}/{equipmentId}")
    public ResponseEntity<Seat> addEquipment(@PathVariable Integer seatId, @PathVariable Integer equipmentId, @RequestHeader String authorization) throws BadRequestException{
        if (authorization.equals("admin")) {
            log.debug("REST request to add equipment to seat");
            Seat seatWithAddedEq = seatService.addEquipmentById(seatId, equipmentId);
            return new ResponseEntity<>(seatWithAddedEq, HttpStatus.OK);
        } else throw new BadRequestException("Authorization header is invalid.");
    }
    @PutMapping("/deleteEq/{seatId}/{equipmentId}")
    public ResponseEntity<Seat> deleteEquipment(@PathVariable Integer seatId, @PathVariable Integer equipmentId, @RequestHeader String authorization) throws BadRequestException{
        if (authorization.equals("admin")) {
            log.debug("REST request to delete equipment of seat");
            Seat seatWithDeletedEq = seatService.deleteEquipmentById(seatId, equipmentId);
            return new ResponseEntity<>(seatWithDeletedEq, HttpStatus.OK);
        } else throw new BadRequestException("Authorization header is invalid.");
    }
    @GetMapping("/allFromOffice/{officeId}")
    public List<Seat> allFromOffice(@PathVariable Integer officeId) {
        log.debug("REST request to get all of Seats from the office");
        return seatService.findAllByOffice(officeId);
    }

    @GetMapping("/{officeId}")
    public List<Seat> allFreeFromOffice(@PathVariable Integer officeId) {
        log.debug("REST request to get all free Seats from the office");
        return seatService.findAllFreeByOffice(officeId);
    }

    @GetMapping("/all")
    public List<Seat> all() {
        log.debug("REST request to get all of Seats");
        return seatService.findAll();
    }

    @GetMapping("/{id}")
    public Seat getSeat(@PathVariable Integer id) throws BadRequestException {
        log.debug("REST request to get Seat : {}", id);
        return seatService.findById(id);
    }

    @GetMapping("/{name}")
    public Seat getSeatByNumberAndOfficeId(@PathVariable Integer number, Integer officeId){
        return seatService.findSeatBySeatNumberAndOfficeId(number, officeId);
    }

    @DeleteMapping("/{id}")
    public void deleteSeat(@PathVariable Integer id, @RequestHeader String authorization) throws BadRequestException {
        if (authorization.equals("admin")) {
            log.debug("REST request to delete Seat : {}", id);
            seatService.delete(id);
        } else throw new BadRequestException("Authorization header is invalid.");
    }

    @GetMapping("/eq/{id}")
    public List<String> getEquipment(@PathVariable Integer id) throws BadRequestException {
        log.debug("REST request to get equipment for Seat : {}", id);
        return seatService.findEquipmentBySeatId(id);
    }

    @GetMapping("/max")
    public Seat getSeatWithHighestNumber() {
        Seat seat = seatService.findSeatWithHighestNumber();
        if (seat == null){
            Seat newSeat = new Seat();
            newSeat.setSeatNumber(0);
            return newSeat;
        }
        return seat;
    }
}
