package com.example.UserRestAPI.service;

import com.example.UserRestAPI.database.entity.UserEntity;
import com.example.UserRestAPI.database.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

@Service
@RequiredArgsConstructor
public class UserService {
	private final UserRepository userRepository;

	public UserEntity create(UserEntity user) {
		return userRepository.save(user);
	}

	public UserEntity readById(long id) {
		return userRepository.findById(id);
	}

	public UserEntity update(UserEntity user) {
		UserEntity oldUser = readById(user.getId());
		return userRepository.save(oldUser);
	}

	public void delete(long id) {
		userRepository.delete(readById(id));
	}

	public List<UserEntity> getAll() {
		return userRepository.findAll();
	}

	public List<UserEntity> getUsersByDatesBetween(LocalDate startDate, LocalDate endDate){
		return userRepository.findByBirthDateBetween(startDate, endDate);
	}
}