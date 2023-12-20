package com.nnk.springboot.service;

import com.nnk.springboot.domain.DBUser;
import com.nnk.springboot.repositories.DBUserRepository;
import com.nnk.springboot.services.DBUserServiceImpl;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DBUserServiceTests {
    @Mock
    DBUserRepository DBUserRepository;

    @InjectMocks
    private DBUserServiceImpl userService;

    private DBUser DBUser;


    @Test
    public void testAddUser() {
        // Création d'un utilisateur fictif pour le test
        DBUser DBUser = new DBUser();
        DBUser.setUsername("username");
        DBUser.setPassword("password");
        DBUser.setFullname("Full Name");

        // Configuration du comportement de userRepository.save pour renvoyer l'utilisateur passé en paramètre
        when(DBUserRepository.save(any(DBUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Appel de la méthode addUser avec l'utilisateur fictif
        DBUser savedDBUser = userService.add(DBUser);

        // Vérification que la méthode save a été appelée une fois
        verify(DBUserRepository, times(1)).save(any(DBUser.class));

        // Capture des arguments passés à userRepository.save pour vérifier l'utilisateur sauvegardé
        ArgumentCaptor<DBUser> userCaptor = ArgumentCaptor.forClass(DBUser.class);
        verify(DBUserRepository).save(userCaptor.capture());
        DBUser capturedDBUser = userCaptor.getValue();

        // Vérification que l'utilisateur sauvegardé correspond à l'utilisateur passé à la méthode addUser
        assertEquals(DBUser.getUsername(), capturedDBUser.getUsername());
        assertEquals(DBUser.getPassword(), capturedDBUser.getPassword());
        assertEquals(DBUser.getFullname(), capturedDBUser.getFullname());

        // Vérification que l'utilisateur renvoyé par la méthode addUser est bien celui sauvegardé
        assertEquals(DBUser.getUsername(), savedDBUser.getUsername());
        assertEquals(DBUser.getPassword(), savedDBUser.getPassword());
        assertEquals(DBUser.getFullname(), savedDBUser.getFullname());
    }
}
