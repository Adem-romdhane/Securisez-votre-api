package com.nnk.springboot;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.Trade;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.repositories.TradeRepository;
import com.nnk.springboot.services.RatingServiceImpl;
import com.nnk.springboot.services.TradeServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class TradeTests {

	@Mock
	private TradeRepository tradeRepository;

	@InjectMocks
	private TradeServiceImpl tradeService;
	@Test
	public void tradeTest() {
		Trade trade = Trade.builder()
				.account("Trade Account")
				.type("Type Test")
				.build();

		// Save
		trade = tradeRepository.save(trade);
		assertNotNull(trade.getTradeId());
        assertEquals("Trade Account", trade.getAccount());

		// Update
		trade.setAccount("Trade Account Update");
		trade = tradeRepository.save(trade);
        assertEquals("Trade Account Update", trade.getAccount());

		// Find
		List<Trade> listResult = tradeRepository.findAll();
		assertFalse(listResult.isEmpty());

		// Delete
		Integer id = trade.getTradeId();
		tradeRepository.delete(trade);
		Optional<Trade> tradeList = tradeRepository.findById(id);
		assertFalse(tradeList.isPresent());
	}


	@Test
	void testAddTrade() {
		Trade tradeToAdd = new Trade(); // Créer une instance fictive de Trade à ajouter
		when(tradeRepository.save(tradeToAdd)).thenReturn(tradeToAdd); // Configurer le comportement du tradeRepository

		Trade addedTrade = tradeService.add(tradeToAdd);

		assertNotNull(addedTrade);
		assertEquals(tradeToAdd, addedTrade);

		verify(tradeRepository, times(1)).save(tradeToAdd); // Vérifier si la méthode save a été appelée une seule fois avec le bon trade
	}

	@Test
	void testGetAllTrades() {
		// Créer une liste fictive de trades
		Trade trade1 = new Trade();
		Trade trade2 = new Trade();
		List<Trade> fakeTradeList = Arrays.asList(trade1, trade2);

		// Configurer le comportement du tradeRepository pour retourner la liste fictive de trades
		when(tradeRepository.findAll()).thenReturn(fakeTradeList);

		// Appeler la méthode getAll de TradeServiceImpl
		List<Trade> retrievedTrades = tradeService.getAll();

		// Vérifier si la liste retournée n'est pas vide et a la même taille que la liste fictive
		assertNotNull(retrievedTrades);
		assertEquals(fakeTradeList.size(), retrievedTrades.size());

		// Vérifier si la méthode findAll du repository a été appelée une seule fois
		verify(tradeRepository, times(1)).findAll();
	}


	@Test
	public void testUpdateTrade() {
		// Créer un ID fictif pour le test
		Integer tradeId = 1;
		Trade tradeToUpdate = new Trade();
		tradeToUpdate.setAccount("New Account");
		tradeToUpdate.setType("New Type");
		tradeToUpdate.setBuyQuantity(100.0);

		// Créer un objet Trade existant dans la base de données pour simuler la recherche
		Trade existingTrade = new Trade();
		existingTrade.setTradeId(tradeId);
		existingTrade.setAccount("Old Account");
		existingTrade.setType("Old Type");
		existingTrade.setBuyQuantity(50.);

		// Configurer le comportement simulé du repository
		when(tradeRepository.findById(tradeId)).thenReturn(Optional.of(existingTrade));
		when(tradeRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		// Appeler la méthode à tester
		Trade updatedTrade = tradeService.update(tradeId, tradeToUpdate);

		// Vérifier que la méthode a renvoyé un Trade mis à jour
		assertNotNull(updatedTrade);
		assertEquals(tradeId, updatedTrade.getTradeId());
		assertEquals(tradeToUpdate.getAccount(), updatedTrade.getAccount());
		assertEquals(tradeToUpdate.getType(), updatedTrade.getType());
		assertEquals(tradeToUpdate.getBuyQuantity(), updatedTrade.getBuyQuantity());

		// Vérifier que la méthode a bien utilisé le repository pour sauvegarder le Trade mis à jour
		verify(tradeRepository, times(1)).findById(tradeId);
		verify(tradeRepository, times(1)).save(existingTrade);
	}

	@Test
	void testDeleteTradeById() {
		Integer tradeIdToDelete = 1;

		// Appel de la méthode deleteById
		String result = tradeService.deleteById(tradeIdToDelete);

		// Vérifier si la méthode deleteById du repository a été appelée avec le bon ID
		verify(tradeRepository, times(1)).deleteById(tradeIdToDelete);

		// Vérifier si la méthode a retourné le bon message après la suppression
		assertEquals("trade deleted", result);
	}


	@Test
	void testFindTradeByIdWhenFound() {
		Integer tradeId = 1;
		Trade foundTrade = new Trade(); // Créez un objet Trade simulé pour les besoins du test

		// Configurez le comportement simulé du repository lors de la recherche par ID
		when(tradeRepository.findById(tradeId)).thenReturn(Optional.of(foundTrade));

		// Appel de la méthode findById
		Trade result = tradeService.findById(tradeId);

		// Vérifiez si la méthode findById du repository a été appelée avec le bon ID
		verify(tradeRepository, times(1)).findById(tradeId);

		// Vérifiez si le résultat retourné par la méthode est le trade trouvé
		assertNotNull(result);
		assertEquals(foundTrade, result);
	}

	@Test
	void testFindTradeByIdWhenNotFound() {
		Integer tradeId = 1;

		// Configurez le comportement simulé du repository lors de la recherche par ID
		when(tradeRepository.findById(tradeId)).thenReturn(Optional.empty());

		// Appel de la méthode findById et capture de l'exception attendue
		RuntimeException exception = assertThrows(
				RuntimeException.class,
				() -> tradeService.findById(tradeId)
		);

		// Vérifiez si la méthode findById du repository a été appelée avec le bon ID
		verify(tradeRepository, times(1)).findById(tradeId);

		// Vérifiez le message de l'exception
		assertEquals("trade not founded ", exception.getMessage());
	}
}

