package com.mtalaat.restaurant.utility;

import com.mtalaat.restaurant.exceptions.BadRequestException;
import com.mtalaat.restaurant.exceptions.InternalServerErrorException;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;

@Component
public class FileUtility {

    private final Path baseStoragePath;

    public FileUtility(@Value("${app.files.base-dir:uploads}") String baseStorageDir) {
        this.baseStoragePath = Paths.get(baseStorageDir).toAbsolutePath().normalize();
    }

    public String saveFile(MultipartFile file, String locationToSave) {
        if (file == null || file.isEmpty()) {
            throw new BadRequestException("File is required");
        }

        String originalFilename = StringUtils.cleanPath(file.getOriginalFilename() == null ? "" : file.getOriginalFilename());
        if (originalFilename.contains("..")) {
            throw new BadRequestException("Invalid file name");
        }

        String normalizedLocation = normalizeLocation(locationToSave);
        Path targetDirectory = normalizedLocation.isBlank()
                ? baseStoragePath
                : baseStoragePath.resolve(normalizedLocation).normalize();

        if (!targetDirectory.startsWith(baseStoragePath)) {
            throw new BadRequestException("Invalid save location");
        }

        String extension = "";
        int extensionIndex = originalFilename.lastIndexOf('.');
        if (extensionIndex >= 0) {
            extension = originalFilename.substring(extensionIndex);
        }
        String generatedFilename = UUID.randomUUID() + extension;

        try {
            Files.createDirectories(targetDirectory);
            Path targetFile = targetDirectory.resolve(generatedFilename);
            try (InputStream inputStream = file.getInputStream()) {
                Files.copy(inputStream, targetFile, StandardCopyOption.REPLACE_EXISTING);
            }
        } catch (IOException ex) {
            throw new InternalServerErrorException("Could not save file");
        }

        return normalizedLocation.isBlank()
                ? "/files/" + generatedFilename
                : "/files/" + normalizedLocation + "/" + generatedFilename;
    }

    private String normalizeLocation(String locationToSave) {
        if (locationToSave == null || locationToSave.isBlank()) {
            return "";
        }

        String normalized = locationToSave.trim().replace('\\', '/');
        normalized = normalized.replaceAll("^/+|/+$", "");
        return normalized;
    }
}