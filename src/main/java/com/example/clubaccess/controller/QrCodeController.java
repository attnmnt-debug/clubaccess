package com.example.clubaccess.controller;

import com.example.clubaccess.dto.QrCodeRequestDto;
import com.example.clubaccess.dto.QrCodeResponseDto;
import com.example.clubaccess.service.QrCodeService;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.UUID;

@RestController
@RequestMapping("/api/qr-codes")
public class QrCodeController {

    private final QrCodeService qrCodeService;

    public QrCodeController(QrCodeService qrCodeService) {
        this.qrCodeService = qrCodeService;
    }

    @PostMapping
    public ResponseEntity<QrCodeResponseDto> create(@RequestBody QrCodeRequestDto dto) {
        return ResponseEntity.status(HttpStatus.CREATED).body(qrCodeService.createQrCode(dto));
    }

    @GetMapping
    public ResponseEntity<List<QrCodeResponseDto>> getAll() {
        return ResponseEntity.ok(qrCodeService.getAllQrCodes());
    }

    @GetMapping("/{id}")
    public ResponseEntity<QrCodeResponseDto> getById(@PathVariable UUID id) {
        return ResponseEntity.ok(qrCodeService.getQrCodeById(id));
    }

    @PutMapping("/{id}")
    public ResponseEntity<QrCodeResponseDto> update(@PathVariable UUID id, @RequestBody QrCodeRequestDto dto) {
        return ResponseEntity.ok(qrCodeService.updateQrCode(id, dto));
    }

    @DeleteMapping("/{id}")
    public ResponseEntity<Void> delete(@PathVariable UUID id) {
        qrCodeService.deleteQrCode(id);
        return ResponseEntity.noContent().build();
    }
}
