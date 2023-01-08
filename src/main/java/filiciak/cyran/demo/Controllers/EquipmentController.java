package filiciak.cyran.demo.Controllers;

import filiciak.cyran.demo.Entities.Address;
import filiciak.cyran.demo.Entities.Equipment;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.Services.EquipmentService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/equipments")
public class EquipmentController {

    private final Logger log = LoggerFactory.getLogger(EquipmentController.class);
    private final EquipmentService equipmentService;

    public EquipmentController(EquipmentService equipmentService) {
        this.equipmentService = equipmentService;
    }

    @PostMapping
    public ResponseEntity<Equipment> createEquipment(@RequestBody Equipment equipment, @RequestHeader String authorization) throws BadRequestException {
        if (authorization.equals("admin")) {
            log.debug("REST request to save Equipment : {}", equipment);
            Equipment createdEquipment = equipmentService.save(equipment);
            return new ResponseEntity<>(createdEquipment, HttpStatus.CREATED);
        }
        else throw new BadRequestException("Authorization header is invalid.");
    }
    @PutMapping
    public ResponseEntity<Equipment> updateEquipment(@RequestBody Equipment equipment, @RequestHeader String authorization) throws BadRequestException {
        if (authorization.equals("admin")) {
            log.debug("REST request to update Equipment : {}", equipment);
            Equipment updatedEquipment = equipmentService.save(equipment);
            return new ResponseEntity<>(updatedEquipment, HttpStatus.CREATED);
        }
        else throw new BadRequestException("Authorization header is invalid.");
    }
    @GetMapping("/all")
    public List<Equipment> all() {
        log.debug("REST request to get all of Equipments");
        return equipmentService.findAll();
    }

    @GetMapping("/{id}")
    public Equipment getEquipment(@PathVariable Integer id) throws BadRequestException {
        log.debug("REST request to get Equipment : {}", id);
        return equipmentService.findById(id);
    }

    @GetMapping("/{name}")
     public Equipment getEquipmentByName(@PathVariable String name) throws BadRequestException {
        log.debug("REST request to get Equipment by name: {}", name);
        return equipmentService.findByName(name);
    }

    @DeleteMapping("/{id}")
    public void deleteEquipment(@PathVariable Integer id, @RequestHeader String authorization) throws BadRequestException {
        if (authorization.equals("admin")) {
            log.debug("REST request to delete Equipment : {}", id);
            equipmentService.delete(id);
        } else throw new BadRequestException("Authorization header is invalid.");
    }

}
