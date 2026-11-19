package com.example.clubaccess.service;

import com.example.clubaccess.dto.QrCodeRequestDto;
import com.example.clubaccess.dto.QrCodeResponseDto;
import com.example.clubaccess.entity.Member;
import com.example.clubaccess.entity.QrCode;
import com.example.clubaccess.exception.ResourceNotFoundException;
import com.example.clubaccess.repository.QrCodeRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class QrCodeService {

    private final QrCodeRepository qrCodeRepository;
    private final MemberService memberService;

    public QrCodeService(QrCodeRepository qrCodeRepository, MemberService memberService) {
        this.qrCodeRepository = qrCodeRepository;
        this.memberService = memberService;
    }

    @Transactional
    public QrCodeResponseDto createQrCode(QrCodeRequestDto dto) {
        if (dto.memberId() == null) {
            throw new IllegalArgumentException("Необходимо указать memberId участника");
        }
        Member member = memberService.findEntityById(dto.memberId());
        boolean isNewActive = dto.active() != null ? dto.active() : true;

        if (isNewActive) {
            List<QrCode> activeCodes = qrCodeRepository.findAllByMemberIdAndActiveTrue(member.getId());
            for (QrCode oldCode : activeCodes) {
                oldCode.setActive(false);
            }
            qrCodeRepository.saveAll(activeCodes);
        }

        QrCode qrCode = new QrCode(member);
        qrCode.setActive(isNewActive);
        return toDto(qrCodeRepository.save(qrCode));
    }

    @Transactional(readOnly = true)
    public List<QrCodeResponseDto> getAllQrCodes() {
        return qrCodeRepository.findAllWithMember().stream()
                .map(this::toDto)
                .toList();
    }


    @Transactional(readOnly = true)
    public QrCodeResponseDto getQrCodeById(UUID id) {
        QrCode qrCode = qrCodeRepository.findByIdWithMember(id)
                .orElseThrow(() -> new ResourceNotFoundException("QR-код " + id + " не найден"));
        return toDto(qrCode);
    }

    @Transactional
    public QrCodeResponseDto updateQrCode(UUID id, QrCodeRequestDto dto) {
        QrCode qrCode = qrCodeRepository.findByIdWithMember(id)
                .orElseThrow(() -> new ResourceNotFoundException("QR-код " + id + " не найден"));

        if (dto.memberId() != null && !dto.memberId().equals(qrCode.getMember().getId())) {
            throw new IllegalArgumentException("Нельзя изменить владельца уже созданного QR-кода");
        }

        if (dto.active() != null) {
            if (!qrCode.isActive() && dto.active()) {
                throw new IllegalArgumentException("Нельзя повторно активировать уже использованный QR-код");
            }
            qrCode.setActive(dto.active());
        }

        return toDto(qrCodeRepository.save(qrCode));
    }

    @Transactional
    public void deleteQrCode(UUID id) {
        if (!qrCodeRepository.existsById(id)) {
            throw new ResourceNotFoundException("QR-код " + id + " не найден");
        }
        qrCodeRepository.deleteById(id);
    }

    private QrCodeResponseDto toDto(QrCode qrCode) {
        Member member = qrCode.getMember();
        return new QrCodeResponseDto(
                qrCode.getId(),
                member.getId(),
                member.getFullName(),
                qrCode.isActive(),
                qrCode.getCreatedAt()
        );
    }
}
