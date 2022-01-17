package me.polyfrontier.casparwia2.controller;

import me.polyfrontier.casparwia2.model.UserEntity;
import me.polyfrontier.casparwia2.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.DataIntegrityViolationException;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import javax.validation.Valid;
import java.util.List;
import java.util.Optional;

@RestController
@RequestMapping(value = "/user", consumes = {MediaType.APPLICATION_JSON_VALUE, MediaType.APPLICATION_FORM_URLENCODED_VALUE}, produces = MediaType.APPLICATION_JSON_VALUE)
public class UserController {
    private final UserRepository userRepository;
    private final UserEntity currentUser;

    @Autowired
    public UserController(UserRepository userRepository, UserEntity currentUser) {
        this.userRepository = userRepository;
        this.currentUser = currentUser;
    }

    /**
     * Get all the users in database
     * @return All the users in database if the caller has the right to do it. Else an error is returned
     */
    @GetMapping("/all")
    public ResponseEntity<List<UserEntity>> getAll() {
        if (!currentUser.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You (" + currentUser.getToken() + ") are not authorized to perform this action as a " + currentUser.getRole() + " user");
        }
        List<UserEntity> listUserEntities = userRepository.findAll();
        return new ResponseEntity<>(listUserEntities, HttpStatus.OK);
    }

    /**
     * Get the user with the given username
     * @param username The username to search
     * @return The user with the given username if it exists, else an error. If the caller doesn't have the right, an error is returned
     */
    @GetMapping("/{username}")
    public ResponseEntity<UserEntity> getByUsername(@PathVariable("username") String username) {
        if (currentUser.isAdmin() || currentUser.getUsername().equals(username)) {

            Optional<UserEntity> optional = userRepository.findByUsername(username);
            if (optional.isPresent()) {
                return new ResponseEntity<>(optional.get(), HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The requested user does not exist");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You (" + currentUser.getToken() + ") are not authorized to perform this action as a " + currentUser.getRole() + " user");
        }
    }

    /**
     * Register a new user
     *
     * @param userEntity The user to register
     * @return The registered user or an error if the caller doesn't have the right
     */
    @PostMapping("")
    public ResponseEntity<UserEntity> register(@Valid @RequestBody UserEntity userEntity) {
        if (!currentUser.isAdmin()) {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You (" + currentUser.getToken() + ") are not authorized to perform this action as a " + currentUser.getRole() + " user");
        }

        try {
            userRepository.save(userEntity);
            return new ResponseEntity<>(userEntity, HttpStatus.CREATED);
        } catch (DataIntegrityViolationException ce) {
            if (ce.getLocalizedMessage().contains("user_username_uindex")) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The username is already taken");
            } else {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The fields are not valid");
            }
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't register this user: " + e.getMessage());
        }
    }

}
