package filiciak.cyran.demo.Services;

import filiciak.cyran.demo.Entities.Address;
import filiciak.cyran.demo.Entities.Office;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.Repositories.AddressRepository;
import filiciak.cyran.demo.Repositories.OfficeRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;


@Service
public class AddressService {

    private final Logger log = LoggerFactory.getLogger(AddressService.class);
    private final AddressRepository addressRepository;
    private final OfficeRepository officeRepository;

    public AddressService(AddressRepository addressRepository, OfficeRepository officeRepository) {
        this.addressRepository = addressRepository;
        this.officeRepository = officeRepository;
    }

    public Address save(Address address) throws BadRequestException {
        if(address.getId() != null){
            throw new BadRequestException("A new seat cannot already have an ID");
        }
        log.debug("Request to save address : {} ", address);
        return addressRepository.save(address);
    }

    public Address update(Address address) throws BadRequestException {
        if(address.getId() == null){
            throw new BadRequestException("Address does not exist");
        }
        if(!addressRepository.existsById(address.getId())){
            throw new BadRequestException("Address with id " + address.getId() + "does not exist");
        }
        return addressRepository.save(address);
    }

    public List<Address> findAll(){
        log.debug("Request to get all Addresses");
        return addressRepository.findAll();
    }
    public Address findById(Integer id) throws BadRequestException {
        if(!addressRepository.existsById(id)){
            throw new BadRequestException("Address with id " + id + "  does not exist");
        }
        log.debug("Request to get Address : {}", id);
        return addressRepository.findById(id).get();
    }

    public void delete(Integer id) throws BadRequestException {
        Optional<Office> office = Optional.ofNullable(officeRepository.getOfficeByAddressId(id));
        if(office.isPresent()){
            throw new BadRequestException("Address has children (first delete office)");
        }
        log.debug("Request to delete Address : {} ", id);
        addressRepository.deleteById(id);
    }
}
