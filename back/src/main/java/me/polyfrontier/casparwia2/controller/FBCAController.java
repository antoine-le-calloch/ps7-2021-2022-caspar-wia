package me.polyfrontier.casparwia2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.polyfrontier.casparwia2.model.FBCAEntity;
import me.polyfrontier.casparwia2.model.FBCAState;
import me.polyfrontier.casparwia2.model.UserEntity;
import me.polyfrontier.casparwia2.repository.FBCARepository;
import org.everit.json.schema.PrimitiveValidationStrategy;
import org.everit.json.schema.Schema;
import org.everit.json.schema.ValidationException;
import org.everit.json.schema.Validator;
import org.everit.json.schema.loader.SchemaClient;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import org.springframework.core.io.ClassPathResource;

import java.io.InputStream;
import java.util.*;
import java.util.stream.Collectors;

@RestController
@RequestMapping("/fbca")
public class FBCAController {
    @Autowired
    private final FBCARepository fbcaRepository;

    private final UserEntity currentUser;

    @Autowired
    public FBCAController(FBCARepository fbcaRepository, UserEntity currentUser) {
        this.fbcaRepository = fbcaRepository;
        this.currentUser = currentUser;
    }

    /**
     * Default FBCA route
     * @return What to do
     */
    @GetMapping("/")
    public String home() {
        throw new ResponseStatusException(HttpStatus.MULTIPLE_CHOICES, "Endpoint not found, try /fbca/all");
    }


