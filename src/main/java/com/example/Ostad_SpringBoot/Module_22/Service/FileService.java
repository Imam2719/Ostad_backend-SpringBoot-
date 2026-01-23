package com.example.Ostad_SpringBoot.Module_22.Service;

import com.example.Ostad_SpringBoot.Module_22.repository.FileMetadata;
import com.example.Ostad_SpringBoot.Module_22.repository.FileMetadataRepository;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.security.SecureRandom;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.UUID;

@Service
public class FileService {

    private static final Logger logger = LoggerFactory.getLogger(FileService.class);
    private static final long MAX_FILE_SIZE = 50 * 1024 * 1024; // 50MB

    private final Path fileStorageLocation;
    private final FileMetadataRepository fileMetadataRepository;
    private final int otpExpiryMinutes;

    public FileService(@Value("${file.upload.dir}") String uploadDir,
                       @Value("${otp.expiry.minutes}") int otpExpiryMinutes,
                       FileMetadataRepository fileMetadataRepository) throws IOException {
        this.otpExpiryMinutes = otpExpiryMinutes;
        this.fileMetadataRepository = fileMetadataRepository;
        this.fileStorageLocation = Paths.get(uploadDir).toAbsolutePath().normalize();

        try {
            Files.createDirectories(this.fileStorageLocation);
            logger.info("File storage directory created at: {}", this.fileStorageLocation);
        } catch (Exception ex) {
            throw new IOException("Could not create the directory for file uploads.", ex);
        }
    }

    public UploadResponse uploadFile(MultipartFile file) throws Exception {
        // Validation
        if (file.isEmpty()) {
            throw new IllegalArgumentException("Cannot upload empty file");
        }

        if (file.getSize() > MAX_FILE_SIZE) {
            throw new IllegalArgumentException("File size exceeds maximum allowed size of 50MB");
        }

        String originalFileName = file.getOriginalFilename();
        if (originalFileName == null || originalFileName.contains("..")) {
            throw new IllegalArgumentException("Invalid file name");
        }

        // Generate unique stored file name
        String storedFileName = UUID.randomUUID().toString() + "_" + originalFileName;

        // Generate numeric OTP
        String otp = generateNumericOTP();

        // Calculate expiry time
        LocalDateTime uploadTime = LocalDateTime.now();
        LocalDateTime expiryTime = uploadTime.plusMinutes(otpExpiryMinutes);

        // Store file on disk
        Path targetLocation = this.fileStorageLocation.resolve(storedFileName);
        Files.copy(file.getInputStream(), targetLocation, StandardCopyOption.REPLACE_EXISTING);

        // Save metadata to database
        FileMetadata metadata = new FileMetadata(
                originalFileName,
                storedFileName,
                file.getContentType(),
                file.getSize(),
                otp,
                uploadTime,
                expiryTime
        );

        fileMetadataRepository.save(metadata);

        logger.info("File uploaded successfully: {} with OTP: {}", originalFileName, otp);

        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss");

        return new UploadResponse(
                "File uploaded successfully",
                otp,
                originalFileName,
                file.getSize(),
                expiryTime.format(formatter)
        );
    }

    public DownloadLinkResponse getDownloadLink(String otp) throws Exception {
        if (otp == null || otp.trim().isEmpty()) {
            throw new IllegalArgumentException("OTP is required");
        }

        if (!otp.matches("\\d+")) {
            throw new IllegalArgumentException("OTP must be numeric");
        }

        FileMetadata metadata = fileMetadataRepository.findByOtp(otp)
                .orElseThrow(() -> new IllegalArgumentException("Invalid OTP"));

        LocalDateTime now = LocalDateTime.now();

        // Check if OTP has expired
        if (now.isAfter(metadata.getExpiryTime()) || metadata.isExpired()) {
            throw new IllegalArgumentException("OTP has expired");
        }

        // Check if OTP has been used
        if (metadata.isOtpUsed()) {
            throw new IllegalArgumentException("OTP has already been used");
        }

        // Mark OTP as used
        metadata.setOtpUsed(true);
        fileMetadataRepository.save(metadata);

        String downloadLink = "/api/files/download/" + metadata.getStoredFileName();

        logger.info("Download link generated for file: {}", metadata.getFileName());

        return new DownloadLinkResponse(
                "Download link generated successfully",
                downloadLink,
                metadata.getFileName()
        );
    }

    public Resource loadFileAsResource(String storedFileName, LocalDateTime requestTime) throws Exception {
        FileMetadata metadata = fileMetadataRepository.findByStoredFileName(storedFileName)
                .orElseThrow(() -> new IllegalArgumentException("File not found"));

        // Check if file is still within 10 minutes window from upload
        if (requestTime.isAfter(metadata.getExpiryTime()) || metadata.isExpired()) {
            throw new IllegalArgumentException("File has expired and cannot be accessed");
        }

        Path filePath = this.fileStorageLocation.resolve(storedFileName).normalize();
        Resource resource = new UrlResource(filePath.toUri());

        if (resource.exists()) {
            logger.info("File downloaded: {}", metadata.getFileName());
            return resource;
        } else {
            throw new IllegalArgumentException("File not found on disk");
        }
    }

    public void deleteExpiredFiles() {
        LocalDateTime now = LocalDateTime.now();
        var expiredFiles = fileMetadataRepository.findByExpiryTimeBeforeAndExpiredFalse(now);

        for (FileMetadata metadata : expiredFiles) {
            try {
                // Delete physical file
                Path filePath = this.fileStorageLocation.resolve(metadata.getStoredFileName()).normalize();
                Files.deleteIfExists(filePath);

                // Mark as expired in database
                metadata.setExpired(true);
                fileMetadataRepository.save(metadata);

                logger.info("Expired file deleted: {}", metadata.getFileName());
            } catch (Exception e) {
                logger.error("Error deleting expired file: {}", metadata.getFileName(), e);
            }
        }

        if (!expiredFiles.isEmpty()) {
            logger.info("Deleted {} expired files", expiredFiles.size());
        }
    }

    private String generateNumericOTP() {
        SecureRandom random = new SecureRandom();
        int otp = 100000 + random.nextInt(900000); // 6-digit OTP
        return String.valueOf(otp);
    }
}