package com.tecsup.petclinic.services;

import static org.junit.jupiter.api.Assertions.assertThrows;
import static org.mockito.ArgumentMatchers.any;

import java.util.Optional;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.exceptions.InvalidScheduleException;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.mappers.SpecialtyMapper;
import com.tecsup.petclinic.repositories.SpecialtyRepository;

@ExtendWith(MockitoExtension.class)
public class SpecialtyServiceTest {

    @Mock
    private SpecialtyRepository repository;

    @Mock
    private SpecialtyMapper specialtyMapper;

    @InjectMocks
    private SpecialtyServiceImpl specialtyService;

    @Test
    public void testFindSpecialtyById_NotFound() {
        Mockito.when(repository.findById(999)).thenReturn(Optional.empty());

        assertThrows(SpecialtyNotFoundException.class,
                () -> specialtyService.findById(999));
    }

    @Test
    public void testCreateSpecialty_InvalidSchedule() {
        SpecialtyDTO invalid = Mockito.mock(SpecialtyDTO.class);
        Mockito.when(invalid.getHourOpen()).thenReturn(18);
        Mockito.when(invalid.getHourClose()).thenReturn(8);

        assertThrows(InvalidScheduleException.class,
                () -> specialtyService.create(invalid));

        Mockito.verify(repository, Mockito.never()).save(any(Specialty.class));
    }

    @Test
    public void testCreateSpecialty_InvalidHourRange() {
        SpecialtyDTO negativeHour = Mockito.mock(SpecialtyDTO.class);
        Mockito.when(negativeHour.getHourOpen()).thenReturn(-1);
        Mockito.when(negativeHour.getHourClose()).thenReturn(10);

        SpecialtyDTO hourTooHigh = Mockito.mock(SpecialtyDTO.class);
        Mockito.when(hourTooHigh.getHourOpen()).thenReturn(8);
        Mockito.when(hourTooHigh.getHourClose()).thenReturn(25);

        assertThrows(InvalidScheduleException.class,
                () -> specialtyService.create(negativeHour));
        assertThrows(InvalidScheduleException.class,
                () -> specialtyService.create(hourTooHigh));

        Mockito.verify(repository, Mockito.never()).save(any(Specialty.class));
    }
}