package com.example.clubaccess.dto;

import java.util.UUID;

public record QrCodeRequestDto(
        UUID memberId,
        Boolean active
) {
}
