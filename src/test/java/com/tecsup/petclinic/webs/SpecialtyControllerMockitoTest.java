package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.services.SpecialtyService;
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

		SpecialtyDTO surgery = SpecialtyDTO.builder()
				.id(2)
				.name("surgery")
				.office("Maryland")
				.hourOpen(8)
				.hourClose(12)
				.build();

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

		SpecialtyDTO radiology = SpecialtyDTO.builder()
				.id(1)
				.name("radiology")
				.office("Farewell")
				.hourOpen(8)
				.hourClose(18)
				.build();

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
}
