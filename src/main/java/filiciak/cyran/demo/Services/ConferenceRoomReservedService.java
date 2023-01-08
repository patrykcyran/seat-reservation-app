package filiciak.cyran.demo.Services;

import filiciak.cyran.demo.Entities.*;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.Repositories.ConferenceRoomReservedRepository;
import filiciak.cyran.demo.Repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class ConferenceRoomReservedService {
    private final Logger log = LoggerFactory.getLogger(ConferenceRoomReservedService.class);
    private final ConferenceRoomReservedRepository conferenceRoomReservedRepository;
    private final ConferenceRoomService conferenceRoomService;
    private final UserRepository userRepository;


    public ConferenceRoomReservedService(ConferenceRoomReservedRepository conferenceRoomReservedRepository, ConferenceRoomService conferenceRoomService, UserRepository userRepository) {
        this.conferenceRoomReservedRepository = conferenceRoomReservedRepository;
        this.conferenceRoomService = conferenceRoomService;
        this.userRepository = userRepository;
    }

    public ConferenceRoomReserved save(ConferenceRoomReserved conferenceRoomReserved) throws BadRequestException {
        Integer conferenceRoomReservedId = conferenceRoomReserved.getConferenceRoom().getId();
        String currentUser = UserInstance.getInstance().getUsername();
        if (!userRepository.findByUsername(currentUser).isPresent()){
            throw new BadRequestException("User with username " + currentUser + "  does not exist");
        }
        if (conferenceRoomService.findById(conferenceRoomReservedId) == null){
            throw new BadRequestException("Room with id " + conferenceRoomReservedId + "  does not exist");
        }
        if (conferenceRoomService.findById(conferenceRoomReserved.getConferenceRoom().getId()).getStatus() != AvailabilityStatus.FREE){
            throw new BadRequestException("Room with id " + conferenceRoomReservedId + "  is OCCUPIED or UNAVAILABLE");
        }
        if (!seatReservedDateValidation(conferenceRoomReserved.getFromDate(),conferenceRoomReserved.getToDate()) || seatReservedFromDateInPastValidation(conferenceRoomReserved.getFromDate())){
            throw new BadRequestException("FromDate or ToDate time is invalid");
        }
        ConferenceRoom conferenceRoom = conferenceRoomService.findById(conferenceRoomReservedId);
        conferenceRoom.setStatus(AvailabilityStatus.OCCUPIED);
        conferenceRoomService.update(conferenceRoom);
        conferenceRoomReserved.setConferenceRoom(conferenceRoom);
        conferenceRoomReserved.setUser(userRepository.findByUsername(currentUser).get());
        conferenceRoomReserved.setStatus(ReservationStatus.ACTIVE);
        return conferenceRoomReservedRepository.save(conferenceRoomReserved);
    }


    public List<ConferenceRoomReserved> findAllByUser(){
        log.debug("Request to get all Conference Room reservation");
        return conferenceRoomReservedRepository.findRoomByUser((UserInstance.getInstance().getUsername()));
    }

    public ConferenceRoomReserved findById(Integer id) throws BadRequestException {
        String currentUser = UserInstance.getInstance().getUsername();
        if(!conferenceRoomReservedRepository.existsById(id)){
            throw new BadRequestException("Room reservation with id " + id + " does not exist");
        }
        if (!conferenceRoomReservedRepository.findById(id).get().getUser().getUsername().equals(currentUser)){
            throw new BadRequestException("This is not your reservation, you can not cancel it");
        }
        log.debug("Request to get Room reservation");
        return conferenceRoomReservedRepository.findById(id).get();
    }

    public ConferenceRoomReserved cancel(Integer id) throws BadRequestException {
        String currentUser = UserInstance.getInstance().getUsername();
        if(!conferenceRoomReservedRepository.existsById(id)){
            throw new BadRequestException("Room reservation with id " + id + " does not exist");
        }
        if (!userRepository.findByUsername(currentUser).isPresent()){
            throw new BadRequestException("User with username " + currentUser + "  does not exist");
        }
        if (!conferenceRoomReservedRepository.findById(id).get().getUser().getUsername().equals(currentUser)){
            throw new BadRequestException("This is not your reservation, you can not cancel it");
        }

        ConferenceRoomReserved conferenceRoomReserved = conferenceRoomReservedRepository.findById(id).get();
        conferenceRoomReserved.setStatus(ReservationStatus.CANCELED);
        ConferenceRoom conferenceRoom = conferenceRoomService.findById(conferenceRoomReserved.getConferenceRoom().getId());
        conferenceRoom.setStatus(AvailabilityStatus.FREE);
        conferenceRoomService.update(conferenceRoom);
        return conferenceRoomReservedRepository.save(conferenceRoomReserved);
    }

    public boolean seatReservedDateValidation(LocalDate fromDate, LocalDate toDate) {
        return fromDate.compareTo(toDate) <= 0;
    }

    public boolean seatReservedFromDateInPastValidation(LocalDate fromDate) {
        return LocalDate.now().compareTo(fromDate) >= 0;
    }
}
