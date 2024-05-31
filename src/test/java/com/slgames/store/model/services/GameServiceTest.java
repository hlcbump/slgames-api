package com.slgames.store.model.services;



import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.test.context.ActiveProfiles;

import com.slgames.store.dtos.game.DefaultResponseGameDTO;
import com.slgames.store.dtos.game.InsertGameDTO;
import com.slgames.store.dtos.game.UpdateGameDTO;
import com.slgames.store.dtos.genre.GenreDTO;
import com.slgames.store.model.Enterprise;
import com.slgames.store.model.Game;
import com.slgames.store.model.Genre;
import com.slgames.store.model.GenreName;
import com.slgames.store.model.repository.GameRepository;

import static io.restassured.module.mockmvc.RestAssuredMockMvc.standaloneSetup;
import static org.mockito.Mockito.when;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;
import java.util.Set;



@SpringBootTest
@ActiveProfiles("test")
public class GameServiceTest {
	
	
	@Autowired
	private GameService service;
	@MockBean
	private GameRepository repository;
	
	@MockBean
	private EnterpriseService enterpriseService;
	
	@MockBean 
	private GenreService genreService;
	
	@BeforeEach
	public void setUp() {
		standaloneSetup(service);
	}
	
	@Test
	public void testServiceNotNull() {
		Assertions.assertNotNull(service);
	}
	
	@Test
	@DisplayName("Should insert a Game when a valid InsertGameDto is sent")
	public void testCreateGameReturnGameWhenAValidInsertGameDTOIsSent() throws CloneNotSupportedException {
		
		LocalDate launchDate = LocalDate.of(2000, 1, 1);
		InsertGameDTO dto = new InsertGameDTO("Sample", launchDate,
				55.12, 1L, 2L, Set.of(new GenreDTO(GenreName.PUZZLE)));
		Game expectedGame = new Game(dto);
		Enterprise developer = new Enterprise(1L, "Sample", LocalDate.of(1999, 1, 1));
		Enterprise publisher = new Enterprise(2L, "Sample 2", LocalDate.of(1999, 1, 1));
		
		
		expectedGame.setDeveloper(developer);
		expectedGame.setPublisher(publisher);
		expectedGame.getGenres().forEach(genre -> genre.setId(5L));
		
		Game returnGame = expectedGame.clone();
		
		returnGame.setId(1L);
		
		
		when(genreService.getIdByGenreName(GenreName.PUZZLE)).thenReturn(5L);
		when(repository.existsByTitle("Sample")).thenReturn(false);
		when(enterpriseService.existsById(developer.getId())).thenReturn(true);
		when(enterpriseService.existsById(publisher.getId())).thenReturn(true);
		when(enterpriseService.findById(developer.getId())).thenReturn(Optional.of(developer));
		when(enterpriseService.findById(publisher.getId())).thenReturn(Optional.of(publisher));
		when(repository.save(expectedGame)).thenReturn(returnGame);
		
		Game savedGame = service.createGame(dto);
		Assertions.assertEquals(returnGame, savedGame);
		Assertions.assertEquals(returnGame.getTitle(), savedGame.getTitle());
		Assertions.assertEquals(returnGame.getPrice(), savedGame.getPrice());
		Assertions.assertEquals(returnGame.getLaunchDate(), savedGame.getLaunchDate());
		Assertions.assertEquals(returnGame.getDeveloper(), savedGame.getDeveloper());
		Assertions.assertEquals(returnGame.getPublisher(), savedGame.getPublisher());
		Assertions.assertEquals(returnGame.getGenres(), savedGame.getGenres());
		Assertions.assertEquals(5L, savedGame.getGenres().iterator().next().getId());
		
	}
	@Test
	@DisplayName("Shouldn't throw any exception when verify a valid game")
	public void testValidateNewGameDontThrowAnyExceptionWhenAValidGameIsSent() {
		Enterprise developer = new Enterprise(1L, "Sample", LocalDate.of(1999, 1, 1));
		Enterprise publisher = new Enterprise(2L, "Sample 2", LocalDate.of(1999, 1, 1));
		Game game = Game.builder().title("Sample")
				.launchDate(LocalDate.of(2000, 1, 1))
				.price(55.12)
				.developer(developer)
				.publisher(publisher)
				.genres(Set.of(new Genre(GenreName.PUZZLE)))
				.build();
		
		
		when(repository.existsByTitle(game.getTitle())).thenReturn(false);
		when(enterpriseService.existsById(developer.getId())).thenReturn(true);
		when(enterpriseService.existsById(publisher.getId())).thenReturn(true);
		
		Assertions.assertDoesNotThrow(() -> service.validateNewGame(game));
	}
	
