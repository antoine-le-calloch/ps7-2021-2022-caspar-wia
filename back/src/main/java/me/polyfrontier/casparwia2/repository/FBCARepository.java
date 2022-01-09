package me.polyfrontier.casparwia2.repository;

import me.polyfrontier.casparwia2.model.FBCAEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FBCARepository extends JpaRepository<FBCAEntity, Long> {

    List<FBCAEntity> findByPassportNumber(String passportNumber);

}
