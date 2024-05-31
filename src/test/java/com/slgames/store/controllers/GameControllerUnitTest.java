package com.slgames.store.controllers;

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

import com.slgames.store.dtos.TypeDTO;
import com.slgames.store.dtos.game.DefaultResponseGameDTO;
import com.slgames.store.dtos.game.GameDTOFactory;
import com.slgames.store.dtos.game.InsertGameDTO;
import com.slgames.store.dtos.game.UpdateGameDTO;
import com.slgames.store.dtos.genre.GenreDTO;
import com.slgames.store.model.Enterprise;
import com.slgames.store.model.Game;
import com.slgames.store.model.Genre;
import com.slgames.store.model.GenreName;
import com.slgames.store.model.services.GameService;


import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.delete;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.post;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.put;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.net.URI;
import java.time.LocalDate;
import java.util.HashSet;
import java.util.List;
import java.util.Optional;
import java.util.Set;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;

@ActiveProfiles("test")
@WebMvcTest(controllers = GameController.class)
public class GameControllerUnitTest {

	@Autowired
	private GameController controller;
	
	@Autowired
	private MockMvc mock;
	
	@MockBean
	private GameService service;
	
	@BeforeEach
	public void setUp() {
		standaloneSetup(controller);
	}
	
	@Test
	@DisplayName("Should return status code Created when a valid body is sent")
	void testInsertGameReturnCreated() throws Exception {
		InsertGameDTO insertDto = new InsertGameDTO("Sample",LocalDate.now().minusMonths(1), 33.21, 1L, 2L, Set.of(new GenreDTO(GenreName.TERROR)));
		Game game = new Game(insertDto);
		game.setId(1L);
		when(service.createGame(insertDto)).thenReturn(game);
		var response = mock.perform(post(URI.create("/game"))
				.contentType(MediaType.APPLICATION_JSON)
				.content(insertDto.toString()))
		.andExpect(status().isCreated()).andReturn();
		String body = response.getResponse().getContentAsString();
		Assertions.assertTrue(body.contains("Sample"));
	}
	
