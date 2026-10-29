package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.exceptions.InvalidScheduleException;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import lombok.extern.slf4j.Slf4j;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
@Slf4j
public class SpecialtyServiceTest {

    @Autowired
    private SpecialtyService specialtyService;

    @Test
    public void testCreateSpecialty() throws InvalidScheduleException {
        SpecialtyDTO newSpecialty = new SpecialtyDTO(null, "Cardiología", "Consultorio 101", 8, 16);

        SpecialtyDTO created = specialtyService.create(newSpecialty);

        assertNotNull(created.getId(), "El ID de la especialidad creada no debe ser nulo");
        assertEquals("Cardiología", created.getName());
        assertEquals("Consultorio 101", created.getOffice());
        assertEquals(8, created.getHourOpen());
        assertEquals(16, created.getHourClose());
    }

    @Test
    public void testUpdateSpecialty() throws InvalidScheduleException {
        SpecialtyDTO initial = new SpecialtyDTO(null, "Cardiología", "Consultorio 101", 8, 16);
        SpecialtyDTO saved = specialtyService.create(initial);

        saved.setName("Cardiología Avanzada");
        saved.setOffice("Consultorio 202");
        saved.setHourOpen(9);
        saved.setHourClose(18);

        SpecialtyDTO updated = specialtyService.update(saved);

        assertEquals(saved.getId(), updated.getId());
        assertEquals("Cardiología Avanzada", updated.getName());
        assertEquals("Consultorio 202", updated.getOffice());
        assertEquals(9, updated.getHourOpen());
        assertEquals(18, updated.getHourClose());
    }

    @Test
    public void testFindSpecialtyById() throws SpecialtyNotFoundException, InvalidScheduleException {
        SpecialtyDTO newSpecialty = new SpecialtyDTO(null, "Neurología", "Consultorio 303", 10, 15);
        SpecialtyDTO saved = specialtyService.create(newSpecialty);

        SpecialtyDTO found = specialtyService.findById(saved.getId());

        assertNotNull(found, "La especialidad encontrada no debe ser nula");
        assertEquals(saved.getId(), found.getId());
        assertEquals("Neurología", found.getName());
        assertEquals("Consultorio 303", found.getOffice());
    }

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

    @Test
    public void testFindSpecialtyById_NotFound() {

        Integer ID_NOT_EXISTS = 9999;

        assertThrows(SpecialtyNotFoundException.class,
                () -> this.specialtyService.findById(ID_NOT_EXISTS));
    }

    @Test
    public void testCreateSpecialty_InvalidSchedule() {

        SpecialtyDTO openAfterClose = SpecialtyDTO.builder()
                .name("invalid-schedule")
                .office("Office-Invalid")
                .hourOpen(18)
                .hourClose(8)
                .build();

        SpecialtyDTO sameHour = SpecialtyDTO.builder()
                .name("invalid-schedule")
                .office("Office-Invalid")
                .hourOpen(10)
                .hourClose(10)
                .build();

        assertThrows(InvalidScheduleException.class,
                () -> this.specialtyService.create(openAfterClose));
        assertThrows(InvalidScheduleException.class,
                () -> this.specialtyService.create(sameHour));

        assertTrue(this.specialtyService.findByOffice("Office-Invalid").isEmpty());
    }

    @Test
    public void testCreateSpecialty_InvalidHourRange() {

        SpecialtyDTO negativeHour = SpecialtyDTO.builder()
                .name("invalid-range")
                .office("Office-Range")
                .hourOpen(-1)
                .hourClose(10)
                .build();

        SpecialtyDTO hourTooHigh = SpecialtyDTO.builder()
                .name("invalid-range")
                .office("Office-Range")
                .hourOpen(8)
                .hourClose(25)
                .build();

        assertThrows(InvalidScheduleException.class,
                () -> this.specialtyService.create(negativeHour));
        assertThrows(InvalidScheduleException.class,
                () -> this.specialtyService.create(hourTooHigh));

        assertTrue(this.specialtyService.findByOffice("Office-Range").isEmpty());
    }
}
