package com.tecsup.petclinic.services;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.entities.Specialty;
import com.tecsup.petclinic.exceptions.InvalidScheduleException;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.mappers.SpecialtyMapper;
import com.tecsup.petclinic.repositories.SpecialtyRepository;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
@Slf4j
@RequiredArgsConstructor
public class SpecialtyServiceImpl implements SpecialtyService {

	private final SpecialtyRepository specialtyRepository;
	private final SpecialtyMapper specialtyMapper;

	@Override
	public SpecialtyDTO create(SpecialtyDTO specialtyDTO) throws InvalidScheduleException {

		validateSchedule(specialtyDTO.getHourOpen(), specialtyDTO.getHourClose());

		Specialty newSpecialty = specialtyRepository.save(specialtyMapper.mapToEntity(specialtyDTO));

		return specialtyMapper.mapToDto(newSpecialty);
	}

	@Override
	public SpecialtyDTO update(SpecialtyDTO specialtyDTO) {

		Specialty specialty = specialtyRepository.save(specialtyMapper.mapToEntity(specialtyDTO));

		return specialtyMapper.mapToDto(specialty);
	}

	@Override
	public void delete(Integer id) throws SpecialtyNotFoundException {

		SpecialtyDTO specialty = findById(id);

		specialtyRepository.delete(specialtyMapper.mapToEntity(specialty));
	}

	@Override
	public SpecialtyDTO findById(Integer id) throws SpecialtyNotFoundException {

		Optional<Specialty> specialty = specialtyRepository.findById(id);

		if (specialty.isEmpty())
			throw new SpecialtyNotFoundException("Record not found...!");

		return specialtyMapper.mapToDto(specialty.get());
	}

	@Override
	public List<SpecialtyDTO> findByName(String name) {

		return specialtyRepository.findByName(name)
				.stream()
				.map(specialtyMapper::mapToDto)
				.toList();
	}

	@Override
	public List<SpecialtyDTO> findByOffice(String office) {

		return specialtyRepository.findByOffice(office)
				.stream()
				.map(specialtyMapper::mapToDto)
				.toList();
	}

	private void validateSchedule(int hourOpen, int hourClose) throws InvalidScheduleException {

		if (hourOpen < 0 || hourOpen > 23 || hourClose < 0 || hourClose > 23)
			throw new InvalidScheduleException("Hours must be between 0 and 23");

		if (hourOpen >= hourClose)
			throw new InvalidScheduleException("h_open must be less than h_close");
	}
}
