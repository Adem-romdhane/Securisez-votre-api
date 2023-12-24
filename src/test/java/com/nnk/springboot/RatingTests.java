package com.nnk.springboot;


import com.nnk.springboot.domain.Rating;
import com.nnk.springboot.repositories.RatingRepository;
import com.nnk.springboot.services.RatingServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.Arrays;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyInt;
import static org.mockito.Mockito.*;


@SpringBootTest
public class RatingTests {

    @Mock
    private RatingRepository ratingRepository;

    @InjectMocks
    private RatingServiceImpl ratingService;
    @Test
    public void ratingTest() {
        Rating rating = Rating.builder()
                .moodysRating("Moodys Rating")
                .fitchRating("Fitch Rating")
                .sandPRating("Sand PRating")
                .orderNumber(10)
                .build();


        // Save
        rating = ratingRepository.save(rating);
        assertNotNull(rating.getRatingId());
        assertEquals(10, (int) rating.getOrderNumber());

        // Update
        rating.setOrderNumber(20);
        rating = ratingRepository.save(rating);
        assertEquals(rating.getOrderNumber(), 20, 20);

        // Find
        List<Rating> listResult = ratingRepository.findAll();
        assertFalse(listResult.isEmpty());

        // Delete
        Integer id = rating.getRatingId();
        ratingRepository.delete(rating);
        Optional<Rating> ratingList = ratingRepository.findById(id);
        assertFalse(ratingList.isPresent());
    }

    @Test
    void testAddRating() {
        // Création d'un objet Rating fictif
        Rating mockRating = new Rating();
        mockRating.setRatingId(1);
        mockRating.setOrderNumber(4);
        //... initialise d'autres attributs

        // Définir le comportement attendu lors de l'appel à save du repository
        when(ratingRepository.save(mockRating)).thenReturn(mockRating);

        // Appeler la méthode à tester
        Rating addedRating = ratingService.add(mockRating);

        // Vérifier que la méthode save du repository a été appelée avec les bons paramètres
        verify(ratingRepository).save(mockRating);

        // Vérifier si le résultat renvoyé par la méthode add est correct
        assertEquals(mockRating.getRatingId(), addedRating.getRatingId());
        //... Vérification d'autres attributs si nécessaire
    }

    @Test
    void testGetAllRatings() {
        // Création de données fictives pour simuler le comportement de findAll du repository
        Rating rating1 = new Rating();
        rating1.setRatingId(1);
        // ... initialise d'autres attributs pour rating1

        Rating rating2 = new Rating();
        rating2.setRatingId(2);
        // ... initialise d'autres attributs pour rating2

        List<Rating> mockRatingList = Arrays.asList(rating1, rating2);

        // Définir le comportement attendu lors de l'appel à findAll du repository
        when(ratingRepository.findAll()).thenReturn(mockRatingList);

        // Appeler la méthode à tester
        List<Rating> retrievedRatings = ratingService.getAll();

        // Vérifier que la méthode findAll du repository a été appelée
        verify(ratingRepository).findAll();

        // Vérifier si les données renvoyées par la méthode getAll correspondent aux données fictives
        assertEquals(mockRatingList.size(), retrievedRatings.size());
        // ... Vérification d'autres attributs si nécessaire
    }


    @Test
    public void testUpdateRating() {
        // Créer un ID fictif pour le test
        Integer ratingId = 1;
        Rating ratingToUpdate = new Rating();
        ratingToUpdate.setMoodysRating("test");
        ratingToUpdate.setSandPRating("test");
        ratingToUpdate.setFitchRating("test");
        ratingToUpdate.setOrderNumber(5);

        // Créer un objet Rating existant dans la base de données pour simuler la recherche
        Rating existingRating = new Rating();
        existingRating.setRatingId(ratingId);
        existingRating.setMoodysRating("test1");
        existingRating.setSandPRating("TEST2");
        existingRating.setFitchRating("Test3");
        existingRating.setOrderNumber(10);

        // Configurer le comportement simulé du repository
        when(ratingRepository.findById(ratingId)).thenReturn(Optional.of(existingRating));
        when(ratingRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Appeler la méthode à tester
        Rating updatedRating = ratingService.update(ratingId, ratingToUpdate);

        // Vérifier que la méthode a renvoyé un Rating mis à jour
        assertNotNull(updatedRating);
        assertEquals(ratingId, updatedRating.getRatingId());
        assertEquals(ratingToUpdate.getMoodysRating(), updatedRating.getMoodysRating());
        assertEquals(ratingToUpdate.getSandPRating(), updatedRating.getSandPRating());
        assertEquals(ratingToUpdate.getFitchRating(), updatedRating.getFitchRating());
        assertEquals(ratingToUpdate.getOrderNumber(), updatedRating.getOrderNumber());

        // Vérifier que la méthode a bien utilisé le repository pour sauvegarder le Rating mis à jour
        verify(ratingRepository, times(1)).findById(ratingId);
        verify(ratingRepository, times(1)).save(existingRating);
    }


    @Test
    void testDeleteRatingById() {
        Integer ratingIdToDelete = 1; // ID à supprimer (adapter selon les besoins)

        // Appel de la méthode à tester
        String result = ratingService.deleteById(ratingIdToDelete);

        // Vérifier que la méthode deleteById du repository a été appelée avec le bon ID
        verify(ratingRepository).deleteById(ratingIdToDelete);

        // Vérifier que la méthode retourne le message attendu
        assertEquals("rating deleted", result);
    }

    @Test
    void testFindRatingById() {
        Integer ratingIdToFind = 1; // ID à rechercher (adapter selon les besoins)

        // Création d'un Rating fictif pour simuler la réponse du repository
        Rating mockRating = new Rating();
        mockRating.setRatingId(ratingIdToFind);

        // Définition du comportement simulé du repository pour findById
        when(ratingRepository.findById(ratingIdToFind)).thenReturn(Optional.of(mockRating));

        // Appel de la méthode à tester
        Rating result = ratingService.findById(ratingIdToFind);

        // Vérifier que la méthode findById du repository a été appelée avec le bon ID
        assertEquals(ratingIdToFind, result.getRatingId());

        // Vérifier le comportement lorsque l'ID n'est pas trouvé
        Integer nonExistentId = 999; // ID qui n'existe pas
        when(ratingRepository.findById(nonExistentId)).thenReturn(Optional.empty());

        // Vérifier que l'appel avec un ID non existant génère une exception
        assertThrows(RuntimeException.class, () -> ratingService.findById(nonExistentId));
    }

    @Test
    void testRatingNotFound() {
        // Définition du comportement simulé du repository pour findById avec n'importe quel ID (anyInt())
        when(ratingRepository.findById(anyInt())).thenReturn(Optional.empty());

        // Vérifier que l'appel avec n'importe quel ID non existant génère une exception
        int nonExistentId = 999; // ID qui n'existe pas
        assertThrows(RuntimeException.class, () -> ratingService.findById(nonExistentId));
    }
}
