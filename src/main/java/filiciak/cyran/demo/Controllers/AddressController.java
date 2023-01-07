package filiciak.cyran.demo.Controllers;

import filiciak.cyran.demo.Entities.Address;
import filiciak.cyran.demo.Entities.Seat;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.Services.AddressService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/addresses")
public class AddressController {

    private final Logger log = LoggerFactory.getLogger(AddressController.class);
    private final AddressService addressService;

    public AddressController(AddressService addressService) {
        this.addressService = addressService;
    }

    @PostMapping
    public ResponseEntity<Address> createAddress(@RequestBody Address address, @RequestHeader String authorization) throws BadRequestException {
        if (authorization.equals("admin")) {
            log.debug("REST request to save Address : {}", address);
            Address createdAddress = addressService.save(address);
            return new ResponseEntity<>(createdAddress, HttpStatus.CREATED);
        }
        else throw new BadRequestException("Authorization header is invalid.");
    }

    @PutMapping()
    public ResponseEntity<Address> updateAddress(@RequestBody Address address, @RequestHeader String authorization) throws BadRequestException {
        if (authorization.equals("admin")) {
            log.debug("REST request to update Address : {}", address);
            Address updatedAddress = addressService.update(address);
            return new ResponseEntity<>(updatedAddress, HttpStatus.OK);
        }
        else throw new BadRequestException("Authorization header is invalid.");
    }

    @GetMapping("/all")
    public List<Address> all() {
        log.debug("REST request to get all of Addresses");
        return addressService.findAll();
    }

    @GetMapping("/{id}")
    public Address getAddress(@PathVariable Integer id) throws BadRequestException {
        log.debug("REST request to get Address : {}", id);
        return addressService.findById(id);
    }

    @DeleteMapping("/{id}")
    public void deleteAddress(@PathVariable Integer id, @RequestHeader String authorization) throws BadRequestException {
        if (authorization.equals("admin")) {
            log.debug("REST request to delete Address : {}", id);
            addressService.delete(id);
        } else throw new BadRequestException("Authorization header is invalid.");
    }

}
