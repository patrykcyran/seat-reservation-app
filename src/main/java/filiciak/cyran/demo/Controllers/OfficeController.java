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
    public ResponseEntity<Office> createOffice(@RequestBody Office office) throws BadRequestException {
        log.debug("REST request to save Office : {}", office);
        Office createdOffice = officeService.save(office);
        return new ResponseEntity<>(createdOffice, HttpStatus.CREATED);
    }

    @PutMapping("/{id}")
    public ResponseEntity<Office> updateOffice(@PathVariable Integer id, @RequestBody Office office) throws BadRequestException {
        log.debug("REST request to update Office : {}, {}", id, office);
        Office updatedOffice = officeService.update(office, id);
        return new ResponseEntity<>(updatedOffice, HttpStatus.OK);
    }

    @GetMapping("/all")
    public List<Office> all() {
        log.debug("REST request to get all of Offices");
        return officeService.findAll();
    }

    @GetMapping("/{id}")
    public Office getOffice(@PathVariable Integer id){
        log.debug("REST request to get Office : {}", id);
        return officeService.findById(id);
    }
}
