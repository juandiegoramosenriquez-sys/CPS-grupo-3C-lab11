package com.tecsup.petclinic.webs;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SpecialtyControllerTest {

    @Autowired
    private MockMvc mockMvc;

    @Test
    void testFindAllSpecialties() throws Exception {
        mockMvc.perform(get("/specialties")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("radiology"))
                .andExpect(jsonPath("$[0].office").value("Farewell"))
                .andExpect(jsonPath("$[0].hourOpen").value(8))
                .andExpect(jsonPath("$[0].hourClose").value(18));
    }

    @Test
    void testFindSpecialtyOK() throws Exception {
        mockMvc.perform(get("/specialties/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id").value(1))
                .andExpect(jsonPath("$.name").value("radiology"))
                .andExpect(jsonPath("$.office").value("Farewell"))
                .andExpect(jsonPath("$.hourOpen").value(8))
                .andExpect(jsonPath("$.hourClose").value(18));
    }

    @Test
    void testFindSpecialtyKO() throws Exception {
        mockMvc.perform(get("/specialties/666")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
