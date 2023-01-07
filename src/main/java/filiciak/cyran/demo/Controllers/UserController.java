package filiciak.cyran.demo.Controllers;

import filiciak.cyran.demo.Entities.Seat;
import filiciak.cyran.demo.Entities.User;
import filiciak.cyran.demo.Entities.UserInstance;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.Services.UserService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@RequestMapping("/user")
public class UserController {

    private final Logger log = LoggerFactory.getLogger(UserController.class);

    private final UserService userService;

    public UserController(UserService userService) {
        this.userService = userService;
    }

    @PostMapping
    public ResponseEntity<User> createUser(@RequestBody User user, @RequestHeader String authorization) throws BadRequestException {
        if (authorization.equals("admin")) {
            log.debug("REST request to save User : {}", user);
            User createdUser = userService.save(user);
            return new ResponseEntity<>(createdUser, HttpStatus.CREATED);
        } else throw new BadRequestException("Authorization header is invalid.");
    }

    @PutMapping()
    public ResponseEntity<User> updateUser(@RequestBody User user, @RequestHeader String authorization) throws BadRequestException {
        if (authorization.equals("admin")) {
            log.debug("REST request to update User : {}", user);
            User updatedUser = userService.update(user);
            return new ResponseEntity<>(updatedUser, HttpStatus.OK);
        } else throw new BadRequestException("Authorization header is invalid.");
    }

    @GetMapping("/all")
    public List<User> all() {
        log.debug("REST request to get all of Users");
        return userService.findAll();
    }

    @GetMapping("/{id}")
    public User getUser(@PathVariable Integer id) {
        log.debug("REST request to get User : {}", id);
        return userService.findById(id);
    }

    @GetMapping("/{username}")
    public User getUser(@PathVariable String username) {
        log.debug("REST request to get User : {}", username);
        return userService.findByUsername(username);
    }

    @GetMapping("/exists/{username}")
    public boolean userExists(@PathVariable String username) {
        log.debug("REST request to check if user exists : {}", username);
        return userService.userExists(username);
    }

    @DeleteMapping("/{id}")
    public void deleteUser(@PathVariable Integer id, @RequestHeader String authorization) throws BadRequestException {
        if (authorization.equals("admin")) {
            log.debug("REST request to delete User : {}", id);
            userService.delete(id);
            if (id == UserInstance.getInstance().getId()) {
                UserInstance.getInstance().setLogged(false);
            }
        } else throw new BadRequestException("Authorization header is invalid.");
    }
}