	@Test
	@DisplayName("Should throw IllegalArgumentException when a game with existing title is provided")
	public void testValidateNewGameThrowIllegalArgumentExceptionWhenGameTitleExists() {
		Enterprise developer = new Enterprise(1L, "Sample", LocalDate.of(1999, 1, 1));
		Enterprise publisher = new Enterprise(2L, "Sample 2", LocalDate.of(1999, 1, 1));
		Game game = Game.builder().title("Sample")
				.launchDate(LocalDate.of(2000, 1, 1))
				.price(55.12)
				.developer(developer)
				.publisher(publisher)
				.genres(Set.of(new Genre(GenreName.PUZZLE)))
				.build();
		when(repository.existsByTitle(game.getTitle())).thenReturn(false);

		Assertions.assertThrows(IllegalArgumentException.class, () -> service.validateNewGame(game));
	}
	
	@Test
	@DisplayName("Should throw IllegalArgumentException when a game with negative price is provided")
	public void testValidateNewGameThrowIllegalArgumentExceptionWhenGamePriceIsNegative() {
		Enterprise developer = new Enterprise(1L, "Sample", LocalDate.of(1999, 1, 1));
		Enterprise publisher = new Enterprise(2L, "Sample 2", LocalDate.of(1999, 1, 1));
		Game game = Game.builder().title("Sample")
				.launchDate(LocalDate.of(2000, 1, 1))
				.price(-0.01)
				.developer(developer)
				.publisher(publisher)
				.genres(Set.of(new Genre(GenreName.PUZZLE)))
				.build();
		when(repository.existsByTitle(game.getTitle())).thenReturn(false);
		when(enterpriseService.existsById(developer.getId())).thenReturn(false);
		when(enterpriseService.existsById(publisher.getId())).thenReturn(true);

		Assertions.assertThrows(IllegalArgumentException.class, () -> service.validateNewGame(game));
	}
	
