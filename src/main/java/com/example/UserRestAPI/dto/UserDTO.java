package com.example.UserRestAPI.dto;

import com.example.UserRestAPI.database.entity.UserEntity;
import jakarta.validation.constraints.Pattern;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDate;

@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class UserDTO {
	private long id;

	private String firstName;

	private String lastName;

	@Pattern(regexp = "[\\w-\\.]+@([\\w-]+\\.)+[\\w-]{2,4}", message = "Must be a valid e-mail address")
	private String email;

	private LocalDate birthDate;

	public static UserDTO transformToDTO(UserEntity user) {
		return new UserDTO(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getBirthDate());
	}

	public static UserEntity transformToEntity(UserDTO user) {
		return new UserEntity(user.getId(), user.getFirstName(), user.getLastName(), user.getEmail(), user.getBirthDate());
	}
}