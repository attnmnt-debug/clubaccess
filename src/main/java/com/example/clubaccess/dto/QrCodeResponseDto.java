package com.example.clubaccess.dto;

import java.time.LocalDateTime;
import java.util.UUID;

public record QrCodeResponseDto(
        UUID id,
        UUID memberId,
        String memberFullName,
        boolean active,
        LocalDateTime createdAt
) {
}
