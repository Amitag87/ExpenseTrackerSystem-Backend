package com.spendsmart.auth.resource;

import com.spendsmart.auth.config.JwtUtil;
import com.spendsmart.auth.dto.*;
import com.spendsmart.auth.entity.User;
import com.spendsmart.auth.service.AuthService;
import com.spendsmart.auth.service.GoogleOAuthService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

/**
 * AuthResource — REST controller
 *
 * Endpoints (requirement-aligned):
 *   POST   /auth/register
 *   POST   /auth/login
 *   POST   /auth/login/google         (NEW - Google OAuth)
 *   POST   /auth/logout
 *   POST   /auth/refresh
 *   GET    /auth/profile/{id}
 *   PUT    /auth/profile/{id}
 *   PUT    /auth/password/{id}
 *   PUT    /auth/currency/{id}
 *   PUT    /auth/budget/{id}
 *   DELETE /auth/deactivate/{id}
 *   GET    /auth/users            (admin helper)
 */
@RestController
@RequestMapping("/api/auth")
public class AuthResource {

    @Autowired
    private AuthService authService;

    @Autowired
    private JwtUtil jwtUtil;

    @Autowired
    private GoogleOAuthService googleOAuthService;

    @GetMapping("/google/config")
    public ResponseEntity<GoogleOAuthConfigResponse> googleOAuthConfig() {
        return ResponseEntity.ok(new GoogleOAuthConfigResponse(googleOAuthService.getClientId()));
    }

