package com.tecsup.petclinic.webs;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.tecsup.petclinic.dtos.SpecialtyDTO;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;

import static org.hamcrest.CoreMatchers.is;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultHandlers.print;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

@AutoConfigureMockMvc
@SpringBootTest
@Slf4j
public class SpecialtyControllerTest {

	private static final ObjectMapper om = new ObjectMapper();

	@Autowired
	private MockMvc mockMvc;

	@Test
	public void testFindAllSpecialties() throws Exception {

		mockMvc.perform(get("/specialties")
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$[0].id", is(1)))
				.andExpect(jsonPath("$[0].name", is("radiology")))
				.andExpect(jsonPath("$[0].office", is("Farewell")))
				.andExpect(jsonPath("$[0].hourOpen", is(8)))
				.andExpect(jsonPath("$[0].hourClose", is(18)));
	}

	@Test
	public void testFindSpecialtyOK() throws Exception {

		mockMvc.perform(get("/specialties/1")
						.accept(MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isOk())
				.andExpect(content().contentType(MediaType.APPLICATION_JSON))
				.andExpect(jsonPath("$.id", is(1)))
				.andExpect(jsonPath("$.name", is("radiology")))
				.andExpect(jsonPath("$.office", is("Farewell")))
				.andExpect(jsonPath("$.hourOpen", is(8)))
				.andExpect(jsonPath("$.hourClose", is(18)));
	}

	@Test
	public void testFindSpecialtyKO() throws Exception {

		mockMvc.perform(get("/specialties/666")
						.accept(MediaType.APPLICATION_JSON))
				.andExpect(status().isNotFound());
	}

	@Test
	public void testCreateSpecialty() throws Exception {

		String NAME = "dermatology-juan";
		String OFFICE = "Lima";
		int HOUR_OPEN = 9;
		int HOUR_CLOSE = 17;

		SpecialtyDTO newSpecialtyTO = SpecialtyDTO.builder()
				.name(NAME)
				.office(OFFICE)
				.hourOpen(HOUR_OPEN)
				.hourClose(HOUR_CLOSE)
				.build();

		mockMvc.perform(post("/specialties")
						.content(om.writeValueAsString(newSpecialtyTO))
						.header(HttpHeaders.CONTENT_TYPE, MediaType.APPLICATION_JSON))
				.andDo(print())
				.andExpect(status().isCreated())
				.andExpect(jsonPath("$.name", is(NAME)))
				.andExpect(jsonPath("$.office", is(OFFICE)))
				.andExpect(jsonPath("$.hourOpen", is(HOUR_OPEN)))
				.andExpect(jsonPath("$.hourClose", is(HOUR_CLOSE)));
	}

	@Test
	public void testFindByName() throws Exception {

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
