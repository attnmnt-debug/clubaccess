package com.example.clubaccess.dto;

import java.util.UUID;

public record EntryResponseDto(
        String message,
        String memberFullName,
        UUID nextQrCodeId
) {
}
