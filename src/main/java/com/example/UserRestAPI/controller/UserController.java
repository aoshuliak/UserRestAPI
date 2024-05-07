package com.example.UserRestAPI.controller;


import com.example.UserRestAPI.database.entity.UserEntity;
import com.example.UserRestAPI.dto.UserDTO;
import com.example.UserRestAPI.service.UserService;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.temporal.ChronoUnit;
import java.util.List;

@RestController
@RequiredArgsConstructor
@RequestMapping("/users")
public class UserController {
	private final UserService userService;

	@Value("${user.min.age}")
	private Long userMinAge;

	@PostMapping
	public ResponseEntity<UserEntity> createUser(@Valid @RequestBody UserEntity user) {
		if (!isUserAgeValid(user.getBirthDate())) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		UserEntity createdUser = userService.create(user);
		return ResponseEntity.status(HttpStatus.CREATED).body(createdUser);
	}

	@GetMapping("/{id}")
	public ResponseEntity<UserEntity> getUserById(@PathVariable long id) {
		UserEntity user = userService.readById(id);
		if (user == null) {
			return ResponseEntity.notFound().build();
		}
		return ResponseEntity.ok(user);
	}

	@PutMapping("/{id}")
	public ResponseEntity<UserEntity> updateRequiredUserFields(
			@PathVariable long id, @Valid @RequestBody UserDTO updatedUser) {

		UserEntity existingUser = userService.readById(id);
		if (existingUser == null) {
			return ResponseEntity.notFound().build();
		}
		updatedUser.setId(id);

		existingUser.setFirstName(updatedUser.getFirstName());
		existingUser.setLastName(updatedUser.getLastName());
		existingUser.setEmail(updatedUser.getEmail());
		existingUser.setBirthDate(updatedUser.getBirthDate());

		UserEntity updated = userService.update(UserDTO.transformToEntity(updatedUser));
		return ResponseEntity.ok(updated);
	}

	@PatchMapping("/{id}")
	public ResponseEntity<UserEntity> updateSomeUserFields(
			@PathVariable long id, @Valid @RequestBody UserEntity updatedUser) {

		UserEntity existingUser = userService.readById(id);
		if (existingUser == null) {
			return ResponseEntity.notFound().build();
		}

		updatedUser.setId(id);

		if(updatedUser.getFirstName() != null){
			existingUser.setFirstName(updatedUser.getFirstName());
		}
		if(updatedUser.getLastName() != null){
			existingUser.setLastName(updatedUser.getLastName());
		}
		if(updatedUser.getEmail() != null){
			existingUser.setLastName(updatedUser.getEmail());
		}
		if(updatedUser.getBirthDate() != null){
			existingUser.setBirthDate(updatedUser.getBirthDate());
		}
		if(updatedUser.getAddress() != null){
			existingUser.setAddress(updatedUser.getAddress());
		}
		if(updatedUser.getPhoneNumber() != null){
			existingUser.setPhoneNumber(updatedUser.getPhoneNumber());
		}

		UserEntity updated = userService.update(updatedUser);
		return ResponseEntity.ok(updated);
	}

	@DeleteMapping("/{id}")
	public ResponseEntity<Void> deleteUser(@PathVariable long id) {
		UserEntity existingUser = userService.readById(id);
		if (existingUser == null) {
			return ResponseEntity.notFound().build();
		}
		userService.delete(id);
		return ResponseEntity.noContent().build();
	}

	@GetMapping("/all")
	public ResponseEntity<List<UserEntity>> getAllUsers() {
		List<UserEntity> users = userService.getAll();
		if (users.isEmpty()) {
			return ResponseEntity.noContent().build();
		}
		return ResponseEntity.ok(users);
	}

	@GetMapping("/searchByDate")
	public ResponseEntity<List<UserEntity>> searchUsersByBirthDateRange(
			@RequestParam LocalDate startDate,
			@RequestParam LocalDate endDate) {
		if (startDate.isAfter(endDate)) {
			return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(null);
		}
		List<UserEntity> users = userService.getUsersByDatesBetween(startDate, endDate);
		return ResponseEntity.ok(users);
	}

	private boolean isUserAgeValid(LocalDate birthDate) {
		LocalDate currentDate = LocalDate.now();
		long years = ChronoUnit.YEARS.between(birthDate, currentDate);
		return years >= userMinAge;
	}
}
