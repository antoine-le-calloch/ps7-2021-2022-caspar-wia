package me.polyfrontier.casparwia2.controller;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.polyfrontier.casparwia2.model.FBCAEntity;
import me.polyfrontier.casparwia2.repository.FBCARepository;
import org.everit.json.schema.PrimitiveValidationStrategy;
import org.everit.json.schema.Schema;
import org.everit.json.schema.Validator;
import org.everit.json.schema.loader.SchemaLoader;
import org.json.JSONObject;
import org.json.JSONTokener;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;

import java.io.InputStream;
import java.util.*;

@RestController
@RequestMapping("/fbca")
public class FBCAController {
    @Autowired
    private FBCARepository fbcaRepository;

    @Value("classpath:ATFBSchema.json")
    Resource schemaFile;

    @GetMapping("/")
    public String home() {
        throw new ResponseStatusException(HttpStatus.MULTIPLE_CHOICES, "Endpoint not found, try /fbca/all");
    }
    @GetMapping("/all")
    public ResponseEntity<List<FBCAEntity>> getAll() {
        List<FBCAEntity> listFBCAs = fbcaRepository.findAll();
        return new ResponseEntity<>(listFBCAs, HttpStatus.OK);
    }

    @GetMapping("/{id}")
    public ResponseEntity<FBCAEntity> getById(@PathVariable("id") String id) {
        Optional<FBCAEntity> optional = fbcaRepository.findById(Long.parseLong(id));
        if (optional.isPresent()) {
            return new ResponseEntity<>(optional.get(), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The requested FBCA is not existent, try again later or with the surname");
        }
    }

    @GetMapping("/passport/{passportNumber}")
    public ResponseEntity<FBCAEntity> getFBCAByPassportId(@PathVariable("passportNumber") String passportNumber) {
        List<FBCAEntity> list = fbcaRepository.findByPassportNumber(passportNumber);
        int listSize = list.size();
        if (listSize!=0) {
            List<FBCAEntity> modifiableList = new ArrayList<>(list);
            modifiableList.sort(Comparator.comparing(FBCAEntity::getId));
            return new ResponseEntity<>(modifiableList.get(listSize-1), HttpStatus.OK);
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND, "The requested FBCA is not existent, try again with an other passport number");
        }
    }

    @PostMapping("/add")
    public ResponseEntity<FBCAEntity> add(@RequestBody String fbca) {
        try (InputStream inputStream = schemaFile.getInputStream()) {
            JSONObject rawSchema = new JSONObject(new JSONTokener(inputStream));
            Schema schema = SchemaLoader.load(rawSchema);
            JSONObject fbcaObj = new JSONObject(fbca);

            Validator validator = Validator.builder()
                    .primitiveValidationStrategy(PrimitiveValidationStrategy.LENIENT)
                    .build();
            validator.performValidation(schema, fbcaObj); // throws a ValidationException if this object is invalid

            ObjectMapper mapper = new ObjectMapper();
            FBCAEntity fbcaEntity = mapper.readValue(fbca, FBCAEntity.class);
            FBCAEntity savedFBCA = fbcaRepository.save(fbcaEntity);
            return new ResponseEntity<>(savedFBCA, HttpStatus.CREATED);
        } catch (Exception e) {
            throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "Can't add this FBCA : " + e);
        }
    }
}
