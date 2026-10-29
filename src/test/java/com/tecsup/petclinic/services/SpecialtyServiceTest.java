package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.exceptions.InvalidScheduleException;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Slf4j
public class SpecialtyServiceTest {

    @Autowired
    private SpecialtyService specialtyService;

    @Test
    public void testFindByOffice() {

        String OFFICE = "Farewell";
        int SIZE_EXPECTED = 1;

        List<SpecialtyDTO> specialties = this.specialtyService.findByOffice(OFFICE);

        assertEquals(SIZE_EXPECTED, specialties.size());
        assertEquals("radiology", specialties.get(0).getName());
    }

    @Test
    public void testFindByName() {

        String NAME = "surgery";
        int SIZE_EXPECTED = 1;

        List<SpecialtyDTO> specialties = this.specialtyService.findByName(NAME);

        assertEquals(SIZE_EXPECTED, specialties.size());
        assertEquals("Maryland", specialties.get(0).getOffice());
    }

    @Test
    public void testDeleteSpecialty() {

        SpecialtyDTO specialtyDTO = SpecialtyDTO.builder()
                .name("cardiology")
                .office("Office-Delete")
                .hourOpen(8)
                .hourClose(12)
                .build();

        SpecialtyDTO newSpecialtyDTO = null;
        try {
            newSpecialtyDTO = this.specialtyService.create(specialtyDTO);
        } catch (InvalidScheduleException e) {
            fail(e.getMessage());
        }
        log.info("" + newSpecialtyDTO);

        try {
            this.specialtyService.delete(newSpecialtyDTO.getId());
        } catch (SpecialtyNotFoundException e) {
            fail(e.getMessage());
        }

        Integer ID = newSpecialtyDTO.getId();
        assertThrows(SpecialtyNotFoundException.class,
                () -> this.specialtyService.findById(ID));
    }
}
