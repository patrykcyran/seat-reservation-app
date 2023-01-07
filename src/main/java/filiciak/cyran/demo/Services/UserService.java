package filiciak.cyran.demo.Services;

import filiciak.cyran.demo.Entities.User;
import filiciak.cyran.demo.Exceptions.BadRequestException;
import filiciak.cyran.demo.Repositories.UserRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class UserService {

    private final Logger log = LoggerFactory.getLogger(UserService.class);
    private final UserRepository userRepository;

    public UserService(UserRepository userRepository) {
        this.userRepository = userRepository;
    }

    public User save(User user) throws BadRequestException {
        if(user.getId() != null){
            throw new BadRequestException("A new user cannot already have an ID");
        }
        if(userRepository.findByUsername(user.getUsername()).isPresent()){
            throw new BadRequestException("User with this username exist in this office");
        }
        log.debug("Request to save user : {} ", user);
        return userRepository.save(user);
    }

    public User update(User user) throws BadRequestException {
        if(user.getId() == null){
            throw new BadRequestException("User does not exist");
        }
        if(!userRepository.existsById(user.getId())){
            throw new BadRequestException("User with id " + user.getId() + " does not exist");
        }
        return userRepository.save(user);
    }

    public List<User> findAll(){
        log.debug("Request to get all Users");
        return userRepository.findAll();
    }

    public User findById(Integer id) {
        log.debug("Request to get User : {}", id);
        return userRepository.findById(id).get();
    }

    public User findByUsername(String username) {
        log.debug("Request to get User : {}", username);
        return userRepository.findByUsername(username).get();
    }

    public boolean userExists(String username) {
        Optional<User> user = userRepository.findByUsername(username);
        return user.isPresent();
    }

    public void delete(Integer id) throws BadRequestException {
        User user = userRepository.getReferenceById(id);
        log.debug("Request to delete User : {} ", id);
        userRepository.deleteById(id);
    }
}
