package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.exception.SpecialtyNotFoundException;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
public class SpecialtyServiceTest {

    @Autowired
    private SpecialtyService specialtyService;

    @Test
    public void testCreateSpecialty() {
        SpecialtyDTO newSpecialty = new SpecialtyDTO(null, "Cardiología", "Consultorio 101", 8, 16);

        SpecialtyDTO created = specialtyService.create(newSpecialty);

        assertNotNull(created.getId(), "El ID de la especialidad creada no debe ser nulo");
        assertEquals("Cardiología", created.getName());
        assertEquals("Consultorio 101", created.getOffice());
        assertEquals(8, created.getHOpen());
        assertEquals(16, created.getHClose());
    }


    @Test
    public void testUpdateSpecialty() {
        SpecialtyDTO initial = new SpecialtyDTO(null, "Cardiología", "Consultorio 101", 8, 16);
        SpecialtyDTO saved = specialtyService.create(initial);

        saved.setName("Cardiología Avanzada");
        saved.setOffice("Consultorio 202");
        saved.setHOpen(9);
        saved.setHClose(18);

        SpecialtyDTO updated = specialtyService.update(saved);

        assertEquals(saved.getId(), updated.getId());
        assertEquals("Cardiología Avanzada", updated.getName());
        assertEquals("Consultorio 202", updated.getOffice());
        assertEquals(9, updated.getHOpen());
        assertEquals(18, updated.getHClose());
    }

    @Test
    public void testFindSpecialtyById() throws SpecialtyNotFoundException {
        SpecialtyDTO newSpecialty = new SpecialtyDTO(null, "Neurología", "Consultorio 303", 10, 15);
        SpecialtyDTO saved = specialtyService.create(newSpecialty);

        SpecialtyDTO found = specialtyService.findById(saved.getId());

        assertNotNull(found, "La especialidad encontrada no debe ser nula");
        assertEquals(saved.getId(), found.getId());
        assertEquals("Neurología", found.getName());
        assertEquals("Consultorio 303", found.getOffice());
    }
}
