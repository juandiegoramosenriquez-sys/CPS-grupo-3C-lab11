package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.exceptions.InvalidScheduleException;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.services.SpecialtyService;
import com.tecsup.petclinic.util.TObjectCreator;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.hamcrest.CoreMatchers.is;
import static org.mockito.ArgumentMatchers.any;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class SpecialtyControllerMockitoTest {

    private static final ObjectMapper om = new ObjectMapper();

    @Autowired
    private MockMvc mockMvc;

    @MockitoBean
    private SpecialtyService specialtyService;

    @Test
    public void testFindAllSpecialties() throws Exception {

        Mockito.when(specialtyService.findAll())
                .thenReturn(TObjectCreator.getSpecialtiesDTOForFindAll());

        mockMvc.perform(get("/specialties")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(3)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("radiology")))
                .andExpect(jsonPath("$[0].office", is("Farewell")))
                .andExpect(jsonPath("$[0].hourOpen", is(8)))
                .andExpect(jsonPath("$[0].hourClose", is(18)));
    }

    @Test
    public void testFindSpecialtyOK() throws Exception {

        SpecialtyDTO radiology = TObjectCreator.getSpecialtyDTO1();

        Mockito.when(specialtyService.findById(1))
                .thenReturn(radiology);

        mockMvc.perform(get("/specialties/1")
                        .accept(MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.id", is(radiology.getId())))
                .andExpect(jsonPath("$.name", is(radiology.getName())))
                .andExpect(jsonPath("$.office", is(radiology.getOffice())))
                .andExpect(jsonPath("$.hourOpen", is(radiology.getHourOpen())))
                .andExpect(jsonPath("$.hourClose", is(radiology.getHourClose())));
    }

    @Test
    public void testFindSpecialtyKO() throws Exception {

        Mockito.when(specialtyService.findById(666))
                .thenThrow(new SpecialtyNotFoundException("Record not found...!"));

        mockMvc.perform(get("/specialties/666")
                        .accept(MediaType.APPLICATION_JSON))
                .andExpect(status().isNotFound());
    }

    @Test
    public void testCreateSpecialty() throws Exception {

        SpecialtyDTO newSpecialtyTO = SpecialtyDTO.builder()
                .name("oncology")
                .office("Lima")
                .hourOpen(9)
                .hourClose(17)
                .build();

        SpecialtyDTO createdSpecialtyTO = SpecialtyDTO.builder()
                .id(100)
                .name("oncology")
                .office("Lima")
                .hourOpen(9)
                .hourClose(17)
                .build();

        Mockito.when(specialtyService.create(newSpecialtyTO))
                .thenReturn(createdSpecialtyTO);

        mockMvc.perform(post("/specialties")
                        .content(om.writeValueAsString(newSpecialtyTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(100)))
                .andExpect(jsonPath("$.name", is("oncology")))
                .andExpect(jsonPath("$.office", is("Lima")))
                .andExpect(jsonPath("$.hourOpen", is(9)))
                .andExpect(jsonPath("$.hourClose", is(17)));
    }

    @Test
    public void testFindByName() throws Exception {

        SpecialtyDTO surgery = TObjectCreator.getSpecialtyDTO2();

        Mockito.when(specialtyService.findByName("surgery"))
                .thenReturn(List.of(surgery));

        mockMvc.perform(get("/specialties").param("name", "surgery"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(2)))
                .andExpect(jsonPath("$[0].name", is("surgery")))
                .andExpect(jsonPath("$[0].office", is("Maryland")));
    }

    @Test
    public void testFindByOffice() throws Exception {

        SpecialtyDTO radiology = TObjectCreator.getSpecialtyDTO1();

        Mockito.when(specialtyService.findByOffice("Farewell"))
                .thenReturn(List.of(radiology));

        mockMvc.perform(get("/specialties").param("office", "Farewell"))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().contentType(MediaType.APPLICATION_JSON))
                .andExpect(jsonPath("$.size()", is(1)))
                .andExpect(jsonPath("$[0].id", is(1)))
                .andExpect(jsonPath("$[0].name", is("radiology")))
                .andExpect(jsonPath("$[0].office", is("Farewell")));
    }

    @Test
    public void testCreateSpecialty_InvalidSchedule() throws Exception {

        SpecialtyDTO invalidTO = SpecialtyDTO.builder()
                .name("cardiology-gerardo-mock")
                .office("OfficeC")
                .hourOpen(18)
                .hourClose(8)
                .build();

        Mockito.when(specialtyService.create(any(SpecialtyDTO.class)))
                .thenThrow(new InvalidScheduleException("h_open must be less than h_close"));

        mockMvc.perform(post("/specialties")
                        .content(om.writeValueAsString(invalidTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testCreateSpecialty_InvalidHourRange() throws Exception {

        SpecialtyDTO invalidTO = SpecialtyDTO.builder()
                .name("oncology-gerardo-mock")
                .office("OfficeC")
                .hourOpen(25)
                .hourClose(10)
                .build();

        Mockito.when(specialtyService.create(any(SpecialtyDTO.class)))
                .thenThrow(new InvalidScheduleException("Hours must be between 0 and 23"));

        mockMvc.perform(post("/specialties")
                        .content(om.writeValueAsString(invalidTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isBadRequest());
    }

    @Test
    public void testDeleteSpecialty() throws Exception {

        int ID = 999;

        SpecialtyDTO createdTO = SpecialtyDTO.builder()
                .id(ID)
                .name("dermatology-gerardo-mock")
                .office("OfficeC")
                .hourOpen(9)
                .hourClose(17)
                .build();

        Mockito.when(specialtyService.create(any(SpecialtyDTO.class)))
                .thenReturn(createdTO);

        Mockito.doNothing().when(specialtyService).delete(ID);

        mockMvc.perform(post("/specialties")
                        .content(om.writeValueAsString(createdTO))
                        .header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
                .andDo(print())
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.id", is(ID)));

        mockMvc.perform(delete("/specialties/" + ID))
                .andDo(print())
                .andExpect(status().isOk())
                .andExpect(content().string("Delete ID :" + ID));
    }
}