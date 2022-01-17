package me.polyfrontier.casparwia2;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.polyfrontier.casparwia2.controller.FBCAController;
import me.polyfrontier.casparwia2.model.*;
import me.polyfrontier.casparwia2.repository.FBCARepository;
import me.polyfrontier.casparwia2.repository.UserRepository;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.text.SimpleDateFormat;
import java.time.Instant;
import java.time.Period;
import java.util.*;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FBCAController.class)
public class FBCATest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private UserRepository userRepository;

    @MockBean
    private FBCARepository repository;

    private final UserEntity admin = new UserEntity(UserEntity.Role.ADMIN);
    private final UserEntity police = new UserEntity(UserEntity.Role.POLICE);

    @Before
    public void mockUserRepositoryFindByToken() {
        given(userRepository.findByToken("admin")).willReturn(Optional.of(admin));
        given(userRepository.findByToken("police")).willReturn(Optional.of(police));
    }

    //region GetAll

    @Test
    public void getAll() throws Exception {

        FBCAEntity fbca1 = new FBCAEntity(1, "Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(15))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, "");

        FBCAEntity fbca2 = new FBCAEntity(2, "Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(15))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, "");

        List<FBCAEntity> allFCBAs = Arrays.asList(fbca1, fbca2);

        given(repository.findAll()).willReturn(allFCBAs);

        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/all")
                        .header("Authorization", "Bearer admin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0]").exists())
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1]").exists());
    }

    @Test
    public void getAllEmptyBase() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/all")
                        .header("Authorization", "Bearer admin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(0)));
    }

    //endregion

    //region isFBCAValid

    @Test
    public void isFBCAValidWith1FBCANotValid() throws Exception {
        FBCAEntity fbca1 = new FBCAEntity(1, "Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(15))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, false, FBCAState.WAITING_FOR_APPROVAL, "");

        given(repository.findByPassportNumberIgnoreCase("01AB12345")).willReturn(List.of(fbca1));

        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/isValid/passport/01AB12345")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(false));
    }

    @Test
    public void isFBCAValidWith1FBCAValid() throws Exception {
        FBCAEntity fbca1 = new FBCAEntity(1, "Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(15))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, "");

        given(repository.findByPassportNumberIgnoreCase("01AB12345")).willReturn(List.of(fbca1));

        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/isValid/passport/01AB12345")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(true));
    }

    @Test
    public void isFBCAValidWith2FBCALastIdNotValid() throws Exception {
        FBCAEntity fbca1 = new FBCAEntity(2, "Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(15))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, false, FBCAState.WAITING_FOR_APPROVAL, "");
        FBCAEntity fbca2 = new FBCAEntity(1, "Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(15))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, "");


        given(repository.findByPassportNumberIgnoreCase("01AB12345")).willReturn(List.of(fbca1,fbca2));

        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/isValid/passport/01AB12345")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(false));
    }

    @Test
    public void isFBCAValidWith3FBCALastIdValid() throws Exception {
        FBCAEntity fbca1 = new FBCAEntity(1, "Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(15))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, false, FBCAState.WAITING_FOR_APPROVAL, "");
        FBCAEntity fbca2 = new FBCAEntity(3, "Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(15))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, "");
        FBCAEntity fbca3 = new FBCAEntity(2, "Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(15))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, false, FBCAState.WAITING_FOR_APPROVAL, "");

        given(repository.findByPassportNumberIgnoreCase("01AB12345")).willReturn(List.of(fbca1,fbca2,fbca3));

        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/isValid/passport/01AB12345")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(true));
    }

    @Test
    public void isFBCAValidWith1FBCAExpired() throws Exception {
        FBCAEntity fbca1 = new FBCAEntity(1, "Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(15))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, "");
        FBCAEntity fbca2 = new FBCAEntity(3, "Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                new SimpleDateFormat("yyyy-MM-dd").parse("2022-01-01"), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, "");

        given(repository.findByPassportNumberIgnoreCase("01AB12345")).willReturn(List.of(fbca1,fbca2));

        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/isValid/passport/01AB12345")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$").value(false));
    }

    //endregion

    //region GetById

    @Test
    public void getById() throws Exception {
        FBCAEntity fbca2 = new FBCAEntity(2, "Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(15))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, "");

        given(repository.findById(Long.parseLong("2"))).willReturn(java.util.Optional.of(fbca2));

        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/2")
                        .header("Authorization", "Bearer admin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("lastName").value("Ju"));
    }

    @Test
    public void getByNonExistentId() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/2")
                        .header("Authorization", "Bearer admin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //endregion

    //region getPassportNumber

    @Test
    public void getByPassportNumberOneEntity() throws Exception {
        FBCAEntity fbca2 = new FBCAEntity(2,"Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, "");

        given(repository.findByPassportNumberIgnoreCase("01AB12345")).willReturn(List.of(fbca2));

        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/passport/01AB12345")
                        .header("Authorization", "Bearer admin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("lastName").value("Ju"));
    }

    @Test
    public void getByPassportNumberMultipleEntities() throws Exception {
        FBCAEntity fbca1 = new FBCAEntity(1,"Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, "");
        FBCAEntity fbca2 = new FBCAEntity(2,"Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, "");
        FBCAEntity fbca3 = new FBCAEntity(3,"Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, "");

        given(repository.findByPassportNumberIgnoreCase("01AB12345")).willReturn(List.of(fbca3, fbca1, fbca2));

        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/passport/01AB12345")
                        .header("Authorization", "Bearer admin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("lastName").value("Ju"));
    }

    @Test
    public void getByNonExistentPassportNumber() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/passport/01AB12345")
                        .header("Authorization", "Bearer admin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    //endregion

    //region add

    @Test
    public void isFBCAValidWithNonExistentPassportNumber() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/isValid/passport/01AB12345")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void add() throws Exception {
        FBCAEntity fbca2 = new FBCAEntity("Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, null);

        given(repository.save(fbca2)).willReturn(fbca2);

        mvc.perform(MockMvcRequestBuilders
                        .post("/fbca")
                        .content(new ObjectMapper().writeValueAsString(fbca2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void addWithWrongMailFormat() throws Exception {
        FBCAEntity fbca2 = new FBCAEntity("Ju", "Ju", "emailmail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, null);

        mvc.perform(MockMvcRequestBuilders
                        .post("/fbca")
                        .content(new ObjectMapper().writeValueAsString(fbca2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addWithWrongMobilePhoneNumberFormat() throws Exception {
        FBCAEntity fbca2 = new FBCAEntity("Ju", "Ju", "email@mail.com", "22", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, null);

        mvc.perform(MockMvcRequestBuilders
                        .post("/fbca")
                        .content(new ObjectMapper().writeValueAsString(fbca2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addWithEmptyField() throws Exception {
        FBCAEntity fbca2 = new FBCAEntity("null", "", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, null);

        mvc.perform(MockMvcRequestBuilders
                        .post("/fbca")
                        .content(new ObjectMapper().writeValueAsString(fbca2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addWithWrongPassportNumberFormat() throws Exception {
        FBCAEntity fbca2 = new FBCAEntity("JU", "juju", "email@mail.com", "0123456789", "01ABCC12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, null);

        mvc.perform(MockMvcRequestBuilders
                        .post("/fbca")
                        .content(new ObjectMapper().writeValueAsString(fbca2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addWithoutRequiredField() throws Exception {
        FBCAEntity fbca2 = new FBCAEntity("Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), null, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, null);

        mvc.perform(MockMvcRequestBuilders
                        .post("/fbca")
                        .content(new ObjectMapper().writeValueAsString(fbca2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addWithFreight() throws Exception {
        FBCAEntity fbca2 = new FBCAEntity("Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true,
                FBCAState.WAITING_FOR_APPROVAL, Set.of(new Freight("S20", "jsp", 20), new Freight("S1", "jsp", 20)));

        given(repository.save(fbca2)).willReturn(fbca2);

        mvc.perform(MockMvcRequestBuilders
                        .post("/fbca")
                        .content(new ObjectMapper().writeValueAsString(fbca2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void addWithFreightWithZeroWeight() throws Exception {
        FBCAEntity fbca2 = new FBCAEntity("Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true,
                FBCAState.WAITING_FOR_APPROVAL, Set.of(new Freight("S20", "jsp", 0), new Freight("S1", "jsp", 20)));

        given(repository.save(fbca2)).willReturn(fbca2);

        mvc.perform(MockMvcRequestBuilders
                        .post("/fbca")
                        .content(new ObjectMapper().writeValueAsString(fbca2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addWithFreightWithEmptyName() throws Exception {
        FBCAEntity fbca2 = new FBCAEntity("Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true,
                FBCAState.WAITING_FOR_APPROVAL, Set.of(new Freight("S20", "", 20), new Freight("S1", "jsp", 20)));

        given(repository.save(fbca2)).willReturn(fbca2);

        mvc.perform(MockMvcRequestBuilders
                        .post("/fbca")
                        .content(new ObjectMapper().writeValueAsString(fbca2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addWithFreightWithWrongType() throws Exception {
        FBCAEntity fbca2 = new FBCAEntity("Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true,
                FBCAState.WAITING_FOR_APPROVAL, Set.of(new Freight("SA32", "okk", 20), new Freight("S1", "jsp", 20)));

        given(repository.save(fbca2)).willReturn(fbca2);

        mvc.perform(MockMvcRequestBuilders
                        .post("/fbca")
                        .content(new ObjectMapper().writeValueAsString(fbca2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    //endregion

    //region update

    @Test
    public void updateAccepted() throws Exception {
        FBCAEntity fbcaInBase = new FBCAEntity(1,"Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, null);

        FBCAEntity newFbca = new FBCAEntity(1,"Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.ACCEPTED, null);

        given(repository.findById(Long.parseLong("1"))).willReturn(Optional.of(fbcaInBase));
        given(repository.save(newFbca)).willReturn(newFbca);

        mvc.perform(MockMvcRequestBuilders
                        .put("/fbca/update/1")
                        .header("Authorization", "Bearer police")
                        .content(new ObjectMapper().writeValueAsString(newFbca))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateDeclinedInvalidFBCA() throws Exception {
        FBCAEntity fbcaInBase = new FBCAEntity(1,"Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, false, FBCAState.WAITING_FOR_APPROVAL, null);

        FBCAEntity newFbca = new FBCAEntity(1,"Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, false, FBCAState.DECLINED, "ATFB invalide");

        given(repository.findById(Long.parseLong("1"))).willReturn(Optional.of(fbcaInBase));
        given(repository.save(newFbca)).willReturn(newFbca);

        mvc.perform(MockMvcRequestBuilders
                        .put("/fbca/update/1")
                        .header("Authorization", "Bearer police")
                        .content(new ObjectMapper().writeValueAsString(newFbca))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk());
    }

    @Test
    public void updateDeclinedWithoutReason() throws Exception {
        FBCAEntity fbcaInBase = new FBCAEntity(1,"Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, null);

        FBCAEntity newFbca = new FBCAEntity(1,"Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.DECLINED, null);

        given(repository.findById(Long.parseLong("1"))).willReturn(Optional.of(fbcaInBase));
        given(repository.save(newFbca)).willReturn(newFbca);

        mvc.perform(MockMvcRequestBuilders
                        .put("/fbca/update/1")
                        .header("Authorization", "Bearer police")
                        .content(new ObjectMapper().writeValueAsString(newFbca))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateDeclinedWithReasonTooShort() throws Exception {
        FBCAEntity fbcaInBase = new FBCAEntity(1,"Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, null);

        FBCAEntity newFbca = new FBCAEntity(1,"Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.DECLINED, "nul");

        given(repository.findById(Long.parseLong("1"))).willReturn(Optional.of(fbcaInBase));
        given(repository.save(newFbca)).willReturn(newFbca);

        mvc.perform(MockMvcRequestBuilders
                        .put("/fbca/update/1")
                        .header("Authorization", "Bearer police")
                        .content(new ObjectMapper().writeValueAsString(newFbca))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void updateAcceptedWithReason() throws Exception {
        FBCAEntity fbcaInBase = new FBCAEntity(1,"Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, null);

        FBCAEntity newFbca = new FBCAEntity(1,"Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.ACCEPTED, "Pourquoi?");

        given(repository.findById(Long.parseLong("1"))).willReturn(Optional.of(fbcaInBase));
        given(repository.save(newFbca)).willReturn(newFbca);

        mvc.perform(MockMvcRequestBuilders
                        .put("/fbca/update/1")
                        .header("Authorization", "Bearer police")
                        .content(new ObjectMapper().writeValueAsString(newFbca))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    //endregion

    //region getFBCAByFirstAndLastName

    @Test
    public void getFBCAByFirstAndLastName() throws Exception {
        FBCAEntity fbca1 = new FBCAEntity(1,"Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, "");

        FBCAEntity fbca2 = new FBCAEntity(2,"Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, "");

        given(repository.findByLastNameAndFirstNameIgnoreCase("Ju", "Ju")).willReturn(List.of(fbca2, fbca1));

        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/firstNameAndLastName?lastName=Ju&firstName=Ju")
                        .header("Authorization", "Bearer admin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(2));
    }

    @Test
    public void getFBCAByLastName() throws Exception {
        FBCAEntity fbca1 = new FBCAEntity(1,"Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, "");

        FBCAEntity fbca2 = new FBCAEntity(2,"Slt", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, "");

        given(repository.findByLastNameIgnoreCase("Ju")).willReturn(List.of(fbca2, fbca1));

        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/firstNameAndLastName?lastName=Ju")
                        .header("Authorization", "Bearer admin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(2)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(1))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[1].id").value(2));
    }

    @Test
    public void getFBCAByNonExistentFirstAndLastName() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/firstNameAndLastName?lastName=Ju&firstName=Ju")
                        .header("Authorization", "Bearer admin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getFBCAByNonExistentLastName() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/firstNameAndLastName?lastName=Ju")
                        .header("Authorization", "Bearer admin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getFBCAByLastNameEntityWithStateAccepted() throws Exception {
        FBCAEntity fbcaAccepted = new FBCAEntity(1,"Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.ACCEPTED, "");

        FBCAEntity fbca = new FBCAEntity(2,"Slt", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, "");

        given(repository.findByLastNameIgnoreCase("Ju")).willReturn(List.of(fbca, fbcaAccepted));

        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/firstNameAndLastName?lastName=Ju")
                        .header("Authorization", "Bearer admin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].firstName").value("Slt"));
    }

    @Test
    public void getFBCAByLastNameEntityInvalid() throws Exception {
        FBCAEntity fbcaAccepted = new FBCAEntity(1,"Ju", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, false, FBCAState.WAITING_FOR_APPROVAL, "");

        FBCAEntity fbca = new FBCAEntity(2,"Slt", "Ju", "email@mail.com", "0123456789", "01AB12345",
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true, FBCAState.WAITING_FOR_APPROVAL, "");

        given(repository.findByLastNameIgnoreCase("Ju")).willReturn(List.of(fbca, fbcaAccepted));

        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/firstNameAndLastName?lastName=Ju")
                        .header("Authorization", "Bearer admin")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(1)))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("$.[0].firstName").value("Slt"));
    }

    //endregion
}
