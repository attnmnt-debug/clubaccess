package com.example.clubaccess.service;

import com.example.clubaccess.dto.MemberRequestDto;
import com.example.clubaccess.dto.MemberResponseDto;
import com.example.clubaccess.entity.Member;
import com.example.clubaccess.exception.ResourceNotFoundException;
import com.example.clubaccess.repository.MemberRepository;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Service
public class MemberService {

    private final MemberRepository memberRepository;

    public MemberService(MemberRepository memberRepository) {
        this.memberRepository = memberRepository;
    }

    @Transactional
    public MemberResponseDto createMember(MemberRequestDto dto) {
        if (dto.fullName() == null || dto.fullName().isBlank()) {
            throw new IllegalArgumentException("ФИО участника не может быть пустым");
        }
        boolean isActive = dto.active() != null ? dto.active() : true;
        Member member = new Member(dto.fullName().trim(), isActive);
        Member saved = memberRepository.save(member);
        return toDto(saved);
    }

    @Transactional(readOnly = true)
    public List<MemberResponseDto> getAllMembers() {
        return memberRepository.findAll().stream()
                .map(this::toDto)
                .toList();
    }

    @Transactional(readOnly = true)
    public MemberResponseDto getMemberById(UUID id) {
        Member member = findEntityById(id);
        return toDto(member);
    }

    @Transactional
    public MemberResponseDto updateMember(UUID id, MemberRequestDto dto) {
        Member member = findEntityById(id);
        if (dto.fullName() != null && !dto.fullName().isBlank()) {
            member.setFullName(dto.fullName().trim());
        }
        if (dto.active() != null) {
            member.setActive(dto.active());
        }
        return toDto(memberRepository.save(member));
    }

    @Transactional
    public void deleteMember(UUID id) {
        if (!memberRepository.existsById(id)) {
            throw new ResourceNotFoundException("Участник с ID " + id + " не найден");
        }
        memberRepository.deleteById(id);
    }

    public Member findEntityById(UUID id) {
        return memberRepository.findById(id)
                .orElseThrow(() -> new ResourceNotFoundException("Участник с ID " + id + " не найден"));
    }

    private MemberResponseDto toDto(Member member) {
        return new MemberResponseDto(member.getId(), member.getFullName(), member.isActive());
    }
}
