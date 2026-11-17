package com.example.clubaccess.service;

import com.example.clubaccess.dto.EntryResponseDto;
import com.example.clubaccess.entity.Member;
import com.example.clubaccess.entity.QrCode;
import com.example.clubaccess.exception.AccessDeniedException;
import com.example.clubaccess.exception.ResourceNotFoundException;
import com.example.clubaccess.repository.QrCodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Service
public class AccessService {

    private final QrCodeRepository qrCodeRepository;

    public AccessService(QrCodeRepository qrCodeRepository) {
        this.qrCodeRepository = qrCodeRepository;
    }

    @Transactional
    public EntryResponseDto enterByQrCode(UUID qrCodeId) {
        QrCode currentQr = qrCodeRepository.findByIdWithMember(qrCodeId)
                .orElseThrow(() -> new ResourceNotFoundException("Вход запрещён: QR-код не найден в системе"));

        if (!currentQr.isActive()) {
            throw new AccessDeniedException("Вход запрещён: этот одноразовый QR-код уже был использован");
        }

        Member member = currentQr.getMember();
        if (!member.isActive()) {
            throw new AccessDeniedException("Вход запрещён: членство участника приостановлено");
        }

        // 1. Деактивируем старый одноразовый QR-код
        currentQr.setActive(false);
        qrCodeRepository.save(currentQr);

        // 2. Генерируем и сохраняем новый QR-код с новым UUID для следующего визита
        QrCode nextQr = qrCodeRepository.save(new QrCode(member));

        return new EntryResponseDto(
                "Доступ разрешён! Добро пожаловать в клуб.",
                member.getFullName(),
                nextQr.getId()
        );
    }
}
