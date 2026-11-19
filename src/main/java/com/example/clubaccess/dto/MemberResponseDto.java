package com.example.clubaccess.dto;

import java.util.UUID;

public record MemberResponseDto(
        UUID id,
        String fullName,
        boolean active
) {
}
