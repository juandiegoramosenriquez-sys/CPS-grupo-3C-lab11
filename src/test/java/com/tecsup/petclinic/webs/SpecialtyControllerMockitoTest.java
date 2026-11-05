package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.services.SpecialtyService;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@SpringBootTest
@AutoConfigureMockMvc
class SpecialtyControllerMockitoTest {

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SpecialtyService specialtyService;

    @Test
    void testFindAllSpecialties() throws Exception {
        SpecialtyDTO radiology = new SpecialtyDTO(1, "radiology", "Farewell", 8, 18);
        SpecialtyDTO surgery = new SpecialtyDTO(2, "surgery", "Maryland", 8, 12);
        SpecialtyDTO dentistry = new SpecialtyDTO(3, "dentistry", "Terranova", 9, 19);
        when(specialtyService.findAll()).thenReturn(List.of(radiology, surgery, dentistry));

        mockMvc.perform(get("/specialties")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()").value(3))
                .andExpect(jsonPath("$[0].id").value(1))
                .andExpect(jsonPath("$[0].name").value("radiology"))
                .andExpect(jsonPath("$[0].office").value("Farewell"))
                .andExpect(jsonPath("$[0].hourOpen").value(8))
                .andExpect(jsonPath("$[0].hourClose").value(18));
    }

    @Test
    void testFindSpecialtyOK() throws Exception {
        SpecialtyDTO radiology = new SpecialtyDTO(1, "radiology", "Farewell", 8, 18);
        when(specialtyService.findById(1)).thenReturn(radiology);

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
        when(specialtyService.findById(666))
                .thenThrow(new SpecialtyNotFoundException("Record not found...!"));

        mockMvc.perform(get("/specialties/666")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }
}
