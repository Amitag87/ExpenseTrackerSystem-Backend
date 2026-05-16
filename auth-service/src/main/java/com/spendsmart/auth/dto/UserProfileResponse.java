package com.spendsmart.auth.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserProfileResponse {
    private Long userId;
    private String fullName;
    private String email;
    private String currency;
    private String timezone;
    private String avatarUrl;
    private Double monthlyBudget;
    private String bio;
    private Boolean isActive;
}
