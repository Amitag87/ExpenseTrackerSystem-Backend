package com.spendsmart.auth.service;

import com.spendsmart.auth.client.CategoryClient;
import com.spendsmart.auth.config.JwtUtil;
import com.spendsmart.auth.entity.User;
import com.spendsmart.auth.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Service
public class AuthServiceImpl implements AuthService {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private BCryptPasswordEncoder passwordEncoder;

    @Autowired
    private JwtUtil jwtUtil;

    // ✅ NEW — injected for logout support
    @Autowired
    private TokenBlacklistService tokenBlacklistService;

    @Autowired
    private CategoryClient categoryClient;

    // ── Core Auth ──────────────────────────────────────────────────────────────

    @Override
    public User register(User user) {
        if (userRepository.existsByEmail(user.getEmail())) {
            throw new RuntimeException("Email already registered");
        }
        boolean googleUser = user.getProvider() == User.Provider.GOOGLE;
        if (googleUser) {
            user.setPasswordHash(passwordEncoder.encode(UUID.randomUUID().toString()));
        } else if (user.getPasswordHash() == null || user.getPasswordHash().isBlank()) {
            throw new RuntimeException("Password is required");
        } else {
            user.setPasswordHash(passwordEncoder.encode(user.getPasswordHash()));
        }
        user.setIsActive(true);
        User savedUser = userRepository.save(user);
        try {
            categoryClient.initDefaultCategories(savedUser.getUserId());
            return savedUser;
        } catch (RuntimeException ex) {
            userRepository.deleteById(savedUser.getUserId());
            throw new RuntimeException("User registration failed while creating default categories", ex);
        }
    }

    /**
     * Login now returns a JWT token string (instead of the User object)
     * so the controller can include it directly in AuthResponse.
     */
    @Override
    public String login(String email, String password) {
        User user = userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found"));
        if (!user.getIsActive()) {
            throw new RuntimeException("Account is deactivated");
        }
        if (!passwordEncoder.matches(password, user.getPasswordHash())) {
            throw new RuntimeException("Invalid password");
        }
        return jwtUtil.generateToken(email);
    }

    // ✅ NEW — Logout: blacklist the token
    @Override
    public void logout(String token) {
        if (!jwtUtil.validateToken(token)) {
            throw new RuntimeException("Invalid or expired token");
        }
        tokenBlacklistService.blacklist(token);
    }

    // ✅ NEW — Validate token (also checks blacklist)
    @Override
    public boolean validateToken(String token) {
        return jwtUtil.validateToken(token) && !tokenBlacklistService.isBlacklisted(token);
    }

    // ✅ NEW — Refresh token: validate old, issue new
    @Override
    public String refreshToken(String token) {
        if (!validateToken(token)) {
            throw new RuntimeException("Token is invalid or has been logged out");
        }
        String email = jwtUtil.extractEmail(token);
        // Blacklist old token so it can't be reused
        tokenBlacklistService.blacklist(token);
        return jwtUtil.generateToken(email);
    }

    // ── User Queries ────────────────────────────────────────────────────────────

    @Override
    public User getUserById(Long userId) {
        return userRepository.findByUserId(userId)
                .orElseThrow(() -> new RuntimeException("User not found with id: " + userId));
    }

    @Override
    public User getUserByEmail(String email) {
        return userRepository.findByEmail(email)
                .orElseThrow(() -> new RuntimeException("User not found with email: " + email));
    }

    @Override
    public Optional<User> findOptionalUserByEmail(String email) {
        return userRepository.findByEmail(email);
    }

    @Override
    public List<User> getAllUsers() {
        return userRepository.findAll();
    }

    // ── Profile Management ──────────────────────────────────────────────────────

    @Override
    public User updateProfile(Long userId, User updatedUser) {
        User existing = getUserById(userId);
        if (updatedUser.getFullName() != null)  existing.setFullName(updatedUser.getFullName());
        if (updatedUser.getAvatarUrl() != null) existing.setAvatarUrl(updatedUser.getAvatarUrl());
        if (updatedUser.getBio() != null)       existing.setBio(updatedUser.getBio());
        if (updatedUser.getTimezone() != null)  existing.setTimezone(updatedUser.getTimezone());
        if (updatedUser.getProvider() != null)  existing.setProvider(updatedUser.getProvider());
        return userRepository.save(existing);
    }

    @Override
    public void changePassword(Long userId, String currentPassword, String newPassword) {
        User user = getUserById(userId);
        if (!passwordEncoder.matches(currentPassword, user.getPasswordHash())) {
            throw new RuntimeException("Current password is incorrect");
        }
        user.setPasswordHash(passwordEncoder.encode(newPassword));
        userRepository.save(user);
    }

    @Override
    public void updateCurrency(Long userId, String currency) {
        // Basic ISO 4217 length check
        if (currency == null || currency.length() != 3) {
            throw new RuntimeException("Currency must be a valid 3-letter ISO 4217 code (e.g. INR, USD)");
        }
        User user = getUserById(userId);
        user.setCurrency(currency.toUpperCase());
        userRepository.save(user);
    }

    @Override
    public void updateMonthlyBudget(Long userId, Double monthlyBudget) {
        if (monthlyBudget != null && monthlyBudget < 0) {
            throw new RuntimeException("Monthly budget cannot be negative");
        }
        User user = getUserById(userId);
        user.setMonthlyBudget(monthlyBudget);
        userRepository.save(user);
    }

    @Override
    public void deactivateAccount(Long userId) {
        User user = getUserById(userId);
        user.setIsActive(false);
        userRepository.save(user);
    }
}
