package com.nnk.springboot;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.CurvePoint;
import com.nnk.springboot.repositories.BidListRepository;
import com.nnk.springboot.repositories.CurvePointRepository;

import com.nnk.springboot.services.BidListServiceImpl;
import com.nnk.springboot.services.CurvePointServiceImpl;
import org.junit.jupiter.api.Test;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


@SpringBootTest
public class CurvePointTests {

	@Mock
	private CurvePointRepository curvePointRepository;

	@InjectMocks
	private CurvePointServiceImpl curvePointService;


	@Test
	public void curvePointTest() {
		CurvePoint curvePoint = CurvePoint.builder()
				.curveId(10)
				.term(10d)
				.value(30d)
				.build();


		// Save
		curvePoint = curvePointRepository.save(curvePoint);
		assertNotNull(curvePoint.getId());
        assertEquals(10, (int) curvePoint.getCurveId());

		// Update
		curvePoint.setCurveId(20);
		curvePoint = curvePointRepository.save(curvePoint);
        assertEquals(20, (int) curvePoint.getCurveId());

		// Find
		List<CurvePoint> listResult = curvePointRepository.findAll();
		assertFalse(listResult.isEmpty());

		// Delete
		Integer id = curvePoint.getId();
		curvePointRepository.delete(curvePoint);
		Optional<CurvePoint> curvePointList = curvePointRepository.findById(id);
		assertFalse(curvePointList.isPresent());
	}

	@Test
	public void testAddCurvePoint(){
		CurvePoint curvePoint = CurvePoint.builder()
				.term(10.0)
				.value(10.0)
				.build();
		
		CurvePointRepository curvePointRepositoryMock = mock(CurvePointRepository.class);
		when(curvePointRepositoryMock.save(any(CurvePoint.class))).thenAnswer(invocation -> invocation.getArgument(0));
		CurvePointServiceImpl curvePointService = new CurvePointServiceImpl(curvePointRepositoryMock);
		CurvePoint saveCurvePoint = curvePointService.add(curvePoint);
		
		verify(curvePointRepositoryMock, times(1)).save(any(CurvePoint.class));
		ArgumentCaptor<CurvePoint> curvePointArgumentCaptor = ArgumentCaptor.forClass(CurvePoint.class);
		verify(curvePointRepositoryMock).save(curvePointArgumentCaptor.capture());
		CurvePoint capturedCurvePoint = curvePointArgumentCaptor.getValue();
		
		assertEquals(curvePoint.getTerm(),capturedCurvePoint.getTerm());
		assertEquals(curvePoint.getValue(),capturedCurvePoint.getValue());
		assertEquals(capturedCurvePoint, saveCurvePoint);
	}


	@Test
	public void testGetAllCurvePoints() {
		// Création de données fictives pour simuler le comportement de findAll
		CurvePoint curvePoint1 = new CurvePoint(/* initialisation */);
		CurvePoint curvePoint2 = new CurvePoint(/* initialisation */);
		List<CurvePoint> curvePointList = new ArrayList<>();
		curvePointList.add(curvePoint1);
		curvePointList.add(curvePoint2);

		// Création d'un mock du repository
		CurvePointRepository curvePointRepositoryMock = mock(CurvePointRepository.class);

		// Création du service en lui passant le mock du repository
		CurvePointServiceImpl curvePointService = new CurvePointServiceImpl(curvePointRepositoryMock);

		// Définition du comportement attendu lors de l'appel à findAll() du mock
		when(curvePointRepositoryMock.findAll()).thenReturn(curvePointList);

		// Appel de la méthode à tester
		List<CurvePoint> returnedCurvePoints = curvePointService.getAll();

		// Vérification que la méthode findAll du repository a été appelée une fois
		verify(curvePointRepositoryMock, times(1)).findAll();

		// Vérification de la correspondance entre la liste retournée et celle simulée
		assertEquals(curvePointList.size(), returnedCurvePoints.size());
		assertEquals(curvePointList.get(0), returnedCurvePoints.get(0));
		assertEquals(curvePointList.get(1), returnedCurvePoints.get(1));
	}

