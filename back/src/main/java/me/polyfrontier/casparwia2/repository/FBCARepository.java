package me.polyfrontier.casparwia2.repository;

import me.polyfrontier.casparwia2.model.FBCAEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FBCARepository extends JpaRepository<FBCAEntity, Long> {
    /**
     * Get all the FBCAs with the given passport number
     * @param passportNumber The passport number to search for
     * @return all the FBCAs with the given passport number or an error
     */
    List<FBCAEntity> findByPassportNumberIgnoreCase(String passportNumber);

    /**
     * Get all the FBCAs with the given last and first names
     * @param lastName The last name to search for
     * @param firstName The first name to search for
     * @return all the FBCAs with the given last and first names or an error
     */
    List<FBCAEntity> findByLastNameAndFirstNameIgnoreCase(String lastName, String firstName);

    /**
     * Get all the FBCAs with the given last name
     * @param lastName The last name to search for
     * @return all the FBCAs with the given last name or an error
     */
    List<FBCAEntity> findByLastNameIgnoreCase(String lastName);
}
