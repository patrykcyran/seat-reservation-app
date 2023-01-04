package filiciak.cyran.demo.Controllers;

import filiciak.cyran.demo.Repositories.SeatRepository;
import filiciak.cyran.demo.Entities.Seat;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
public class Controller {

    private final SeatRepository repository;

    public Controller(SeatRepository repository) {
        this.repository = repository;
    }

    @GetMapping("/all")
    public List<Seat> all() {
        return repository.findAll();
    }
}
