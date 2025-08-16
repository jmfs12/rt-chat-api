package com.jmfs.api.service;

import com.jmfs.api.domain.User;
import com.jmfs.api.dto.AuthResponseDTO;
import com.jmfs.api.dto.LoginRequestDTO;
import com.jmfs.api.dto.RegisterRequestDTO;
import com.jmfs.api.repositories.UserRepository;
import com.jmfs.api.service.impl.AuthServiceImpl;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.util.ArrayList;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.mockito.Mockito.*;

class AuthServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private TokenService tokenService;
    @Mock
    private PasswordEncoder passwordEncoder;

    @InjectMocks
    private AuthServiceImpl authService;

    @BeforeEach
    void setup(){
        MockitoAnnotations.openMocks(this);
    }

    @Test
    @DisplayName("Should login user successfully when everything is ok")
    void loginCase1() {

        Optional<User> user = Optional.of(new User(1L, "jmfs", "encodedPassword", "joanomiguel@gmail.com",
                new ArrayList<>(), new ArrayList<>()));

        when(userRepository.findByUsername("jmfs")).thenReturn(user);
        when(passwordEncoder.matches("jmfs", "encodedPassword")).thenReturn(true);
        when(passwordEncoder.encode("jmfs")).thenReturn("encodedPassword");
        when(tokenService.generateToken(user.get())).thenReturn("token");

        AuthResponseDTO loggedUser = authService.login(new LoginRequestDTO("jmfs", "jmfs"));
        assertThat(loggedUser.token()).isEqualTo("token");
        assertThat(loggedUser.name()).isEqualTo("jmfs");
        assertThat(loggedUser.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should throw Exception when the password is wrong")
    void loginCase2() {

        Optional<User> user = Optional.of(new User(1L, "jmfs", "encodedPassword", "joanomiguel@gmail.com",
                new ArrayList<>(), new ArrayList<>()));

        when(userRepository.findByUsername("jmfs")).thenReturn(user);
        when(passwordEncoder.encode("jmfs")).thenReturn("encodedPassword");
        when(passwordEncoder.matches("jmfs", "encodedPassword")).thenReturn(false);

        Exception thrown = Assertions.assertThrows(RuntimeException.class,
                () -> authService.login(new LoginRequestDTO("jmfs", "jmfs")));

        Assertions.assertEquals("Invalid credentials", thrown.getMessage());
    }

    @Test
    @DisplayName("Should throw Exception when user not exists in database")
    void loginCase3() {
        when(userRepository.findByUsername("jmfs")).thenReturn(Optional.empty());
        Exception thrown = Assertions.assertThrows(RuntimeException.class, () -> authService.login(new LoginRequestDTO("jmfs", "jmfs")));

        Assertions.assertEquals("User not found", thrown.getMessage());
    }

    @Test
    @DisplayName("Should register user successfully when everything is ok")
    void registerCase1() {
        RegisterRequestDTO registerRequestDTO = new RegisterRequestDTO(
                "jmfs", "password", "jmfs@gmail.com"
        );

        when(userRepository.findByUsername(registerRequestDTO.username())).thenReturn(Optional.empty());
        when(userRepository.findByEmail(registerRequestDTO.email())).thenReturn(Optional.empty());

        Optional<User> user = Optional.of(new User(1L, registerRequestDTO.username()
                , registerRequestDTO.password(), registerRequestDTO.email(),
                new ArrayList<>(), new ArrayList<>())
        );

        when(passwordEncoder.encode("password")).thenReturn("encodedPassword");
        when(userRepository.save(any(User.class))).thenAnswer(invocation -> {
            User u = invocation.getArgument(0);
            u.setId(1L); // simulate DB assigning ID
            return u;
        });
        when(tokenService.generateToken(any(User.class))).thenReturn("token");

        AuthResponseDTO response = authService.register(registerRequestDTO);

        verify(userRepository, times(1)).save(any());

        assertThat(response.token()).isEqualTo("token");
        assertThat(response.name()).isEqualTo("jmfs");
        assertThat(response.id()).isEqualTo(1L);
    }

    @Test
    @DisplayName("Should throw Exception when a user with username already exists in database")
    void registerCase2() {
        Optional<User> user = Optional.of(new User());
        when(userRepository.findByUsername("jmfs")).thenReturn(user);

        Exception thrown = Assertions.assertThrows(RuntimeException.class, () ->
            authService.register(new RegisterRequestDTO(
                    "jmfs", "password", "jmfs@gmail.com"
            ))
        );
        Assertions.assertEquals("Username already exists", thrown.getMessage());
    }

    @Test
    @DisplayName("Should throw Exception when a user with email already exists in database")
    void registerCase3() {
        Optional<User> user = Optional.of(new User());
        when(userRepository.findByUsername("jmfs")).thenReturn(Optional.empty());
        when(userRepository.findByEmail("jmfs@gmail.com")).thenReturn(user);

        Exception thrown = Assertions.assertThrows(RuntimeException.class, () ->
                authService.register(new RegisterRequestDTO(
                        "jmfs", "password", "jmfs@gmail.com"
                ))
        );
        Assertions.assertEquals("Email already in use", thrown.getMessage());
    }
}