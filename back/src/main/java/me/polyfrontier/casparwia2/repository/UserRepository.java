package me.polyfrontier.casparwia2.repository;

import me.polyfrontier.casparwia2.model.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Component;

import java.util.Optional;

@Component("userRepository")
public interface UserRepository extends JpaRepository<UserEntity, Long> {
    /**
     * Get the user with the given token
     * @param token The token to search for
     * @return the user with the given token or empty
     */
    Optional<UserEntity> findByToken(String token);

    /**
     * Get the user with the given username
     * @param username The username to search for
     * @return the user with the given username or empty
     */
    Optional<UserEntity> findByUsername(String username);
}
