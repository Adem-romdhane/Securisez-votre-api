package com.nnk.springboot.service;

import com.nnk.springboot.domain.User;
import com.nnk.springboot.repositories.UserRepository;
import com.nnk.springboot.services.UserServiceImpl;
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
public class UserServiceTests {
    @Mock
    UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    private User user;


    @Test
    public void testAddUser() {
        // Création d'un utilisateur fictif pour le test
        User user = new User();
        user.setUsername("username");
        user.setPassword("password");
        user.setFullname("Full Name");

        // Configuration du comportement de userRepository.save pour renvoyer l'utilisateur passé en paramètre
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Appel de la méthode addUser avec l'utilisateur fictif
        User savedUser = userService.add(user);

        // Vérification que la méthode save a été appelée une fois
        verify(userRepository, times(1)).save(any(User.class));

        // Capture des arguments passés à userRepository.save pour vérifier l'utilisateur sauvegardé
        ArgumentCaptor<User> userCaptor = ArgumentCaptor.forClass(User.class);
        verify(userRepository).save(userCaptor.capture());
        User capturedUser = userCaptor.getValue();

        // Vérification que l'utilisateur sauvegardé correspond à l'utilisateur passé à la méthode addUser
        assertEquals(user.getUsername(), capturedUser.getUsername());
        assertEquals(user.getPassword(), capturedUser.getPassword());
        assertEquals(user.getFullname(), capturedUser.getFullname());

        // Vérification que l'utilisateur renvoyé par la méthode addUser est bien celui sauvegardé
        assertEquals(user.getUsername(), savedUser.getUsername());
        assertEquals(user.getPassword(), savedUser.getPassword());
        assertEquals(user.getFullname(), savedUser.getFullname());
    }
}
