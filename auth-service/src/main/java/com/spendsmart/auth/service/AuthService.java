package com.spendsmart.auth.service;

import com.spendsmart.auth.entity.User;
import java.util.List;
import java.util.Optional;

public interface AuthService {

    // ── Core Auth ──────────────────────────────────────────────
    User register(User user);

    String login(String email, String password); // returns JWT token

    void logout(String token); // ✅ NEW — blacklists the token

    boolean validateToken(String token); // ✅ NEW

    String refreshToken(String token); // ✅ NEW

    // ── User Queries ───────────────────────────────────────────
    User getUserById(Long userId);

    User getUserByEmail(String email);

    Optional<User> findOptionalUserByEmail(String email);

    List<User> getAllUsers();

    // ── Profile Management ─────────────────────────────────────
    User updateProfile(Long userId, User updatedUser);

    void changePassword(Long userId, String currentPassword, String newPassword);

    void updateCurrency(Long userId, String currency);

    void updateMonthlyBudget(Long userId, Double monthlyBudget);

    void deactivateAccount(Long userId);
}
