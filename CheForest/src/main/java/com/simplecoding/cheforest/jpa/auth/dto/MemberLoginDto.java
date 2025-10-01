package com.simplecoding.cheforest.jpa.auth.dto;

import jakarta.validation.constraints.NotBlank;
import lombok.Data;

@Data
public class MemberLoginDto {
    @NotBlank
    private String loginId;

    @NotBlank
    private String password;
}
