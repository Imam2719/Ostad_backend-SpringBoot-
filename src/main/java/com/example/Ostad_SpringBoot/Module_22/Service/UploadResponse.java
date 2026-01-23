package com.example.Ostad_SpringBoot.Module_22.Service;

// DTO for Upload Response
class UploadResponse {
    private String message;
    private String otp;
    private String fileName;
    private Long fileSize;
    private String expiryTime;

    public UploadResponse() {
    }

    public UploadResponse(String message, String otp, String fileName, Long fileSize, String expiryTime) {
        this.message = message;
        this.otp = otp;
        this.fileName = fileName;
        this.fileSize = fileSize;
        this.expiryTime = expiryTime;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getOtp() {
        return otp;
    }

    public void setOtp(String otp) {
        this.otp = otp;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }

    public Long getFileSize() {
        return fileSize;
    }

    public void setFileSize(Long fileSize) {
        this.fileSize = fileSize;
    }

    public String getExpiryTime() {
        return expiryTime;
    }

    public void setExpiryTime(String expiryTime) {
        this.expiryTime = expiryTime;
    }
}

// DTO for Download Link Response
class DownloadLinkResponse {
    private String message;
    private String downloadLink;
    private String fileName;

    public DownloadLinkResponse() {
    }

    public DownloadLinkResponse(String message, String downloadLink, String fileName) {
        this.message = message;
        this.downloadLink = downloadLink;
        this.fileName = fileName;
    }

    // Getters and Setters
    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getDownloadLink() {
        return downloadLink;
    }

    public void setDownloadLink(String downloadLink) {
        this.downloadLink = downloadLink;
    }

    public String getFileName() {
        return fileName;
    }

    public void setFileName(String fileName) {
        this.fileName = fileName;
    }
}

// DTO for Error Response
class ErrorResponse {
    private String error;
    private String message;
    private String timestamp;

    public ErrorResponse() {
    }

    public ErrorResponse(String error, String message, String timestamp) {
        this.error = error;
        this.message = message;
        this.timestamp = timestamp;
    }

    // Getters and Setters
    public String getError() {
        return error;
    }

    public void setError(String error) {
        this.error = error;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public String getTimestamp() {
        return timestamp;
    }

    public void setTimestamp(String timestamp) {
        this.timestamp = timestamp;
    }
}