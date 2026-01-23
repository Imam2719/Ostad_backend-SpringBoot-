package com.example.Ostad_SpringBoot.Module_22.Service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Component;

@Component
public class FileCleanupScheduler {

    private static final Logger logger = LoggerFactory.getLogger(FileCleanupScheduler.class);
    private final FileService fileService;

    public FileCleanupScheduler(FileService fileService) {
        this.fileService = fileService;
    }

    // Run every minute to check for expired files
    @Scheduled(fixedRate = 60000)
    public void cleanupExpiredFiles() {
        logger.debug("Running scheduled file cleanup task");
        fileService.deleteExpiredFiles();
    }
}