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
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class DBUserServiceTests {
    @Mock
    DBUserRepository DBUserRepository;

    @InjectMocks
    private DBUserServiceImpl userService;

    private DBUser user;


    @Test
    public void testAddUser() {
        // Création d'un utilisateur fictif pour le test
        DBUser DBUser = new DBUser();
        DBUser.setUsername("username");
        DBUser.setPassword("Azerty94@");
        DBUser.setFullname("Full Name");

        // Configuration du comportement de userRepository.save pour renvoyer l'utilisateur passé en paramètre
        when(DBUserRepository.save(any(DBUser.class))).thenAnswer(invocation -> invocation.getArgument(0));

        // Création d'un passwordEncoder mock
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        when(passwordEncoder.encode(any(CharSequence.class))).thenAnswer(invocation -> {
            CharSequence arg = invocation.getArgument(0);
            // Vous pouvez définir ici la logique de l'encoder (ceci est un exemple simple)
            return "encoded_" + arg.toString();
        });

        // Injectez le passwordEncoder mock dans votre service avant d'appeler la méthode addUser
        DBUserServiceImpl userService = new DBUserServiceImpl(DBUserRepository, passwordEncoder);
        DBUser savedDBUser = userService.add(DBUser);

        // Vérification que la méthode save a été appelée une fois
        verify(DBUserRepository, times(1)).save(any(DBUser.class));

        // Capture des arguments passés à userRepository.save pour vérifier l'utilisateur sauvegardé
        ArgumentCaptor<DBUser> userCaptor = ArgumentCaptor.forClass(DBUser.class);
        verify(DBUserRepository).save(userCaptor.capture());
        DBUser capturedDBUser = userCaptor.getValue();

        // Vérification que l'utilisateur sauvegardé correspond à l'utilisateur passé à la méthode addUser
        assertEquals(DBUser.getUsername(), capturedDBUser.getUsername());
        assertEquals("encoded_Azerty94@", capturedDBUser.getPassword()); // Vérifie le mot de passe encodé
        assertEquals(DBUser.getFullname(), capturedDBUser.getFullname());

        // Vérification que l'utilisateur renvoyé par la méthode addUser est bien celui sauvegardé
        assertEquals(DBUser.getUsername(), savedDBUser.getUsername());
        assertEquals("encoded_Azerty94@", savedDBUser.getPassword()); // Vérifie le mot de passe encodé
        assertEquals(DBUser.getFullname(), savedDBUser.getFullname());
    }

    @Test
    public void testGetAllUsers() {
        // Création de données fictives pour simuler les utilisateurs
        DBUser user1 = new DBUser();
        user1.setUsername("user1");
        user1.setPassword("password1");
        user1.setFullname("Full Name 1");

        DBUser user2 = new DBUser();
        user2.setUsername("user2");
        user2.setPassword("password2");
        user2.setFullname("Full Name 2");

        List<DBUser> userList = new ArrayList<>();
        userList.add(user1);
        userList.add(user2);

        // Mock du DBUserRepository
        DBUserRepository DBUserRepository = mock(DBUserRepository.class);

        // Initialise le service avec le mock
        DBUserServiceImpl userService = new DBUserServiceImpl(DBUserRepository, mock(PasswordEncoder.class));

        // Configuration du comportement de DBUserRepository.findAll
        when(DBUserRepository.findAll()).thenReturn(userList);

        // Appel de la méthode getAll
        List<DBUser> returnedUsers = userService.getAll();

        // Vérification que DBUserRepository.findAll a été appelé
        verify(DBUserRepository).findAll();

        // Vérification que la liste retournée par la méthode contient les utilisateurs simulés
        assertEquals(2, returnedUsers.size());
        assertEquals("user1", returnedUsers.get(0).getUsername());
        assertEquals("password1", returnedUsers.get(0).getPassword());
        assertEquals("Full Name 1", returnedUsers.get(0).getFullname());
        assertEquals("user2", returnedUsers.get(1).getUsername());
        assertEquals("password2", returnedUsers.get(1).getPassword());
        assertEquals("Full Name 2", returnedUsers.get(1).getFullname());
    }

    @Test
    public void testAddUserPasswordEncodingAndSaving() {
        // Création d'un utilisateur fictif pour le test
        DBUser DBUser = new DBUser();
        DBUser.setUsername("username");
        DBUser.setPassword("Azerty94@");
        DBUser.setFullname("Full Name");

        // Mock pour le PasswordEncoder et le DBUserRepository
        PasswordEncoder passwordEncoder = mock(PasswordEncoder.class);
        DBUserRepository DBUserRepository = mock(DBUserRepository.class);

        // Initialise le service avec les mocks
        DBUserServiceImpl userService = new DBUserServiceImpl(DBUserRepository, passwordEncoder);

        // Configuration du comportement du PasswordEncoder
        when(passwordEncoder.encode(any(CharSequence.class))).thenReturn("encoded_Azerty94@");

        // Appel de la méthode add avec l'utilisateur fictif
        userService.add(DBUser);

        // Vérification que passwordEncoder.encode a été appelé avec le bon mot de passe
        verify(passwordEncoder).encode("Azerty94@");

        // Capture des arguments passés à DBUserRepository.save
        ArgumentCaptor<DBUser> userCaptor = ArgumentCaptor.forClass(DBUser.class);
        verify(DBUserRepository).save(userCaptor.capture());
        DBUser capturedDBUser = userCaptor.getValue();

        // Vérification que l'utilisateur passé à DBUserRepository.save a le bon mot de passe encodé
        assertEquals("encoded_Azerty94@", capturedDBUser.getPassword());
    }




    @Test
    public void testUpdate_UserNotFound() {
        // Création d'un ID fictif pour le test
        Integer userId = 1;

        // Création d'un mock du repository qui retourne Optional.empty() pour simuler un utilisateur non trouvé
        DBUserRepository userRepositoryMock = mock(DBUserRepository.class);
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

        // Création du service en lui passant le mock du repository
        DBUserServiceImpl userService = new DBUserServiceImpl(userRepositoryMock, null);

        // Appel de la méthode update avec un ID inexistant et un utilisateur fictif à mettre à jour
        DBUser updatedUser = new DBUser();
        updatedUser.setId(userId);
        // ... initialisez d'autres attributs pour la mise à jour

        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.update(userId, updatedUser));

        // Vérification qu'une exception est levée avec le bon message
        assertEquals("User non trouvé !", exception.getMessage());

        // Vérification que la méthode findById du repository a été appelée avec le bon ID
        verify(userRepositoryMock, times(1)).findById(userId);

        // Vérification que la méthode save du repository n'a pas été appelée
        verifyNoMoreInteractions(userRepositoryMock);
    }

    @Test
    public void testUpdateUserById() {
        // Créer un utilisateur fictif pour le test
        Integer userId = 1;
        DBUser userToUpdate = new DBUser();
        userToUpdate.setFullname("John Doe");
        userToUpdate.setUsername("johndoe");
        userToUpdate.setPassword("password");

        // Créer un utilisateur existant dans la base de données pour simuler la recherche
        DBUser existingUser = new DBUser();
        existingUser.setId(userId);
        existingUser.setFullname("Jane Smith");
        existingUser.setUsername("janesmith");
        existingUser.setPassword("oldpassword");

        // Configurer le comportement simulé du repository
        when(DBUserRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(DBUserRepository.save(any())).thenAnswer(invocation -> invocation.getArgument(0));

        // Appeler la méthode à tester
        DBUser updatedUser = userService.update(userId, userToUpdate);

        // Vérifier que la méthode a renvoyé un utilisateur mis à jour
        assertNotNull(updatedUser);
        assertEquals(userId, updatedUser.getId());
        assertEquals(userToUpdate.getFullname(), updatedUser.getFullname());
        assertEquals(userToUpdate.getUsername(), updatedUser.getUsername());
        assertEquals(userToUpdate.getPassword(), updatedUser.getPassword());

        // Vérifier que la méthode a bien utilisé le repository pour sauvegarder l'utilisateur mis à jour
        verify(DBUserRepository, times(1)).findById(userId);
        verify(DBUserRepository, times(1)).save(existingUser);
    }


    @Test
    public void testDeleteById() {
        // Création d'un ID fictif pour le test
        Integer id = 1;

        // Création d'un mock du repository
        DBUserRepository dbUserRepositoryMock = mock(DBUserRepository.class);

        // Création du service en lui passant le mock du repository
        DBUserServiceImpl dbUserService = new DBUserServiceImpl(dbUserRepositoryMock, null);

        // Appel de la méthode à tester
        String result = dbUserService.deleteById(id);

        // Vérification que la méthode deleteById du repository a été appelée une fois avec le bon ID
        verify(dbUserRepositoryMock, times(1)).deleteById(eq(id));

        // Vérification du résultat retourné par la méthode
        assertEquals("user deleted", result);
    }

    @Test
    public void testFindById_UserFound() {
        // Création d'un ID fictif pour le test
        Integer userId = 1;

        // Création d'un utilisateur fictif
        DBUser expectedUser = new DBUser();
        expectedUser.setId(userId);
        // ... initialisez d'autres attributs au besoin

        // Création d'un mock du repository
        DBUserRepository userRepositoryMock = mock(DBUserRepository.class);

        // Configuration du comportement du repository pour retourner un utilisateur fictif lorsque findById est appelé
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.of(expectedUser));

        // Création du service en lui passant le mock du repository
        DBUserServiceImpl userService = new DBUserServiceImpl(userRepositoryMock, null);

        // Appel de la méthode findById du service avec l'ID fictif
        DBUser result = userService.findById(userId);

        // Vérification que la méthode du repository a bien été appelée avec l'ID fictif
        verify(userRepositoryMock, times(1)).findById(userId);

        // Vérification que l'utilisateur retourné par la méthode du service correspond à celui attendu
        assertEquals(expectedUser, result);
    }


    @Test
    public void testFindById_UserNotFound() {
        // Création d'un ID fictif pour le test
        Integer userId = 1;

        // Création d'un mock du repository qui retourne Optional.empty() pour simuler un utilisateur non trouvé
        DBUserRepository userRepositoryMock = mock(DBUserRepository.class);
        when(userRepositoryMock.findById(userId)).thenReturn(Optional.empty());

        // Création du service en lui passant le mock du repository
        DBUserServiceImpl userService = new DBUserServiceImpl(userRepositoryMock, null);

        // Appel de la méthode findById du service avec l'ID fictif
        RuntimeException exception = assertThrows(RuntimeException.class, () -> userService.findById(userId));

        // Vérification qu'une exception est levée avec le bon message
        assertEquals("user not founded for id : " + userId, exception.getMessage());

        // Vérification que la méthode du repository a bien été appelée avec l'ID fictif
        verify(userRepositoryMock, times(1)).findById(userId);
    }

}