	@Test
	public void testUpdateCurvePoint() {
		// Créer un ID fictif pour le test
		Integer curvePointId = 1;
		CurvePoint curvePointToUpdate = new CurvePoint();
		curvePointToUpdate.setCurveId(100);
		curvePointToUpdate.setTerm(5.0);
		curvePointToUpdate.setValue(50.0);

		// Créer un objet CurvePoint existant dans la base de données pour simuler la recherche
		CurvePoint existingCurvePoint = new CurvePoint();
		existingCurvePoint.setId(curvePointId);
		existingCurvePoint.setCurveId(200);
		existingCurvePoint.setTerm(10.0);
		existingCurvePoint.setValue(25.0);

		// Configurer le comportement simulé du repository
		when(curvePointRepository.findById(curvePointId)).thenReturn(Optional.of(existingCurvePoint));
		when(curvePointRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

		// Appeler la méthode à tester
		CurvePoint updatedCurvePoint = curvePointService.update(curvePointId, curvePointToUpdate);

		// Vérifier que la méthode a renvoyé un CurvePoint mis à jour
		assertNotNull(updatedCurvePoint);
		assertEquals(curvePointId, updatedCurvePoint.getId());
		assertEquals(curvePointToUpdate.getCurveId(), updatedCurvePoint.getCurveId());
		assertEquals(curvePointToUpdate.getTerm(), updatedCurvePoint.getTerm());
		assertEquals(curvePointToUpdate.getValue(), updatedCurvePoint.getValue());

		// Vérifier que la méthode a bien utilisé le repository pour sauvegarder le CurvePoint mis à jour
		verify(curvePointRepository, times(1)).findById(curvePointId);
		verify(curvePointRepository, times(1)).save(existingCurvePoint);
	}
	@Test
	public void testDeleteById() {
		// Création d'un ID fictif pour le test
		Integer curveId = 1;

		// Création d'un mock du repository
		CurvePointRepository curvePointRepositoryMock = mock(CurvePointRepository.class);

		// Création du service en lui passant le mock du repository
		CurvePointServiceImpl curvePointService = new CurvePointServiceImpl(curvePointRepositoryMock);

		// Appel de la méthode à tester
		String result = curvePointService.deleteById(curveId);

		// Vérification que la méthode deleteById du repository a été appelée une fois avec le bon ID
		verify(curvePointRepositoryMock, times(1)).deleteById(eq(curveId));

		// Vérification du résultat retourné par la méthode
		assertEquals("curve point deleted", result);
	}

	@Test
	public void testFindById() {
		// Création d'un ID fictif pour le test
		Integer curveId = 1;

		// Création d'un objet CurvePoint fictif pour simuler le résultat de findById
		CurvePoint curvePoint = new CurvePoint(/* initialisation */);
		Optional<CurvePoint> optionalCurvePoint = Optional.of(curvePoint);

		// Création d'un mock du repository
		CurvePointRepository curvePointRepositoryMock = mock(CurvePointRepository.class);

		// Création du service en lui passant le mock du repository
		CurvePointServiceImpl curvePointService = new CurvePointServiceImpl(curvePointRepositoryMock);

		// Définition du comportement attendu lors de l'appel à findById du mock
		when(curvePointRepositoryMock.findById(curveId)).thenReturn(optionalCurvePoint);

		// Appel de la méthode à tester
		CurvePoint returnedCurvePoint = curvePointService.findById(curveId);

		// Vérification que la méthode findById du repository a été appelée une fois avec le bon ID
		verify(curvePointRepositoryMock, times(1)).findById(eq(curveId));

		// Vérification de la correspondance entre l'objet retourné et celui simulé
		assertEquals(curvePoint, returnedCurvePoint);
	}

}
