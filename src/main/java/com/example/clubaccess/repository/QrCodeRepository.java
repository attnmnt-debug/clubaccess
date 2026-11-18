package com.example.clubaccess.repository;

import com.example.clubaccess.entity.QrCode;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface QrCodeRepository extends JpaRepository<QrCode, UUID> {

    @Query("SELECT q FROM QrCode q JOIN FETCH q.member WHERE q.id = :id")
    Optional<QrCode> findByIdWithMember(@Param("id") UUID id);

    List<QrCode> findAllByMemberId(UUID memberId);

    List<QrCode> findAllByMemberIdAndActiveTrue(UUID memberId);
}
