package com.tecsup.petclinic.webs;

import com.tecsup.petclinic.dtos.SpecialtyDTO;
import com.tecsup.petclinic.exceptions.InvalidScheduleException;
import com.tecsup.petclinic.exceptions.SpecialtyNotFoundException;
import com.tecsup.petclinic.services.SpecialtyService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequestMapping("/specialties")
@RequiredArgsConstructor
@Slf4j
public class SpecialtyController {

	private final SpecialtyService specialtyService;

	@GetMapping
	public ResponseEntity<List<SpecialtyDTO>> find(@RequestParam(required = false) String name,
												   @RequestParam(required = false) String office) {

		if (name != null)
			return ResponseEntity.ok(specialtyService.findByName(name));

		if (office != null)
			return ResponseEntity.ok(specialtyService.findByOffice(office));

		return ResponseEntity.ok(specialtyService.findAll());
	}

	@GetMapping("/{id}")
	public ResponseEntity<SpecialtyDTO> findById(@PathVariable Integer id) {

		try {
			return ResponseEntity.ok(specialtyService.findById(id));
		} catch (SpecialtyNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}

	@PostMapping
	public ResponseEntity<SpecialtyDTO> create(@RequestBody SpecialtyDTO specialtyDTO) {

		try {
			return ResponseEntity.status(HttpStatus.CREATED).body(specialtyService.create(specialtyDTO));
		} catch (InvalidScheduleException e) {
			return ResponseEntity.badRequest().build();
		}
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<String> delete(@PathVariable Integer id) {

		try {
			specialtyService.delete(id);
			return ResponseEntity.ok("Delete ID :" + id);
		} catch (SpecialtyNotFoundException e) {
			return ResponseEntity.notFound().build();
		}
	}
}
