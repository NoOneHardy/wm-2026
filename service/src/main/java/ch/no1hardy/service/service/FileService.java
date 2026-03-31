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
import java.util.UUID;

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
    public String store(MultipartFile file) {
        return store(file, "");
    }

    public String store(MultipartFile file, @NotNull String subDirectory) {
        return store(file, subDirectory, file.getOriginalFilename());
    }

    public String store(MultipartFile file, @NotNull String subDirectory, String name) {
        try {
            if (file.isEmpty() || file.getOriginalFilename() == null)
                throw new StorageException("File upload empty.", "Fehler beim Speichern der Datei.", HttpStatus.BAD_REQUEST);

            Path subDir = StorageUtils.initDirectory(root, subDirectory);
            String fileName = StorageUtils.renameFile(file, name);
            Path dest = StorageUtils.resolvePath(subDir, fileName);

            if (!dest.getParent().equals(subDir))
                throw new StorageException("File upload outside directory.", "Fehler beim Speichern der Datei.", HttpStatus.BAD_REQUEST);

            try (InputStream stream = file.getInputStream()) {
                Files.copy(stream, dest, StandardCopyOption.REPLACE_EXISTING);
                return getUrlPath(dest);
            }

        } catch (IOException e) {
            throw new StorageException("File upload failed.", "Fehler beim Speichern der Datei.");
        }
    }

    public String storeAvatar(MultipartFile avatarFile, @NotNull String userId) {
        return store(avatarFile, "avatars", userId);
    }

    public String storeFlag(MultipartFile flagFile) {
        return store(flagFile, "flags", UUID.randomUUID().toString());
    }

    private String getUrlPath(Path path) {
        String url = path.toString().replace("\\", "/");
        return "/cdn" + url.substring(url.indexOf("/"));
    }
}
