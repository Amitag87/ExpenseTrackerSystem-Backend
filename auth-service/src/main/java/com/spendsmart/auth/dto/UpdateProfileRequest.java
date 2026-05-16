package com.spendsmart.auth.dto;

import lombok.Data;

@Data
public class UpdateProfileRequest {
    private String fullName;
    private String avatarUrl;
    private String bio;
    private String timezone;
}
