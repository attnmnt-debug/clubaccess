package com.example.clubaccess.controller;

import com.example.clubaccess.dto.EntryResponseDto;
import com.example.clubaccess.service.AccessService;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.UUID;

@RestController
@RequestMapping("/api/access")
public class AccessController {

    private final AccessService accessService;

    public AccessController(AccessService accessService) {
        this.accessService = accessService;
    }

    @PostMapping("/enter/{qrCodeId}")
    public ResponseEntity<EntryResponseDto> enterClub(@PathVariable UUID qrCodeId) {
        return ResponseEntity.ok(accessService.enterByQrCode(qrCodeId));
    }
}
