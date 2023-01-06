package filiciak.cyran.demo.Controllers;

import filiciak.cyran.demo.Entities.Seat;
import filiciak.cyran.demo.Services.SeatService;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class SeatController {

    private final SeatService seatService;

    public SeatController(SeatService seatService) {
        this.seatService = seatService;
    }

    @GetMapping("/all")
    public List<Seat> all() {
        return seatService.findAll();
    }
}
