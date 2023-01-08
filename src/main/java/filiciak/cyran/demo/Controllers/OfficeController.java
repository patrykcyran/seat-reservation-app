package filiciak.cyran.demo.Controllers;

import filiciak.cyran.demo.Entities.Office;
import filiciak.cyran.demo.Entities.Seat;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.Services.OfficeService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping("/offices")
public class OfficeController {

    private final Logger log = LoggerFactory.getLogger(OfficeController.class);

    private final OfficeService officeService;

    public OfficeController(OfficeService officeService) {
        this.officeService = officeService;
    }

    @PostMapping
    public ResponseEntity<Office> createOffice(@RequestBody Office office, @RequestHeader String authorization) throws BadRequestException {
        if (authorization.equals("admin")) {
            log.debug("REST request to save Office : {}", office);
            Office createdOffice = officeService.save(office);
            return new ResponseEntity<>(createdOffice, HttpStatus.CREATED);
        } else throw new BadRequestException("Authorization header is invalid.");
    }

    @PutMapping()
    public ResponseEntity<Office> updateOffice( @RequestBody Office office, @RequestHeader String authorization) throws BadRequestException {
        if (authorization.equals("admin")) {
            log.debug("REST request to update Office : {}", office);
            Office updatedOffice = officeService.update(office);
            return new ResponseEntity<>(updatedOffice, HttpStatus.OK);
        } else throw new BadRequestException("Authorization header is invalid.");
    }

    @GetMapping("/all")
    public List<Office> all() {
        log.debug("REST request to get all of Offices");
        return officeService.findAll();
    }

    @GetMapping("/{id}")
    public Office getOffice(@PathVariable Integer id) throws BadRequestException {
        log.debug("REST request to get Office : {}", id);
        return officeService.findById(id);
    }

    @GetMapping("/{string}")
    public Office getOfficeByName(@PathVariable String name) throws BadRequestException {
        log.debug("REST request to get Office id by : {}", name);
        return officeService.findOfficeByName(name);
    }

    @DeleteMapping("/{id}")
    public void deleteOffice(@PathVariable Integer id, @RequestHeader String authorization) throws BadRequestException {
        if (authorization.equals("admin")) {
            log.debug("REST request to delete Office : {}", id);
            officeService.delete(id);
        } else throw new BadRequestException("Authorization header is invalid.");
    }
}