    /**
     * Get all the FBCAs in database
     * @return All the FBCAs in database if the caller has the right to do it. Else an error is returned
     */
    @GetMapping("/all")
    public ResponseEntity<List<FBCAEntity>> getAll() {
        if (currentUser.getRole() == UserEntity.Role.ADMIN || currentUser.getRole() == UserEntity.Role.POLICE) {
            List<FBCAEntity> listFBCAs = fbcaRepository.findAll();
            return new ResponseEntity<>(listFBCAs, HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You (" + currentUser.getToken() + ") are not authorized to perform this action as a " + currentUser.getRole() + " user");
        }
    }

    /**
     * Get the FBCA with the given id
     * @param id The id of the FBCA
     * @return the FBCA with the given id if the caller has the right to do it. Else an error is returned. If the id doesn't exist,
     * an error is returned
     */
    @GetMapping("/{id}")
    public ResponseEntity<FBCAEntity> getById(@PathVariable("id") String id) {
        if (currentUser.getRole() == UserEntity.Role.ADMIN || currentUser.getRole() == UserEntity.Role.POLICE || currentUser.getRole() == UserEntity.Role.CUSTOMS) {
            Optional<FBCAEntity> optional = fbcaRepository.findById(Long.parseLong(id));
            if (optional.isPresent()) {
                return new ResponseEntity<>(optional.get(), HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The requested FBCA is not existent, try again later or with the surname");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You are not allowed to access this endpoint");
        }
    }

    /**
     * get the valid state of the latest FBCA with the given passport number
     * @param passportNumber The passport number of the FBCA
     * @return the valid state of the latest FBCA if the caller has the right to do it. Else an error is returned. If the passportNumber doesn't exist,
     * an error is returned
     */
    @GetMapping("/isValid/passport/{passportNumber}")
    public ResponseEntity<Boolean> isFBCAValid(@PathVariable("passportNumber") String passportNumber) {
        List<FBCAEntity> list = fbcaRepository.findByPassportNumberIgnoreCase(passportNumber);
        int listSize = list.size();
        if (listSize != 0) {
            List<FBCAEntity> modifiableList = new ArrayList<>(list);
            modifiableList.sort(Comparator.comparing(FBCAEntity::getId));
            return new ResponseEntity<>(modifiableList.get(listSize - 1).canBeUsed(), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The requested FBCA is not existent, try again with an other passport number");
        }
    }

    /**
     * Get the latest FBCA with the given passport number
     * @param passportNumber The passport number of the FBCA
     * @return the latest FBCA with the given passport number if the caller has the right to do it. Else an error is returned. If the passport number doesn't exist,
     * an error is returned
     */
    @GetMapping("/passport/{passportNumber}")
    public ResponseEntity<FBCAEntity> getFBCAByPassportId(@PathVariable("passportNumber") String passportNumber) {
        if (currentUser.getRole() == UserEntity.Role.ADMIN || currentUser.getRole() == UserEntity.Role.POLICE || currentUser.getRole() == UserEntity.Role.CUSTOMS) {
            List<FBCAEntity> list = fbcaRepository.findByPassportNumberIgnoreCase(passportNumber);
            int listSize = list.size();
            if (listSize != 0) {
                List<FBCAEntity> modifiableList = new ArrayList<>(list);
                modifiableList.sort(Comparator.comparing(FBCAEntity::getId));
                return new ResponseEntity<>(modifiableList.get(listSize - 1), HttpStatus.OK);
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The requested FBCA is not existent, try again with an other passport number");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You (" + currentUser.getToken() + ") are not authorized to perform this action as a " + currentUser.getRole() + " user");
        }
    }

    /**
     * Get all the FBCA with the given last name and first name.
     * @param lastName The last name wanted
     * @param firstName (Optional) The first name wanted
     * @return - If only the last name is here, a list of FBCAs is returned. The FBCAs are WAITING_FOR_APPROVAL and valid.
     * If the last name doesn't exist or the caller doesn't have the right, an error is returned.
     * - If only the first name is here, an error is returned.
     * - If the last and first name are here, the latest FBCA with this information is returned. If the couple doesn't exist or the caller doesn't have the right,
     * an error is returned
     */
    @GetMapping("/firstNameAndLastName")
    public ResponseEntity<List<FBCAEntity>> getFBCAByFirstAndLastName(@RequestParam(defaultValue = "") String lastName, @RequestParam(defaultValue = "") String firstName) {
        if (currentUser.getRole() == UserEntity.Role.ADMIN || currentUser.getRole() == UserEntity.Role.POLICE || currentUser.getRole() == UserEntity.Role.CUSTOMS) {
            if (lastName.isEmpty() || lastName.isBlank()) {
                throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "The last name is required");
            }
            if (firstName.isEmpty() || firstName.isBlank()) {
                List<FBCAEntity> list = fbcaRepository.findByLastNameIgnoreCase(lastName);
                int listSize = list.size();
                if (listSize != 0) {
                    List<FBCAEntity> modifiableList = new ArrayList<>(list);
                    modifiableList = modifiableList.stream().filter(fbca -> fbca.getState() == FBCAState.WAITING_FOR_APPROVAL).collect(Collectors.toList());
                    modifiableList = modifiableList.stream().filter(FBCAEntity::isValid).collect(Collectors.toList());
                    modifiableList.sort(Comparator.comparing(FBCAEntity::getId));
                    return new ResponseEntity<>(modifiableList, HttpStatus.OK);
                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The requested FBCA is not existent, try again with an other passport number");
                }
            } else {
                List<FBCAEntity> list = fbcaRepository.findByLastNameAndFirstNameIgnoreCase(lastName, firstName);
                int listSize = list.size();
                if (listSize != 0) {
                    List<FBCAEntity> modifiableList = new ArrayList<>(list);
                    modifiableList.sort(Comparator.comparing(FBCAEntity::getId));
                    return new ResponseEntity<>(List.of(modifiableList.get(listSize - 1)), HttpStatus.OK);
                } else {
                    throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The requested FBCA is not existent, try again with an other passport number");
                }
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You (" + currentUser.getToken() + ") are not authorized to perform this action as a " + currentUser.getRole() + " user");
        }

    }

        /**
         * Add the given FBCA in the database
         * @param fbca The FBCA to add
         * @return The saved FBCA. If the given FBCA doesn't respect the Schema, an error is returned
         */
    @PostMapping("")
    public ResponseEntity<FBCAEntity> add(@RequestBody String fbca) {
        Resource schemaFile = new ClassPathResource("schemas/ATFBSchemaPOST.json");
        try (InputStream inputStream = schemaFile.getInputStream()) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
            SchemaLoader schemaLoader = SchemaLoader.builder()
                    .schemaClient(SchemaClient.classPathAwareClient())
                    .schemaJson(rawSchema)
                    .resolutionScope("classpath:/schemas/") // setting the default resolution scope
                    .build();
            Schema schema = schemaLoader.load().build();
            JSONObject fbcaObj = new JSONObject(fbca);
            Validator validator = Validator.builder()
                    .primitiveValidationStrategy(PrimitiveValidationStrategy.LENIENT)
                    .build();
            validator.performValidation(schema, fbcaObj); // throws a ValidationException if this object is invalid
            ObjectMapper mapper = new ObjectMapper();
            FBCAEntity fbcaEntity = mapper.readValue(fbca, FBCAEntity.class);
            FBCAEntity savedFBCA = fbcaRepository.save(fbcaEntity);
            return new ResponseEntity<>(savedFBCA, HttpStatus.CREATED);
        } catch (ValidationException e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't add this FBCA because it is in a malformed state (" + e.getMessage() + ")");
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't add this FBCA : " + e.getMessage());
        }
    }

    /**
     * Update the FBCA with the given id
     * @param id The id of the FBCA to update
     * @param fbca The FBCA to update
     * @return The FBCA updated. If the id doesn't exit, an error is returned. If the caller doesn't have the right, an error is returned
     */
    @PutMapping("/update/{id}")
    public ResponseEntity<FBCAEntity> update(@PathVariable("id") String id, @RequestBody String fbca) {
        if (currentUser.getRole() == UserEntity.Role.ADMIN || currentUser.getRole() == UserEntity.Role.POLICE || currentUser.getRole() == UserEntity.Role.CUSTOMS) {
            Optional<FBCAEntity> optional = fbcaRepository.findById(Long.parseLong(id));
            if (optional.isPresent()) {
                Resource schemaFile = new ClassPathResource("schemas/ATFBSchemaPUT.json");
                try (InputStream inputStream = schemaFile.getInputStream()) {
                    JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
                    SchemaLoader schemaLoader = SchemaLoader.builder()
                            .schemaClient(SchemaClient.classPathAwareClient())
                            .schemaJson(rawSchema)
                            .resolutionScope("classpath:/schemas/") // setting the default resolution scope
                            .build();
                    Schema schema = schemaLoader.load().build();
                    JSONObject fbcaObj = new JSONObject(fbca);
                    Validator validator = Validator.builder()
                            .primitiveValidationStrategy(PrimitiveValidationStrategy.LENIENT)
                            .build();
                    validator.performValidation(schema, fbcaObj); // throws a ValidationException if this object is invalid
                    ObjectMapper mapper = new ObjectMapper();
                    FBCAEntity fbcaEntity = mapper.readValue(fbca, FBCAEntity.class);
                    FBCAEntity updatedFBCA = fbcaRepository.save(fbcaEntity);
                    return new ResponseEntity<>(updatedFBCA, HttpStatus.OK);
                } catch (Exception e) {
                    throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't update this FBCA : " + e);
                }
            } else {
                throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The requested FBCA is not existent, try again later or with the surname");
            }
        } else {
            throw new ResponseStatusException(HttpStatus.FORBIDDEN, "You (" + currentUser.getToken() + ") are not authorized to perform this action as a " + currentUser.getRole() + " user");
        }
    }
}
