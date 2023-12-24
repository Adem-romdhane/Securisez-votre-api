package com.nnk.springboot;

import com.nnk.springboot.domain.BidList;
import com.nnk.springboot.domain.RuleName;
import com.nnk.springboot.repositories.RuleNameRepository;

import com.nnk.springboot.services.RuleNameServiceImpl;
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
public class RuleTests {

    @Mock
    private RuleNameRepository ruleNameRepository;

    @InjectMocks
    private RuleNameServiceImpl ruleNameService;


    @Test
    public void ruleTest() {
        RuleName rule =
                RuleName.builder()
                        .name("Rule Name")
                        .description("Description")
                        .json("Json")
                        .template("Template")
                        .sqlStr("SQL")
                        .sqlPart("SQL Part")
                        .build();


        // Save
        rule = ruleNameRepository.save(rule);
        assertNotNull(rule.getId());
        assertEquals("Rule Name", rule.getName());

        // Update
        rule.setName("Rule Name Update");
        rule = ruleNameRepository.save(rule);
        assertEquals("Rule Name Update", rule.getName());

        // Find
        List<RuleName> listResult = ruleNameRepository.findAll();
        assertFalse(listResult.isEmpty());

        // Delete
        Integer id = rule.getId();
        ruleNameRepository.delete(rule);
        Optional<RuleName> ruleList = ruleNameRepository.findById(id);
        assertFalse(ruleList.isPresent());
    }


    @Test
    void testAddRuleName() {
        // Créer un objet RuleName fictif pour le test
        RuleName ruleName = new RuleName();

        // Définir le comportement simulé du repository lors de l'ajout
        when(ruleNameRepository.save(ruleName)).thenReturn(ruleName);

        // Appeler la méthode add du service
        RuleName addedRuleName = ruleNameService.add(ruleName);

        // Vérifier si l'objet retourné n'est pas null
        assertNotNull(addedRuleName);
        // Vous pouvez ajouter d'autres assertions en fonction de votre logique métier
    }


    @Test
    void testGetAllRuleNames() {
        // Créer une liste fictive de RuleName pour le test
        RuleName ruleName1 = new RuleName();
        RuleName ruleName2 = new RuleName();
        List<RuleName> ruleNames = Arrays.asList(ruleName1, ruleName2);

        // Définir le comportement simulé du repository lors de la récupération de tous les RuleName
        when(ruleNameRepository.findAll()).thenReturn(ruleNames);

        // Appeler la méthode getAll du service
        List<RuleName> retrievedRuleNames = ruleNameService.getAll();

        // Vérifier si la liste retournée correspond à la liste simulée
        assertEquals(ruleNames.size(), retrievedRuleNames.size());
        // Vous pouvez ajouter d'autres assertions pour vérifier la correspondance des éléments
    }

    @Test
    void testDeleteRuleNameById() {
        Integer ruleNameIdToDelete = 1; // ID à supprimer

        // Appeler la méthode deleteById du service
        ruleNameService.deleteById(ruleNameIdToDelete);

        // Vérifier si la méthode deleteById du repository a été appelée avec le bon ID
        verify(ruleNameRepository).deleteById(ruleNameIdToDelete);
    }

    @Test
    void testFindRuleNameByIdWhenFound() {
        Integer existingRuleNameId = 1; // ID existant
        RuleName existingRuleName = new RuleName(); // Créer une instance RuleName fictive
        when(ruleNameRepository.findById(existingRuleNameId)).thenReturn(Optional.of(existingRuleName));

        RuleName foundRuleName = ruleNameService.findById(existingRuleNameId);

        assertNotNull(foundRuleName);
        assertEquals(existingRuleName, foundRuleName);

        verify(ruleNameRepository, times(1)).findById(existingRuleNameId);
    }

    @Test
    void testFindRuleNameByIdWhenNotFound() {
        Integer nonExistingRuleNameId = 2; // ID non existant
        when(ruleNameRepository.findById(nonExistingRuleNameId)).thenReturn(Optional.empty());

        assertThrows(RuntimeException.class, () -> {
            ruleNameService.findById(nonExistingRuleNameId);
        });

        verify(ruleNameRepository, times(1)).findById(nonExistingRuleNameId);
    }
}
