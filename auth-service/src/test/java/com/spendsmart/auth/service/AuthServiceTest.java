package com.spendsmart.auth.service;

import com.spendsmart.auth.client.CategoryClient;
import com.spendsmart.auth.config.JwtUtil;
import com.spendsmart.auth.entity.User;
import com.spendsmart.auth.repository.UserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import java.util.Optional;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
public class AuthServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private BCryptPasswordEncoder passwordEncoder;

    @Mock
    private JwtUtil jwtUtil;

    @Mock
    private CategoryClient categoryClient;

    @InjectMocks
    private AuthServiceImpl authService;

    private User sampleUser;

    @BeforeEach
    void setUp() {
        sampleUser = new User();
        sampleUser.setUserId(1L);
        sampleUser.setEmail("test@example.com");
        sampleUser.setPasswordHash("encoded_password");
        sampleUser.setIsActive(true);
    }

    @Test
    void register_Success() {
        when(userRepository.existsByEmail(anyString())).thenReturn(false);
        when(passwordEncoder.encode(anyString())).thenReturn("encoded_password");
        when(userRepository.save(any(User.class))).thenReturn(sampleUser);
        
        User result = authService.register(sampleUser);
        
        assertNotNull(result);
        verify(categoryClient, times(1)).initDefaultCategories(1L);
    }

    @Test
    void login_Success() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("password", "encoded_password")).thenReturn(true);
        when(jwtUtil.generateToken("test@example.com")).thenReturn("jwt_token");
        
        String token = authService.login("test@example.com", "password");
        
        assertEquals("jwt_token", token);
    }

    @Test
    void login_InvalidPassword() {
        when(userRepository.findByEmail("test@example.com")).thenReturn(Optional.of(sampleUser));
        when(passwordEncoder.matches("wrong_password", "encoded_password")).thenReturn(false);
        
        assertThrows(RuntimeException.class, () -> authService.login("test@example.com", "wrong_password"));
    }
}
