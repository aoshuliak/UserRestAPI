package com.example.UserRestAPI;

import com.example.UserRestAPI.controller.UserController;
import com.example.UserRestAPI.database.entity.UserEntity;
import com.example.UserRestAPI.dto.UserDTO;
import com.example.UserRestAPI.service.UserService;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.request.MockMvcRequestBuilders;
import org.springframework.test.web.servlet.result.MockMvcResultMatchers;

import java.time.LocalDate;
import java.util.ArrayList;
import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.when;

@WebMvcTest(UserController.class)
public class UserControllerTests {

	@Autowired
	private MockMvc mockMvc;

	@MockBean
	private UserService userService;

	@InjectMocks
	private UserController userController;

	@Test
	public void testCreateUser() throws Exception {
		UserEntity user = new UserEntity();
		user.setFirstName("Andrii");
		user.setLastName("Shu");
		user.setBirthDate(LocalDate.of(2000, 1, 1));

		when(userService.create(any(UserEntity.class))).thenReturn(user);

		mockMvc.perform(MockMvcRequestBuilders.post("/users")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"firstName\": \"Andrii\", \"lastName\": \"Shu\", \"birthDate\": \"2000-01-01\"}"))
				.andExpect(MockMvcResultMatchers.status().isCreated());
	}

	@Test
	public void testGetUserById() throws Exception {
		UserEntity user = new UserEntity();
		user.setId(1L);
		user.setFirstName("Andrii");
		user.setLastName("Shu");
		user.setBirthDate(LocalDate.of(2000, 1, 1));

		when(userService.readById(1L)).thenReturn(user);

		mockMvc.perform(MockMvcRequestBuilders.get("/users/1"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.birthDate").value("2000-01-01"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.id").value(1))
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Andrii"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Shu"))
				.andExpect(MockMvcResultMatchers.status().isOk());

		mockMvc.perform(MockMvcRequestBuilders.get("/users/5") // if such user doesnt exist
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void testUpdateUserByRequiredFields() throws Exception {
		UserEntity user = new UserEntity();
		user.setId(1L);
		user.setFirstName("Andrii");
		user.setLastName("Shu");
		user.setEmail("aoshuliak@gmail.com");
		user.setBirthDate(LocalDate.of(2000, 1, 1));

		UserDTO updatedUser = new UserDTO();
		updatedUser.setId(1L);
		updatedUser.setFirstName("Andrew");
		updatedUser.setLastName("Shu");
		updatedUser.setEmail("andrij.shuljak@gmail.com");
		updatedUser.setBirthDate(LocalDate.of(2000, 10, 19));

		when(userService.readById(1L)).thenReturn(user);

		when(userService.update(any(UserEntity.class))).thenReturn(UserDTO.transformToEntity(updatedUser));

		mockMvc.perform(MockMvcRequestBuilders.put("/users/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"firstName\": \"Andrew\", \"lastName\": \"Shu\", \"email\": \"andrij.shuljak@gmail.com\", \"birthDate\": \"2000-10-19\"}"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Andrew"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.lastName").value("Shu"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.email").value("andrij.shuljak@gmail.com"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.birthDate").value("2000-10-19"));
	}

	@Test
	public void testUpdateSomeUserFields() throws Exception {
		UserEntity user = new UserEntity();
		user.setId(1L);
		user.setFirstName("Andrii");
		user.setLastName("Shu");
		user.setEmail("aoshuliak@gmail.com");
		user.setBirthDate(LocalDate.of(2000, 1, 1));
		user.setPhoneNumber("0222222222");
		user.setAddress("Avenue 5 st.");

		UserEntity updatedUser = new UserEntity();
		updatedUser.setId(1L);
		updatedUser.setFirstName("Somebody");
		updatedUser.setEmail("whoisthis@google.com");
		updatedUser.setPhoneNumber("1111111111");

		when(userService.readById(1L)).thenReturn(user);

		when(userService.update(any(UserEntity.class))).thenReturn(updatedUser);

		mockMvc.perform(MockMvcRequestBuilders.patch("/users/1")
				.contentType(MediaType.APPLICATION_JSON)
				.content("{\"firstName\": \"Somebody\", \"email\": \"whoisthis@google.com\", \"phoneNumber\": \"1111111111\"}"))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.firstName").value("Somebody"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.email").value("whoisthis@google.com"))
				.andExpect(MockMvcResultMatchers.jsonPath("$.phoneNumber").value("1111111111"));
	}

	@Test
	public void testGetAllUsersWithNoUsersAvailable() throws Exception {
		List<UserEntity> users = new ArrayList<>();
		users.add(UserEntity.builder()
				.id(1L)
				.firstName("Andrii")
				.lastName("Shu")
				.build());

		users.add(UserEntity.builder()
				.id(2L)
				.firstName("Andrew")
				.lastName("Shu")
				.build());

		when(userService.getAll()).thenReturn(users);

		mockMvc.perform(MockMvcRequestBuilders.get("/users/all")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].firstName").value("Andrii"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[0].lastName").value("Shu"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].firstName").value("Andrew"))
				.andExpect(MockMvcResultMatchers.jsonPath("$[1].lastName").value("Shu"));
	}

	@Test
	public void testDeleteUser() throws Exception {
		UserEntity user = new UserEntity();
		user.setId(1L);
		user.setFirstName("Andrii");
		user.setLastName("Shu");
		user.setEmail("aoshuliak@gmail.com");
		user.setBirthDate(LocalDate.of(2000, 1, 1));
		user.setPhoneNumber("0222222222");
		user.setAddress("Avenue 5 st.");

		when(userService.readById(1L)).thenReturn(user);

		mockMvc.perform(MockMvcRequestBuilders.delete("/users/1")
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNoContent());

		mockMvc.perform(MockMvcRequestBuilders.delete("/users/3") // if such user doesnt exist
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isNotFound());
	}

	@Test
	public void testSearchUsersByInvalidDates() throws Exception {
		LocalDate endDate = LocalDate.of(1999, 1, 1);
		LocalDate startDate = LocalDate.of(2014, 11, 1);

		List<UserEntity> users = new ArrayList<>();
		users.add(UserEntity.builder().id(2L).firstName("Oleg")
				.birthDate(LocalDate.of(2000, 4, 5))
				.build());

		users.add(UserEntity.builder().id(3L).firstName("Sam")
				.birthDate(LocalDate.of(2010, 2, 5))
				.build());

		when(userService.getUsersByDatesBetween(startDate, endDate)).thenReturn(users);

		mockMvc.perform(MockMvcRequestBuilders.get("/users/searchByDate")
				.param("startDate", startDate.toString())
				.param("endDate", endDate.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isBadRequest());

	}

	@Test
	public void testSearchUsersByValidDates() throws Exception {

		LocalDate startDate = LocalDate.of(1999, 1, 1);
		LocalDate endDate = LocalDate.of(2014, 11, 1);

		List<UserEntity> users = new ArrayList<>();

		users.add(UserEntity.builder().id(2L).firstName("Oleg")
				.birthDate(LocalDate.of(2000, 4, 5))
				.build());

		users.add(UserEntity.builder().id(3L).firstName("Sam")
				.birthDate(LocalDate.of(2010, 2, 5))
				.build());

		when(userService.getUsersByDatesBetween(startDate, endDate)).thenReturn(users);

		mockMvc.perform(MockMvcRequestBuilders.get("/users/searchByDate")
				.param("startDate", startDate.toString())
				.param("endDate", endDate.toString())
				.contentType(MediaType.APPLICATION_JSON))
				.andExpect(MockMvcResultMatchers.status().isOk())
				.andExpect(MockMvcResultMatchers.jsonPath("$.size()").value(2));
	}
}
