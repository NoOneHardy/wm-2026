package ch.no1hardy.service.service;

import ch.no1hardy.service.exception.StorageException;
import ch.no1hardy.service.storage.StorageProperties;
import ch.no1hardy.service.storage.StorageService;
import ch.no1hardy.service.storage.StorageUtils;
import jakarta.validation.constraints.NotNull;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.StandardCopyOption;
import java.util.stream.Stream;

@Service
public class FileService implements StorageService {
    private final Logger logger = LoggerFactory.getLogger(FileService.class);
    private final Path root;

    @Autowired
    public FileService(StorageProperties props) {
        if (props.getLocation().trim().isEmpty()) {
            throw new IllegalArgumentException("File upload location can not be empty.");
        }

        this.root = StorageUtils.resolvePath(props.getLocation());
    }

    @Override
    public void init() {
        Path rootPath = StorageUtils.initDirectory(root);
        logger.info("Init storage directory: {}", rootPath.toAbsolutePath());
    }

    @Override
    public void store(MultipartFile file) {
        store(file, "");
    }

    @Override
    public Stream<Path> loadAll() {
        return Stream.empty();
    }

    @Override
    public Path load(String filename) {
        return null;
    }

    @Override
    public void deleteAll() {

    }

    public void store(MultipartFile file, @NotNull String subDirectory) {
        try {
            if (file.isEmpty() || file.getOriginalFilename() == null)
                throw new StorageException("File upload empty.", "Fehler beim Speichern der Datei.", HttpStatus.BAD_REQUEST);

            Path subDir = StorageUtils.initDirectory(root, subDirectory);
            Path dest = StorageUtils.resolvePath(subDir, file.getOriginalFilename());

            if (!dest.getParent().equals(subDir))
                throw new StorageException("File upload outside directory.", "Fehler beim Speichern der Datei.", HttpStatus.BAD_REQUEST);

            try (InputStream stream = file.getInputStream()) {
                Files.copy(stream, dest, StandardCopyOption.REPLACE_EXISTING);
            }

        } catch (IOException e) {
            throw new StorageException("File upload failed.", "Fehler beim Speichern der Datei.");
        }
    }
}
