package com.fdc.quartesure.quartesurebackend.serviceImpl;

import com.fdc.quartesure.quartesurebackend.service.FileStorageService;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
@Service
public class FileStorageServiceImpl implements FileStorageService {

    private static final String UPLOAD_DIR = "uploads/";
    @Override
    public String saveFile(MultipartFile file) {
        try {
            if (file == null || file.isEmpty()) return null;

            Files.createDirectories(Paths.get(UPLOAD_DIR));

            String fileName = System.currentTimeMillis() + "_" + file.getOriginalFilename();
            Path path = Paths.get(UPLOAD_DIR + fileName);

            Files.write(path, file.getBytes());
            return path.toString();

        } catch (Exception e) {
            throw new RuntimeException("Erreur lors de l'enregistrement du fichier.");
        }
    }
}