package filiciak.cyran.demo.Services;

import filiciak.cyran.demo.Entities.*;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.Repositories.SeatReservedRepository;
import filiciak.cyran.demo.Repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class SeatReservedService {

    private final Logger log = LoggerFactory.getLogger(SeatReservedService.class);
    private final SeatReservedRepository seatReservedRepository;
    private final SeatService seatService;
    private final UserRepository userRepository;

    public SeatReservedService(SeatReservedRepository seatReservedRepository, SeatService seatService, UserRepository userRepository) {
        this.seatReservedRepository = seatReservedRepository;
        this.seatService = seatService;
        this.userRepository = userRepository;
    }

    public SeatReserved save(SeatReserved seatReserved) throws BadRequestException{
        Integer seatId = seatReserved.getSeat().getId();
        String currentUser = UserInstance.getInstance().getUsername();
        if (!userRepository.findByUsername(currentUser).isPresent()){
            throw new BadRequestException("User with username " + currentUser + "  does not exist");
        }
        if (seatService.findById(seatId) == null){
            throw new BadRequestException("Seat with id " + seatId + "  does not exist");
        }
        if (seatService.findById(seatReserved.getSeat().getId()).getStatus() != AvailabilityStatus.FREE){
            throw new BadRequestException("Seat with id " + seatId + "  is OCCUPIED or UNAVAILABLE");
        }
        if (!seatReservedDateValidation(seatReserved.getFromDate(),seatReserved.getToDate()) || seatReservedFromDateInPastValidation(seatReserved.getFromDate())){
            throw new BadRequestException("FromDate or ToDate time is invalid");
        }
        Seat seat = seatService.findById(seatId);
        seat.setStatus(AvailabilityStatus.OCCUPIED);
        seatService.update(seat);
        seatReserved.setSeat(seat);
        seatReserved.setUser(userRepository.findByUsername(currentUser).get());
        seatReserved.setStatus(ReservationStatus.ACTIVE);
        return seatReservedRepository.save(seatReserved);
    }


    public List<SeatReserved> findAllByUser(){
        log.debug("Request to get all Seat reservation");
        return seatReservedRepository.findSeatsByUser(UserInstance.getInstance().getUsername());
    }

    public SeatReserved findById(Integer id) throws BadRequestException {
        String currentUser = UserInstance.getInstance().getUsername();
        if(!seatReservedRepository.existsById(id)){
            throw new BadRequestException("Seat reservation with id " + id + " does not exist");
        }
        if (!seatReservedRepository.findById(id).get().getUser().getUsername().equals(currentUser)){
            throw new BadRequestException("This is not your reservation, you can not cancel it");
        }
        log.debug("Request to get Seat reservation");
        return seatReservedRepository.findById(id).get();
    }
    public SeatReserved cancel(Integer id) throws BadRequestException {
        String currentUser = UserInstance.getInstance().getUsername();
        if(!seatReservedRepository.existsById(id)){
            throw new BadRequestException("Seat reservation with id " + id + " does not exist");
        }
        if (!userRepository.findByUsername(currentUser).isPresent()){
            throw new BadRequestException("User with username " + currentUser + "  does not exist");
        }
        if (!seatReservedRepository.findById(id).get().getUser().getUsername().equals(currentUser)){
            throw new BadRequestException("This is not your reservation, you can not cancel it");
        }

        SeatReserved seatReserved = seatReservedRepository.findById(id).get();
        seatReserved.setStatus(ReservationStatus.CANCELED);
        Seat seat = seatService.findById(seatReserved.getSeat().getId());
        seat.setStatus(AvailabilityStatus.FREE);
        seatService.update(seat);
        return seatReservedRepository.save(seatReserved);
    }

    public boolean seatReservedDateValidation(LocalDate fromDate, LocalDate toDate) {
        return fromDate.compareTo(toDate) <= 0;
    }

    public boolean seatReservedFromDateInPastValidation(LocalDate fromDate) {
        return LocalDate.now().compareTo(fromDate) >= 0;
    }
}


