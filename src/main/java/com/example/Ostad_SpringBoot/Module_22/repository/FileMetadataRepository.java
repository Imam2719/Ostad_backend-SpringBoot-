package com.example.Ostad_SpringBoot.Module_22.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Repository
public interface FileMetadataRepository extends JpaRepository<FileMetadata, Long> {

    Optional<FileMetadata> findByOtp(String otp);

    List<FileMetadata> findByExpiryTimeBeforeAndExpiredFalse(LocalDateTime currentTime);

    Optional<FileMetadata> findByStoredFileName(String storedFileName);
}