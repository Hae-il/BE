package com.haeil.be.file.service;

import com.haeil.be.file.domain.FileEntity;
import com.haeil.be.file.repository.FileRepository;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.UUID;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

@Service
@RequiredArgsConstructor
public class FileService {

    private final FileRepository fileRepository;

    @Value("${file.upload.directory:./uploads}")
    private String uploadDirectory;

    public FileEntity uploadFile(MultipartFile file) throws IOException {
        String originalFilename = file.getOriginalFilename();
        String storedFilename = generateStoredFilename(originalFilename);

        Path uploadPath = Paths.get(uploadDirectory);
        if (!Files.exists(uploadPath)) {
            Files.createDirectories(uploadPath);
        }

        Path filePath = uploadPath.resolve(storedFilename);
        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);

        FileEntity fileEntity =
                FileEntity.builder()
                        .originalFilename(originalFilename)
                        .storedFilename(storedFilename)
                        .fileUrl(filePath.toString())
                        .fileSize(file.getSize())
                        .contentType(file.getContentType())
                        .build();

        return fileRepository.save(fileEntity);
    }

    private String generateStoredFilename(String originalFilename) {
        String extension = "";
        if (originalFilename != null && originalFilename.contains(".")) {
            extension = originalFilename.substring(originalFilename.lastIndexOf("."));
        }
        return UUID.randomUUID().toString() + extension;
    }
}