	@Test
	@DisplayName("Should throw IllegalArgumentException when a game with non existing developer is provided")
	public void testValidateNewGameThrowIllegalArgumentExceptionWhenDeveloperDontExists() {
		Enterprise developer = new Enterprise(-1L, "Sample", LocalDate.of(1999, 1, 1));
		Enterprise publisher = new Enterprise(2L, "Sample 2", LocalDate.of(1999, 1, 1));
		Game game = Game.builder().title("Sample")
				.launchDate(LocalDate.of(2000, 1, 1))
				.price(55.12)
				.developer(developer)
				.publisher(publisher)
				.genres(Set.of(new Genre(GenreName.PUZZLE)))
				.build();
		when(repository.existsByTitle(game.getTitle())).thenReturn(false);
		when(enterpriseService.existsById(developer.getId())).thenReturn(false);
		when(enterpriseService.existsById(publisher.getId())).thenReturn(true);
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> service.validateNewGame(game));
	}
	@Test
	@DisplayName("Should throw IllegalArgumentException when a game with non existing publisher is provided")
	public void testValidateNewGameThrowIllegalArgumentExceptionWhenPublisherDontExists() {
		Enterprise developer = new Enterprise(1L, "Sample", LocalDate.of(1999, 1, 1));
		Enterprise publisher = new Enterprise(-2L, "Sample 2", LocalDate.of(1999, 1, 1));
		Game game = Game.builder().title("Sample")
				.launchDate(LocalDate.of(2000, 1, 1))
				.price(55.12)
				.developer(developer)
				.publisher(publisher)
				.genres(Set.of(new Genre(GenreName.PUZZLE)))
				.build();
		when(repository.existsByTitle(game.getTitle())).thenReturn(false);
		when(enterpriseService.existsById(developer.getId())).thenReturn(true);
		when(enterpriseService.existsById(publisher.getId())).thenReturn(false);
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> service.validateNewGame(game));
	}
	
	@Test
	@DisplayName("Should return true when a game price is positive or equals 0.00")
	public void testIsPositiveOrFreeReturnTrueWhenAGameWithValidPriceIsSent(){
		Enterprise developer = new Enterprise(1L, "Sample", LocalDate.of(1999, 1, 1));
		Enterprise publisher = new Enterprise(2L, "Sample 2", LocalDate.of(1999, 1, 1));
		Game game = Game.builder().title("Sample")
				.launchDate(LocalDate.of(2000, 1, 1))
				.price(55.12)
				.developer(developer)
				.publisher(publisher)
				.genres(Set.of(new Genre(GenreName.PUZZLE)))
				.build();
		Assertions.assertTrue(service.isPositiveOrFree(game.getPrice()));
	}
	@Test
	@DisplayName("Should return false when a game price is negative")
	public void testIsPositiveOrFreeReturnFalseWhenAGameWithInvalidPriceIsSent(){
		Enterprise developer = new Enterprise(1L, "Sample", LocalDate.of(1999, 1, 1));
		Enterprise publisher = new Enterprise(2L, "Sample 2", LocalDate.of(1999, 1, 1));
		Game game = Game.builder().title("Sample")
				.launchDate(LocalDate.of(2000, 1, 1))
				.price(-0.01)
				.developer(developer)
				.publisher(publisher)
				.genres(Set.of(new Genre(GenreName.PUZZLE)))
				.build();
		Assertions.assertFalse(service.isPositiveOrFree(game.getPrice()));
	}
	
	@Test
	@DisplayName("Should return false when a game title don't exists")
	public void testExistsByTitleReturnFalseWhenAGameTitleDontExists(){
		Enterprise developer = new Enterprise(1L, "Sample", LocalDate.of(1999, 1, 1));
		Enterprise publisher = new Enterprise(2L, "Sample 2", LocalDate.of(1999, 1, 1));
		Game game = Game.builder().title("Sample")
				.launchDate(LocalDate.of(2000, 1, 1))
				.price(55.12)
				.developer(developer)
				.publisher(publisher)
				.genres(Set.of(new Genre(GenreName.PUZZLE)))
				.build();
		when(repository.existsByTitle(game.getTitle())).thenReturn(false);
		Assertions.assertFalse(service.existsByTitle(game.getTitle()));
	}
	@Test
	@DisplayName("Should return true when a game title exists")
	public void testExistsByTitleReturnTrueWhenAGameTitleExists(){
		Enterprise developer = new Enterprise(1L, "Sample", LocalDate.of(1999, 1, 1));
		Enterprise publisher = new Enterprise(2L, "Sample 2", LocalDate.of(1999, 1, 1));
		Game game = Game.builder().title("Sample")
				.launchDate(LocalDate.of(2000, 1, 1))
				.price(55.12)
				.developer(developer)
				.publisher(publisher)
				.genres(Set.of(new Genre(GenreName.PUZZLE)))
				.build();
		when(repository.existsByTitle(game.getTitle())).thenReturn(true);
		Assertions.assertTrue(service.existsByTitle(game.getTitle()));
	}
	
	@Test
	@DisplayName("Should return true when a game developer exists")
	public void testExistsEnterpriseReturnTrueWhenADeveloperExists() {
		Enterprise developer = new Enterprise(1L, "Sample", LocalDate.of(1999, 1, 1));
		Enterprise publisher = new Enterprise(2L, "Sample 2", LocalDate.of(1999, 1, 1));
		Game game = Game.builder().title("Sample")
				.launchDate(LocalDate.of(2000, 1, 1))
				.price(55.12)
				.developer(developer)
				.publisher(publisher)
				.genres(Set.of(new Genre(GenreName.PUZZLE)))
				.build();
		when(enterpriseService.existsById(developer.getId())).thenReturn(true);
		
		Assertions.assertTrue(service.existsEnterprise(game.getDeveloperId()));
	}
	
	@Test
	@DisplayName("Should return false when a game developer don't exists")
	public void testExistsEnterpriseReturnFalseWhenADeveloperDontExists() {
		Enterprise developer = new Enterprise(-1L, "Sample", LocalDate.of(1999, 1, 1));
		Enterprise publisher = new Enterprise(2L, "Sample 2", LocalDate.of(1999, 1, 1));
		Game game = Game.builder().title("Sample")
				.launchDate(LocalDate.of(2000, 1, 1))
				.price(55.12)
				.developer(developer)
				.publisher(publisher)
				.genres(Set.of(new Genre(GenreName.PUZZLE)))
				.build();
		when(enterpriseService.existsById(developer.getId())).thenReturn(false);
		
		Assertions.assertFalse(service.existsEnterprise(game.getDeveloperId()));
	}
	
	@Test
	@DisplayName("Should return true when a game publisher exists")
	public void testExistsEnterpriseReturnTrueWhenAPublisherExists() {
		Enterprise developer = new Enterprise(1L, "Sample", LocalDate.of(1999, 1, 1));
		Enterprise publisher = new Enterprise(2L, "Sample 2", LocalDate.of(1999, 1, 1));
		Game game = Game.builder().title("Sample")
				.launchDate(LocalDate.of(2000, 1, 1))
				.price(55.12)
				.developer(developer)
				.publisher(publisher)
				.genres(Set.of(new Genre(GenreName.PUZZLE)))
				.build();
		when(enterpriseService.existsById(publisher.getId())).thenReturn(true);
		
		Assertions.assertTrue(service.existsEnterprise(game.getPublisherId()));
	}
	@Test
	@DisplayName("Should return false when a game publisher don't exists")
	public void testExistsEnterpriseReturnTrueWhenAPublisherDontExists() {
		Enterprise developer = new Enterprise(1L, "Sample", LocalDate.of(1999, 1, 1));
		Enterprise publisher = new Enterprise(-2L, "Sample 2", LocalDate.of(1999, 1, 1));
		Game game = Game.builder().title("Sample")
				.launchDate(LocalDate.of(2000, 1, 1))
				.price(55.12)
				.developer(developer)
				.publisher(publisher)
				.genres(Set.of(new Genre(GenreName.PUZZLE)))
				.build();
		when(enterpriseService.existsById(publisher.getId())).thenReturn(false);
		
		Assertions.assertFalse(service.existsEnterprise(game.getPublisherId()));
	}
	
	@Test
	@DisplayName("Should return a DefaultResponseGameDTO List when it's called")
	public void testFindAllReturnDefaultResponseGameDTOList() {
		Enterprise developer = new Enterprise(1L, "Sample", LocalDate.of(1999, 1, 1));
		Enterprise publisher = new Enterprise(2L, "Sample 2", LocalDate.of(1999, 1, 1));
		Game game = Game.builder().title("Sample")
				.launchDate(LocalDate.of(2000, 1, 1))
				.price(55.12)
				.developer(developer)
				.publisher(publisher)
				.genres(Set.of(new Genre(GenreName.PUZZLE)))
				.build();
		List<DefaultResponseGameDTO> gameDtos = List.of(new DefaultResponseGameDTO(game));
		when(repository.findAll()).thenReturn(List.of(game));
		
		Assertions.assertEquals(gameDtos, service.findAll());
	}
	
	@Test
	@DisplayName("Should return a present game when a existing and valid id is provided")
	public void testFindByIDReturnAPresentGameWhenAValidAndExistingIDIsProvided() {
		Enterprise developer = new Enterprise(1L, "Sample", LocalDate.of(1999, 1, 1));
		Enterprise publisher = new Enterprise(2L, "Sample 2", LocalDate.of(1999, 1, 1));
		Long id = 1L;
		Game game = createGameSample(id, developer, publisher);
		when(repository.findById(id)).thenReturn(Optional.of(game));
		Assertions.assertTrue(service.findById(id).isPresent());
	}
	@Test
	@DisplayName("Should return a empty game when a not existing id is provided")
	public void testFindByIDReturnAEmptyGameWhenANonExistingIDIsProvided() {
		Long id = -1L;
		when(repository.findById(id)).thenReturn(Optional.empty());
		Assertions.assertTrue(service.findById(id).isEmpty());
	}
	
	@Test
	@DisplayName("Should update a game")
	public void testUpdateAllItensWhenAValidUpdateDTOIsProvided() throws CloneNotSupportedException {
		Long id = 1L;
		Enterprise developer = new Enterprise(1L, "Sample", LocalDate.of(1999, 1, 1));
		Enterprise publisher = new Enterprise(2L, "Sample 2", LocalDate.of(1999, 1, 1));
		Game beforeUpdate = createGameSample(id, developer, publisher);
		String expectedUpdatedTitle = "Updated Sample";
		LocalDate expectedUpdatedLaunchDate = LocalDate.of(2001, 1, 1);
		double expectedUpdatedPrice = 54.00;
		UpdateGameDTO dto = new UpdateGameDTO(id, expectedUpdatedTitle, expectedUpdatedLaunchDate, expectedUpdatedPrice);
		
		when(repository.findById(id)).thenReturn(Optional.of(beforeUpdate));
		Game afterUpdate = beforeUpdate.clone();
		afterUpdate.update(dto);
		when(repository.save(afterUpdate)).thenReturn(afterUpdate);
		
		Game actualAfterUpdate = service.update(dto);
		
		Assertions.assertEquals(afterUpdate, actualAfterUpdate);
		Assertions.assertEquals(expectedUpdatedTitle, actualAfterUpdate.getTitle());
		Assertions.assertEquals(expectedUpdatedLaunchDate, actualAfterUpdate.getLaunchDate());
		Assertions.assertEquals(expectedUpdatedPrice, actualAfterUpdate.getPrice());
	}
	@Test
	@DisplayName("Should return null when a invalid ID is provided")
	public void testUpdateReturnNullWhenAInvalidIDIsProvided() throws CloneNotSupportedException {
		Long id = -1L;
		Enterprise developer = new Enterprise(1L, "Sample", LocalDate.of(1999, 1, 1));
		Enterprise publisher = new Enterprise(2L, "Sample 2", LocalDate.of(1999, 1, 1));
		Game beforeUpdate = createGameSample(id, developer, publisher);
		String expectedUpdatedTitle = "Updated Sample";
		LocalDate expectedUpdatedLaunchDate = LocalDate.of(2001, 1, 1);
		double expectedUpdatedPrice = 54.00;
		UpdateGameDTO dto = new UpdateGameDTO(id, expectedUpdatedTitle, expectedUpdatedLaunchDate, expectedUpdatedPrice);
		
		when(repository.findById(id)).thenReturn(Optional.empty());
		Game afterUpdate = beforeUpdate.clone();
		afterUpdate.update(dto);
		when(repository.save(afterUpdate)).thenReturn(afterUpdate);
		
		Game actualAfterUpdate = service.update(dto);
		
		Assertions.assertNull(actualAfterUpdate);
	}
	@Test
	@DisplayName("Should don't alter the title when the provided it's empty, even when the ID exists")
	public void testUpdateDontChangeTitleWhenTheProvidedIsEmpty() throws CloneNotSupportedException {
		Long id = 1L;
		Enterprise developer = new Enterprise(1L, "Sample", LocalDate.of(1999, 1, 1));
		Enterprise publisher = new Enterprise(2L, "Sample 2", LocalDate.of(1999, 1, 1));
		Game beforeUpdate = createGameSample(id, developer, publisher);
		String expectedUpdatedTitle = beforeUpdate.getTitle();
		LocalDate expectedUpdatedLaunchDate = LocalDate.of(2001, 1, 1);
		double expectedUpdatedPrice = 54.00;
		UpdateGameDTO dto = new UpdateGameDTO(id, "", expectedUpdatedLaunchDate, expectedUpdatedPrice);
		
		when(repository.findById(id)).thenReturn(Optional.of(beforeUpdate));
		Game afterUpdate = beforeUpdate.clone();
		afterUpdate.update(dto);
		when(repository.save(afterUpdate)).thenReturn(afterUpdate);
		
		Game actualAfterUpdate = service.update(dto);
		
		Assertions.assertEquals(afterUpdate, actualAfterUpdate);
		Assertions.assertEquals(expectedUpdatedTitle, actualAfterUpdate.getTitle());
	}
	@Test
	@DisplayName("Should throw IllegalArgumentException when trying to update game price with a negative value")
	public void testUpdateThrowIllegalArgumentExceptionWhenUpdatedPriceIsNegative() throws CloneNotSupportedException {
		Long id = 1L;
		Enterprise developer = new Enterprise(1L, "Sample", LocalDate.of(1999, 1, 1));
		Enterprise publisher = new Enterprise(2L, "Sample 2", LocalDate.of(1999, 1, 1));
		Game beforeUpdate = createGameSample(id, developer, publisher);
		LocalDate expectedUpdatedLaunchDate = LocalDate.of(2001, 1, 1);
		double expectedUpdatedPrice = -54.00;
		UpdateGameDTO dto = new UpdateGameDTO(id, "", expectedUpdatedLaunchDate, expectedUpdatedPrice);
		
		when(repository.findById(id)).thenReturn(Optional.of(beforeUpdate));
		Game afterUpdate = beforeUpdate.clone();
		afterUpdate.update(dto);
		when(repository.save(afterUpdate)).thenReturn(afterUpdate);
		
		Assertions.assertThrows(IllegalArgumentException.class, () -> service.update(dto));
	}
	@Test
	@DisplayName("Should delete a game")
	public void testDelete() {
		Long id = 1L;
		Enterprise developer = new Enterprise(1L, "Sample", LocalDate.of(1999, 1, 1));
		Enterprise publisher = new Enterprise(2L, "Sample 2", LocalDate.of(1999, 1, 1));
		Game game = createGameSample(id, developer, publisher);
		
		when(repository.findById(id)).thenReturn(Optional.of(game));
		
		Assertions.assertTrue(service.delete(id));
	}

	private Game createGameSample(Long id, Enterprise developer, Enterprise publisher) {
		Game game =Game.builder().title("Sample")
				.id(id)
				.launchDate(LocalDate.of(2000, 1, 1))
				.price(55.12)
				.developer(developer)
				.publisher(publisher)
				.genres(Set.of(new Genre(1L, GenreName.PUZZLE)))
				.build();
		return game;
	}
	
	@Test
	@DisplayName("Should return false when the ID is invalid or don't exists game")
	public void testDeleteReturnFalseWhenIDDontExists() {
		Long id = -1L;
		
		when(repository.findById(id)).thenReturn(Optional.empty());
		
		Assertions.assertFalse(service.delete(id));
	}
}
