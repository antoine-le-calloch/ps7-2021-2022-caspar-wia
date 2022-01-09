package me.polyfrontier.casparwia2;

import com.fasterxml.jackson.databind.ObjectMapper;
import me.polyfrontier.casparwia2.controller.FBCAController;
import me.polyfrontier.casparwia2.model.FBCAEntity;
import me.polyfrontier.casparwia2.model.FBCAReason;
import me.polyfrontier.casparwia2.model.FBCATransport;
import me.polyfrontier.casparwia2.repository.FBCARepository;
import org.junit.Ignore;
import org.junit.Test;
import org.junit.jupiter.api.Disabled;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.junit4.SpringRunner;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.Instant;
import java.time.Period;
import java.util.Arrays;
import java.util.Date;
import java.util.List;

import static org.hamcrest.Matchers.hasSize;
import static org.mockito.BDDMockito.given;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@RunWith(SpringRunner.class)
@WebMvcTest(FBCAController.class)
public class FBCATest {
    @Autowired
    private MockMvc mvc;

    @MockBean
    private FBCARepository repository;

    @Test
    public void getAll() throws Exception {

        FBCAEntity fbca1 = new FBCAEntity(1, "Ju", "Ju", "email@mail.com", "0123456789", "01AB12345", Date.from(Instant.now()),
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true);

        FBCAEntity fbca2 = new FBCAEntity(2, "Ju", "Ju", "email@mail.com", "0123456789", "01AB12345", Date.from(Instant.now()),
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true);

        List<FBCAEntity> allFCBAs = Arrays.asList(fbca1, fbca2);

        given(repository.findAll()).willReturn(allFCBAs);

        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/all")
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
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("$", hasSize(0)));
    }

    @Test
    public void getById() throws Exception {
        FBCAEntity fbca2 = new FBCAEntity(2, "Ju", "Ju", "email@mail.com", "0123456789", "01AB12345", Date.from(Instant.now()),
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true);

        given(repository.findById(Long.parseLong("2"))).willReturn(java.util.Optional.of(fbca2));

        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("lastName").value("Ju"));
    }

    @Test
    public void getByNonExistentId() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/2")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void getByPassportNumberOneEntity() throws Exception {
        FBCAEntity fbca2 = new FBCAEntity(2,"Ju", "Ju", "email@mail.com", "0123456789", "01AB12345", Date.from(Instant.now()),
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true);

        given(repository.findByPassportNumber("01AB12345")).willReturn(List.of(fbca2));

        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/passport/01AB12345")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(2))
                .andExpect(MockMvcResultMatchers.jsonPath("lastName").value("Ju"));
    }

    @Test
    public void getByPassportNumberMultipleEntities() throws Exception {
        FBCAEntity fbca1 = new FBCAEntity(1,"Ju", "Ju", "email@mail.com", "0123456789", "01AB12345", Date.from(Instant.now()),
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true);
        FBCAEntity fbca2 = new FBCAEntity(2,"Ju", "Ju", "email@mail.com", "0123456789", "01AB12345", Date.from(Instant.now()),
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true);
        FBCAEntity fbca3 = new FBCAEntity(3,"Ju", "Ju", "email@mail.com", "0123456789", "01AB12345", Date.from(Instant.now()),
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true);

        given(repository.findByPassportNumber("01AB12345")).willReturn(List.of(fbca3, fbca1, fbca2));

        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/passport/01AB12345")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(MockMvcResultMatchers.jsonPath("id").value(3))
                .andExpect(MockMvcResultMatchers.jsonPath("lastName").value("Ju"));
    }

    @Test
    public void getByNonExistentPassportNumber() throws Exception {
        mvc.perform(MockMvcRequestBuilders
                        .get("/fbca/passport/01AB12345")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void add() throws Exception {
        FBCAEntity fbca2 = new FBCAEntity("Ju", "Ju", "email@mail.com", "0123456789", "01AB12345", Date.from(Instant.now()),
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true);

        given(repository.save(fbca2)).willReturn(fbca2);

        mvc.perform(MockMvcRequestBuilders
                        .post("/fbca/add")
                        .content(new ObjectMapper().writeValueAsString(fbca2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isCreated());
    }

    @Test
    public void addWithWrongMailFormat() throws Exception {
        FBCAEntity fbca2 = new FBCAEntity("Ju", "Ju", "emailmail.com", "0123456789", "01AB12345", Date.from(Instant.now()),
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true);

        mvc.perform(MockMvcRequestBuilders
                        .post("/fbca/add")
                        .content(new ObjectMapper().writeValueAsString(fbca2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addWithWrongMobilePhoneNumberFormat() throws Exception {
        FBCAEntity fbca2 = new FBCAEntity("Ju", "Ju", "email@mail.com", "22", "01AB12345", Date.from(Instant.now()),
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true);

        mvc.perform(MockMvcRequestBuilders
                        .post("/fbca/add")
                        .content(new ObjectMapper().writeValueAsString(fbca2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addWithEmptyField() throws Exception {
        FBCAEntity fbca2 = new FBCAEntity("null", "", "email@mail.com", "0123456789", "01AB12345", Date.from(Instant.now()),
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true);

        mvc.perform(MockMvcRequestBuilders
                        .post("/fbca/add")
                        .content(new ObjectMapper().writeValueAsString(fbca2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addWithWrongPassportNumberFormat() throws Exception {
        FBCAEntity fbca2 = new FBCAEntity("JU", "juju", "email@mail.com", "0123456789", "01ABCC12345", Date.from(Instant.now()),
                Date.from(Instant.now().plus(Period.ofDays(3))), FBCAReason.BUSINESS, FBCATransport.AIRPLANE, true);

        mvc.perform(MockMvcRequestBuilders
                        .post("/fbca/add")
                        .content(new ObjectMapper().writeValueAsString(fbca2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }

    @Test
    public void addWithoutRequiredField() throws Exception {
        FBCAEntity fbca2 = new FBCAEntity("Ju", "Ju", "email@mail.com", "0123456789", "01AB12345", Date.from(Instant.now()),
                Date.from(Instant.now().plus(Period.ofDays(3))), null, FBCATransport.AIRPLANE, true);

        mvc.perform(MockMvcRequestBuilders
                        .post("/fbca/add")
                        .content(new ObjectMapper().writeValueAsString(fbca2))
                        .contentType(MediaType.APPLICATION_JSON)
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest());
    }
}
