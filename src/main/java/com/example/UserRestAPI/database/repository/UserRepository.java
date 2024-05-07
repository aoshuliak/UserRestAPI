package com.example.UserRestAPI.database.repository;

import com.example.UserRestAPI.database.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface UserRepository extends JpaRepository<UserEntity, Long> {
	UserEntity findById(long id);

	List<UserEntity> findByBirthDateBetween(LocalDate start, LocalDate end);
}