	@Test
	@DisplayName("Should return status code Created when a invalid title is sent")
	void testInsertGameReturnBadRequestWhenInvalidTitleIsSent() throws Exception {
		InsertGameDTO insertDto = new InsertGameDTO("",LocalDate.now().minusMonths(1), 33.21, 1L, 2L, Set.of(new GenreDTO(GenreName.TERROR)));
		when(service.createGame(insertDto)).thenReturn(new Game(insertDto));
		mock.perform(post(URI.create("/game"))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(insertDto.toString()))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("Should return status code Bad Request when a invalid developer ID is sent")
	void testInsertGameReturnBadRequestWhenInvalidDeveloperIdIsSent() throws Exception {
		InsertGameDTO insertDto = new InsertGameDTO("Sample",LocalDate.now().minusMonths(1), 33.21, -1L, 2L, Set.of(new GenreDTO(GenreName.TERROR)));
		when(service.createGame(insertDto)).thenThrow(new IllegalArgumentException("No existing developer id"));
		mock.perform(post(URI.create("/game"))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(insertDto.toString()))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("Should return status code Created when a invalid publisher ID is sent")
	void testInsertGameReturnBadRequestWhenInvalidPublisherIdIsSent() throws Exception {
		InsertGameDTO insertDto = new InsertGameDTO("Sample",LocalDate.now().minusMonths(1), 33.21, 1L, -1L, Set.of(new GenreDTO(GenreName.TERROR)));
		when(service.createGame(insertDto)).thenThrow(new IllegalArgumentException("No existing publisher id"));
		mock.perform(post(URI.create("/game"))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(insertDto.toString()))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("Should return status code Bad Request when a negative price is sent")
	void testInsertGameReturnBadRequestWhenNegativePriceIsSent() throws Exception {
		InsertGameDTO insertDto = new InsertGameDTO("Sample" ,LocalDate.now().minusMonths(1), -33.21, 1L, 2L, Set.of(new GenreDTO(GenreName.TERROR)));
		when(service.createGame(insertDto)).thenThrow(new IllegalArgumentException("Price must be not negative"));
		mock.perform(post(URI.create("/game"))
				.accept(MediaType.APPLICATION_JSON)
				.contentType(MediaType.APPLICATION_JSON)
				.content(insertDto.toString()))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("Should return status code Ok when call with a existing ID")
	public void testFindByIdReturnOkWhenExistingIdIsSent() throws Exception {
		when(service.findById(1L))
		.thenReturn(Optional.of(Game.builder()
				.id(1L)
				.title("Nier: Automata")
				.developer(new Enterprise(1L, "PlatinumGames", LocalDate.of(2005, 6, 7)))
				.publisher(new Enterprise(2L, "Square Enix", LocalDate.of(2003, 4, 1)))
				.launchDate(LocalDate.of(2016, 12, 22))
				.price(77.9)
				.genres(new HashSet<Genre>(Set.of(new Genre(new GenreDTO(GenreName.RPG)))))
				.build()));
		mock.perform(get("/game/{id}", 1L)).andExpect(status().isOk());
		
	}
	@Test
	@DisplayName("Should return status code Not Found when call with a non existing ID")
	public void testFindByIdReturnNotFoundWhenNonExistingIDIsSent() throws Exception {
		when(service.findById(-1L))
		.thenReturn(Optional.empty());
		mock.perform(get("/game/{id}", -1L)).andExpect(status().isNotFound());
		
	}
	
	@Test
	@DisplayName("Should return status code Ok")
	public void testFindAllReturnOk() throws Exception {
		Enterprise developer = new Enterprise(1L, "Sample", LocalDate.of(1999, 1, 1));
		Game game = Game.builder().id(1L)
				.developer(developer)
				.publisher(new Enterprise(2L, "Sample 2", LocalDate.of(1999, 1, 1)))
				.price(55.21)
				.launchDate(LocalDate.of(2000, 1, 1))
				.genres(Set.of(new Genre(1L, GenreName.PUZZLE)))
				.build();
		List<DefaultResponseGameDTO> dtos = List.of((DefaultResponseGameDTO)
				GameDTOFactory.createDTO(game, TypeDTO.DEFAULT));
		when(service.findAll()).thenReturn(dtos);
		mock.perform(get("/game")).andExpect(status().isOk());
	}
	
	@Test
	@DisplayName("Should return status code Ok when a valid body to update is sent")
	public void testUpdateGameReturnOkWhenAValidBodyIsProvided() throws Exception {
		UpdateGameDTO dto = new UpdateGameDTO(1L, "Updated Sample", null, null);
		Game game = Game.builder()
				.genres(Set.of(new Genre(1L, GenreName.RPG)))
				.build();
		game.update(dto);
		when(service.update(dto)).thenReturn(game);
		var response = mock.perform(put("/game")
				.contentType(MediaType.APPLICATION_JSON)
				.content(dto.toString()))
		.andExpect(status().isOk()).andReturn();
		
		String body = response.getResponse().getContentAsString();
		Assertions.assertTrue(body.contains("Updated Sample"));
	}
	
	@Test
	@DisplayName("Should return status code Not Found when a invalid ID is sent")
	public void testUpdateGameReturnNotFoundWhenAInvalidIDIsSent() throws Exception {
		UpdateGameDTO game = new UpdateGameDTO(-1L, "Sample", null, null);
		when(service.update(game)).thenReturn(null);
		mock.perform(put("/game")
				.contentType(MediaType.APPLICATION_JSON)
				.content(game.toString()))
		.andExpect(status().isNotFound());
		
	}
	@Test
	@DisplayName("Should return status code Bad Request when a null ID is sent")
	public void testUpdateGameReturnBadRequestWhenANullIDIsSent() throws Exception {
		UpdateGameDTO game = new UpdateGameDTO(null, "Sample", null, null);
		when(service.update(game)).thenReturn(null);
		mock.perform(put("/game")
				.contentType(MediaType.APPLICATION_JSON)
				.content(game.toString()))
		.andExpect(status().isBadRequest());
	}
	
	@Test
	@DisplayName("Should return status code No Content when a existing ID is sent")
	public void testDeleteGameReturnNoContentWhenAExistingIDIsSent() throws Exception {
		long id = 1L;
		when(service.delete(id)).thenReturn(true);
		mock.perform(delete("/game/{id}", id)).andExpect(status().isNoContent());
	}
	
	@Test
	@DisplayName("Should return status code Not Found when a Non existing ID is sent")
	public void testDeleteGameReturnNotFoundWhenANonExistingIDIsSent() throws Exception {
		long id = -1L;
		when(service.delete(id)).thenReturn(false);
		mock.perform(delete("/game/{id}", id)).andExpect(status().isNotFound());
	}
	
}
