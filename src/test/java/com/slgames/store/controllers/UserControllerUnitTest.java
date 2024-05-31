package com.slgames.store.controllers;

import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;

import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.http.MediaType;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.slgames.store.dtos.users.InsertUserDTO;
import com.slgames.store.dtos.users.UpdateUserDTO;
import com.slgames.store.model.User;
import com.slgames.store.model.services.UserService;

import lombok.Getter;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;

@WebMvcTest(controllers = UserController.class)
@ActiveProfiles("test")
@Getter
public class UserControllerUnitTest {

	@Autowired
	private UserController controller;
	
	@Autowired
	private MockMvc mock;
	
	@Autowired
	private ObjectMapper mapper;
	@MockBean
	private UserService service; 
	
	
	@BeforeEach
	public void setUp() {
		standaloneSetup(controller);
	}
	
	@Test
	@DisplayName("Should return status code 201")
	public void testInsertUserReturnCreated() throws Exception {
		InsertUserDTO dto = new InsertUserDTO("sample", "sample@gmail.com", "112233", "DEFAULT");
		User user = new User(dto);
		
		user.setId(1L);
		when(getService().createUser(dto)).thenReturn(user);
		String json = mapper.writeValueAsString(dto);
		var response = mock.perform(post(URI.create("/user"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andExpect(status().isCreated()).andReturn();
		String body = response.getResponse().getContentAsString();
		Assertions.assertTrue(body.contains("sample"));
	}
	
	@Test
	@DisplayName("Should return status code 404 when a user with ADM role is sent to insert")
	public void testInsertUserReturnBadRequestWhenRoleADMIsSet() throws Exception{
		InsertUserDTO dto = new InsertUserDTO("sample", "sample@gmail.com", "112233", "adm");
		when(getService().createUser(dto)).thenThrow(new IllegalArgumentException("Cannot create user with ADM role"));
		String json = mapper.writeValueAsString(dto);
		var response = mock.perform(post(URI.create("/user"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andExpect(status().isBadRequest()).andReturn();
	}
	@Test
	@DisplayName("Should return status code Ok")
	public void testUpdateUserReturnOk() throws Exception {
		UpdateUserDTO dto = new UpdateUserDTO(1L, "Sample", "", "123456");
		User user = new User(dto);
		
		when(getService().updateUser(dto)).thenReturn(user);
		String json = mapper.writeValueAsString(dto);
		var response = mock.perform(put(URI.create("/user"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(json))
		.andExpect(status().isOk()).andReturn();
		String body = response.getResponse().getContentAsString();
		Assertions.assertTrue(body.contains("Sample"));
		
	}
	
	@Test
	@DisplayName("Should return status code No content")
	public void testDeleteUserReturnStatusCodeNoContent() throws Exception {
		Long id = 1L;
		when(getService().deleteUser(id)).thenReturn(true);
		mock.perform(delete(URI.create("/user/" + id)))
		.andExpect(status().isNoContent());
	}
}
