package com.nnk.springboot;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.DBUser;
import com.nnk.springboot.repositories.BidListRepository;

import com.nnk.springboot.repositories.DBUserRepository;
import com.nnk.springboot.services.BidListServiceImpl;
import com.nnk.springboot.services.DBUserServiceImpl;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
public class BidTests {

    @Mock
    private BidListRepository bidListRepository;

    @InjectMocks
    private BidListServiceImpl bidListService;

    @BeforeEach
    public void SetUp() {
        System.out.println("test");
    }

    @Test
    public void bidListTest() {
        BidList bid = BidList.builder()
                .account("Account Test")
                .type("Type Test")
                .bidQuantity(10d)
                .build();

        // Save
        bid = bidListRepository.save(bid);
        assertNotNull(bid.getBidListId());
        assertEquals(bid.getBidQuantity(), 10d, 10d);

        // Update
        bid.setBidQuantity(20d);
        bid = bidListRepository.save(bid);
        assertEquals(bid.getBidQuantity(), 20d, 20d);

        // Find
        List<BidList> listResult = bidListRepository.findAll();
        assertFalse(listResult.isEmpty());

        // Delete
        Integer id = bid.getBidListId();
        bidListRepository.delete(bid);
        Optional<BidList> bidList = bidListRepository.findById(id);
        assertFalse(bidList.isPresent());
    }

    @Test
    public void testAddBidList() {
        // Création d'un objet BidList fictif pour le test
        BidList bidList = BidList.builder()
                .account("Account Test")
                .type("Type Test")
                .bidQuantity(10d)
                .build();

        // Création d'un mock du repository
        BidListRepository bidListRepositoryMock = mock(BidListRepository.class);

        // Configuration du comportement du mock pour capturer l'argument passé à save
        when(bidListRepositoryMock.save(any(BidList.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Utilisation du service avec le mock
        BidListServiceImpl bidListService = new BidListServiceImpl(bidListRepositoryMock);
        BidList savedBidList = bidListService.add(bidList);

        // Vérification que la méthode save du repository a été appelée une fois
        verify(bidListRepositoryMock, times(1)).save(any(BidList.class));

        // Capturer l'argument passé à save pour vérifier les détails de l'objet sauvegardé
        ArgumentCaptor<BidList> bidListCaptor = ArgumentCaptor.forClass(BidList.class);
        verify(bidListRepositoryMock).save(bidListCaptor.capture());
        BidList capturedBidList = bidListCaptor.getValue();

        // Vérification que l'objet sauvegardé correspond à l'objet passé à la méthode add
        assertEquals(bidList.getAccount(), capturedBidList.getAccount());
        assertEquals(bidList.getType(), capturedBidList.getType());
        assertEquals(bidList.getBidQuantity(), capturedBidList.getBidQuantity());

        // Vérification que l'objet retourné par la méthode add est bien celui sauvegardé
        assertEquals(capturedBidList, savedBidList);
    }
    @Test
    public void testGetAllBidList() {
        // Création des objets BidList
        BidList bidList1 = BidList.builder()
                .account("Account Test")
                .type("Type Test")
                .bidQuantity(10d)
                .build();

        BidList bidList2 = BidList.builder()
                .account("Account Test 2")
                .type("Type Test 2")
                .bidQuantity(10d)
                .build();

        List<BidList> bidLists = Arrays.asList(bidList1, bidList2);

        // Création d'un mock pour le repository
        BidListRepository bidListRepositoryMock = mock(BidListRepository.class);

        // Configuration du comportement du mock
        when(bidListRepositoryMock.findAll()).thenReturn(bidLists);

        // Utilisation du service avec le mock
        BidListServiceImpl bidListService = new BidListServiceImpl(bidListRepositoryMock);
        List<BidList> returnBidList = bidListService.getAll();

        // Vérification du nombre d'éléments retournés
        assertEquals(2, returnBidList.size());

        // Vérification des éléments individuels
        assertEquals("Account Test", returnBidList.get(0).getAccount());
        assertEquals("Type Test", returnBidList.get(0).getType());
        assertEquals(10d, returnBidList.get(0).getBidQuantity());

        assertEquals("Account Test 2", returnBidList.get(1).getAccount());
        assertEquals("Type Test 2", returnBidList.get(1).getType());
        assertEquals(10d, returnBidList.get(1).getBidQuantity());
    }


    @Test
    public void testUpdateBidList() {
        // Créer un ID fictif pour le test
        Integer bidListId = 1;
        BidList bidListToUpdate = new BidList();
        bidListToUpdate.setAccount("TestAccount");
        bidListToUpdate.setType("TestType");
        bidListToUpdate.setBidQuantity(100.0);

        // Créer un objet BidList existant dans la base de données pour simuler la recherche
        BidList existingBidList = new BidList();
        existingBidList.setBidListId(bidListId);
        existingBidList.setAccount("ExistingAccount");
        existingBidList.setType("ExistingType");
        existingBidList.setBidQuantity(50.0);

        // Configurer le comportement simulé du repository
        when(bidListRepository.findById(bidListId)).thenReturn(Optional.of(existingBidList));
        when(bidListRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Appeler la méthode à tester
        BidList updatedBidList = bidListService.update(bidListId, bidListToUpdate);

        // Vérifier que la méthode a renvoyé une BidList mise à jour
        assertNotNull(updatedBidList);
        assertEquals(bidListId, updatedBidList.getBidListId());
        assertEquals(bidListToUpdate.getAccount(), updatedBidList.getAccount());
        assertEquals(bidListToUpdate.getType(), updatedBidList.getType());
        assertEquals(bidListToUpdate.getBidQuantity(), updatedBidList.getBidQuantity());

        // Vérifier que la méthode a bien utilisé le repository pour sauvegarder la BidList mise à jour
        verify(bidListRepository, times(1)).findById(bidListId);
        verify(bidListRepository, times(1)).save(existingBidList);
    }


    @Test
    public void testDeleteBidListById() {
        // Création d'un ID fictif pour le test
        Integer bidToDelete = 1;

        // Création d'un mock du repository
        BidListRepository bidListRepositoryMock = mock(BidListRepository.class);

        // Utilisation du service avec le mock
        BidListServiceImpl bidListService = new BidListServiceImpl(bidListRepositoryMock);
        String deletionMessage = bidListService.deleteById(bidToDelete);

        // Vérification que la méthode deleteById du repository a été appelée une fois avec le bon ID
        verify(bidListRepositoryMock, times(1)).deleteById(eq(bidToDelete));

        // Vérification du message de suppression retourné par le service
        assertEquals("bid list deleted", deletionMessage);
    }


    @Test
    void testFindById() {
        // Création d'un exemple d'objet BidList
        BidList exampleBidList = new BidList();
        exampleBidList.setBidListId(1); // Définir l'ID souhaité pour le test

        // Configurer le comportement du mock du repository
        when(bidListRepository.findById(1)).thenReturn(Optional.of(exampleBidList));

        // Appeler la méthode à tester
        BidList foundBidList = bidListService.findById(1);

        // Vérifier que la méthode a été appelée avec l'ID attendu
        verify(bidListRepository, times(1)).findById(1);

        // Vérifier que l'objet retourné n'est pas nul et correspond à l'objet attendu
        assertNotNull(foundBidList);
        assertEquals(1, foundBidList.getBidListId());
        // Ajouter d'autres assertions pour les autres champs si nécessaire
    }
}
