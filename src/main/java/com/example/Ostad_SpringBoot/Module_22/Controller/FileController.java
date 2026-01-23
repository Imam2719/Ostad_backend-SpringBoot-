package com.example.Ostad_SpringBoot.Module_22.Controller;

import com.example.Ostad_SpringBoot.Module_22.Service.FileService;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;

@RestController
@RequestMapping("/api/files")
public class FileController {

    private static final Logger logger = LoggerFactory.getLogger(FileController.class);
    private final FileService fileService;

    public FileController(FileService fileService) {
        this.fileService = fileService;
    }

    @PostMapping(value = "/upload", consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    public ResponseEntity<?> uploadFile(@RequestParam("file") MultipartFile file) {
        try {
            var response = fileService.uploadFile(file);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Validation error during file upload: {}", e.getMessage());
            return ResponseEntity.badRequest().body(createErrorResponse("Validation Error", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error uploading file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Upload Failed", "An error occurred while uploading the file"));
        }
    }

    @GetMapping("/access")
    public ResponseEntity<?> getDownloadLink(@RequestParam("otp") String otp) {
        try {
            var response = fileService.getDownloadLink(otp);
            return ResponseEntity.ok(response);
        } catch (IllegalArgumentException e) {
            logger.error("Invalid OTP or access error: {}", e.getMessage());
            return ResponseEntity.badRequest().body(createErrorResponse("Access Denied", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error generating download link", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Error", "An error occurred while processing your request"));
        }
    }

    @GetMapping("/download/{storedFileName}")
    public ResponseEntity<?> downloadFile(@PathVariable String storedFileName) {
        try {
            LocalDateTime requestTime = LocalDateTime.now();
            Resource resource = fileService.loadFileAsResource(storedFileName, requestTime);

            String contentType = "application/octet-stream";

            return ResponseEntity.ok()
                    .contentType(MediaType.parseMediaType(contentType))
                    .header(HttpHeaders.CONTENT_DISPOSITION,
                            "attachment; filename=\"" + resource.getFilename() + "\"")
                    .body(resource);
        } catch (IllegalArgumentException e) {
            logger.error("File access error: {}", e.getMessage());
            return ResponseEntity.status(HttpStatus.NOT_FOUND)
                    .body(createErrorResponse("File Not Found", e.getMessage()));
        } catch (Exception e) {
            logger.error("Error downloading file", e);
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR)
                    .body(createErrorResponse("Download Failed", "An error occurred while downloading the file"));
        }
    }

    private Map<String, String> createErrorResponse(String error, String message) {
        Map<String, String> errorResponse = new HashMap<>();
        errorResponse.put("error", error);
        errorResponse.put("message", message);
        errorResponse.put("timestamp", LocalDateTime.now().format(DateTimeFormatter.ISO_DATE_TIME));
        return errorResponse;
    }
}