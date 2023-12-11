package org.gg.service;

import org.gg.model.User;
import org.gg.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import java.util.Optional;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
import static org.mockito.Mockito.when;

/*
 * Unit test class for the restaurant service class covering all CRUD functionality.
 */

    public class UserServiceImplTest {

    @Mock
    private UserRepository userRepository;

    @InjectMocks
    private UserServiceImpl userService;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.initMocks(this);
    }

    @Test
    void testAddUser() {
        User user = new User();
        when(userRepository.save(user)).thenReturn(user);

        User result = userService.addUser(user);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).save(user);
    }

    @Test
    void testGetUserById() {
        String userId = "123";
        User user = new User();
        when(userRepository.findById(userId)).thenReturn(Optional.of(user));

        Optional<User> result = userService.getUserById(userId);

        assertTrue(result.isPresent());
        assertEquals(user, result.get());
        verify(userRepository, times(1)).findById(userId);
    }

    @Test
    void testGetUserByEmail() {
        String email = "test@example.com";
        User user = new User();
        when(userRepository.getUsersByEmail(email)).thenReturn(user);

        User result = userService.getUserByEmail(email);

        assertNotNull(result);
        assertEquals(user, result);
        verify(userRepository, times(1)).getUsersByEmail(email);
    }

    @Test
    void testUpdateUser() {
        String userId = "123";
        User existingUser = new User();
        existingUser.setLastName("LastName");
        User updatedUser = new User();
        updatedUser.setLastName("AnotherName");

        when(userRepository.findById(userId)).thenReturn(Optional.of(existingUser));
        when(userRepository.save(existingUser)).thenReturn(existingUser);

        User result = userService.updateUser(userId, updatedUser);

        assertNotNull(result);
        assertEquals(existingUser, result);
        verify(userRepository, times(1)).findById(userId);
        verify(userRepository, times(1)).save(existingUser);
    }

    @Test
    void testRemoveUser() {
        String userId = "123";

        userService.removeUser(userId);

        verify(userRepository, times(1)).deleteById(userId);
    }

    @Test
    void testExists() {
        String email = "test@example.com";
        when(userRepository.getUsersByEmail(email)).thenReturn(new User());

        assertTrue(userService.exists(email));
        verify(userRepository, times(1)).getUsersByEmail(email);
    }
}