    // ── Register ────────────────────────────────────────────────
    @PostMapping("/register")
    public ResponseEntity<AuthResponse> register(@RequestBody RegisterRequest req) {
        User user = User.builder()
                .fullName(req.getFullName())
                .email(req.getEmail())
                .passwordHash(req.getPassword())
                .build();
        User saved = authService.register(user);
        String token = jwtUtil.generateToken(saved.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, saved.getUserId(), saved.getEmail(), saved.getFullName()));
    }

    // ── Login ────────────────────────────────────────────────────
    @PostMapping("/login")
    public ResponseEntity<AuthResponse> login(@RequestBody LoginRequest req) {
        String token = authService.login(req.getEmail(), req.getPassword());
        User user = authService.getUserByEmail(req.getEmail());
        return ResponseEntity.ok(new AuthResponse(token, user.getUserId(), user.getEmail(), user.getFullName()));
    }

    // ✅ NEW — Google OAuth Login
    @PostMapping("/login/google")
    public ResponseEntity<?> loginWithGoogle(@RequestBody GoogleLoginRequest req) {
        try {
            // Exchange authorization code for token
            GoogleTokenResponse googleToken = googleOAuthService.exchangeCodeForToken(
                req.getCode(),
                req.getCodeVerifier(),
                req.getRedirectUri()
            );

            // Check if user exists
            User user = authService.findOptionalUserByEmail(googleToken.getEmail()).orElse(null);
            
            if (user == null) {
                // Create new user with GOOGLE provider
                user = User.builder()
                        .email(googleToken.getEmail())
                        .fullName(googleToken.getFullName())
                        .avatarUrl(googleToken.getPicture())
                        .provider(User.Provider.GOOGLE)
                        .isActive(true)
                        .build();
                user = authService.register(user);
            } else if (user.getProvider() == null || user.getProvider() == User.Provider.LOCAL) {
                // Update existing user to also support Google login
                user.setProvider(User.Provider.GOOGLE);
                user.setAvatarUrl(googleToken.getPicture());
                user = authService.updateProfile(user.getUserId(), user);
            }

            // Generate JWT token
            String token = jwtUtil.generateToken(user.getEmail());

            return ResponseEntity.ok(new AuthResponse(token, user.getUserId(), user.getEmail(), user.getFullName()));

        } catch (Exception e) {
            return ResponseEntity.badRequest().body(Map.of("message", e.getMessage()));
        }
    }

    // ✅ NEW — Logout
    @PostMapping("/logout")
    public ResponseEntity<Map<String, String>> logout(@RequestHeader("Authorization") String authHeader) {
        String token = extractToken(authHeader);
        authService.logout(token);
        return ResponseEntity.ok(Map.of("message", "Logged out successfully"));
    }

    // ✅ NEW — Refresh Token
    @PostMapping("/refresh")
    public ResponseEntity<Map<String, String>> refresh(@RequestHeader("Authorization") String authHeader) {
        String oldToken = extractToken(authHeader);
        String newToken = authService.refreshToken(oldToken);
        return ResponseEntity.ok(Map.of("token", newToken));
    }

    // ── Profile GET ──────────────────────────────────────────────
    @GetMapping("/profile/{id}")
    public ResponseEntity<UserProfileResponse> getProfile(@PathVariable Long id) {
        return ResponseEntity.ok(toUserProfileResponse(authService.getUserById(id)));
    }

    // ── Profile PUT ──────────────────────────────────────────────
    @PutMapping("/profile/{id}")
    public ResponseEntity<UserProfileResponse> updateProfile(@PathVariable Long id,
                                                             @RequestBody UpdateProfileRequest req) {
        User patch = new User();
        patch.setFullName(req.getFullName());
        patch.setAvatarUrl(req.getAvatarUrl());
        patch.setBio(req.getBio());
        patch.setTimezone(req.getTimezone());
        return ResponseEntity.ok(toUserProfileResponse(authService.updateProfile(id, patch)));
    }

    // ✅ NEW — Change Password (was missing proper endpoint)
    @PutMapping("/password/{id}")
    public ResponseEntity<Map<String, String>> changePassword(@PathVariable Long id,
                                                               @RequestBody ChangePasswordRequest req) {
        authService.changePassword(id, req.getCurrentPassword(), req.getNewPassword());
        return ResponseEntity.ok(Map.of("message", "Password changed successfully"));
    }

    // ── Currency ─────────────────────────────────────────────────
    @PutMapping("/currency/{id}")
    public ResponseEntity<Map<String, String>> updateCurrency(@PathVariable Long id,
                                                               @RequestParam String currency) {
        authService.updateCurrency(id, currency);
        return ResponseEntity.ok(Map.of("message", "Currency updated to " + currency.toUpperCase()));
    }

    // ── Monthly Budget ────────────────────────────────────────────
    @PutMapping("/budget/{id}")
    public ResponseEntity<Map<String, String>> updateBudget(@PathVariable Long id,
                                                             @RequestParam Double budget) {
        authService.updateMonthlyBudget(id, budget);
        return ResponseEntity.ok(Map.of("message", "Monthly budget updated"));
    }

    // ✅ NEW — Deactivate Account (was DELETE /user/{id}, now proper path)
    @DeleteMapping("/deactivate/{id}")
    public ResponseEntity<Map<String, String>> deactivate(@PathVariable Long id) {
        authService.deactivateAccount(id);
        return ResponseEntity.ok(Map.of("message", "Account deactivated successfully"));
    }

    // ── Admin: all users ──────────────────────────────────────────
    @GetMapping("/users")
    public ResponseEntity<List<UserProfileResponse>> getAllUsers() {
        return ResponseEntity.ok(
                authService.getAllUsers().stream()
                        .map(this::toUserProfileResponse)
                        .collect(Collectors.toList())
        );
    }

    // ── Helper ───────────────────────────────────────────────────
    private String extractToken(String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            throw new RuntimeException("Authorization header missing or malformed");
        }
        return authHeader.substring(7);
    }

    private UserProfileResponse toUserProfileResponse(User user) {
        return UserProfileResponse.builder()
                .userId(user.getUserId())
                .fullName(user.getFullName())
                .email(user.getEmail())
                .currency(user.getCurrency())
                .timezone(user.getTimezone())
                .avatarUrl(user.getAvatarUrl())
                .monthlyBudget(user.getMonthlyBudget())
                .bio(user.getBio())
                .isActive(user.getIsActive())
                .build();
    }
}
