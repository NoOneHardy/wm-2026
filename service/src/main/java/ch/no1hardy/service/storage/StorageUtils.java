package ch.no1hardy.service.storage;

import ch.no1hardy.service.exception.StorageException;
import jakarta.validation.constraints.NotNull;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class StorageUtils {
    public static Path resolvePath(String path) {
        return trimPath(Paths.get(path));
    }

    public static Path resolvePath(Path parent, String child) {
        return trimPath(parent.resolve(child));
    }

    public static String trimPath(String path) {
        return path.replace("../", "./").replace("..\\", ".\\");
    }

    public static Path trimPath(Path path) {
        return Paths.get(trimPath(path.toString())).normalize();
    }

    public static String renameFile(MultipartFile file, @NotNull String newName) {
        String newExtension = getFileExtension(newName);
        String extension = getFileExtension(file);

        if (newExtension.equals(extension)) return newName;

        return newExtension.isEmpty() ? newName + "." + extension : newName;
    }

    public static String getFileExtension(MultipartFile file) {
        String filename = file.getOriginalFilename();
        if (filename == null) return "";
        return getFileExtension(filename);
    }

    public static String getFileExtension(@NotNull String filename) {
        int dotIndex = filename.lastIndexOf('.');
        if (dotIndex == -1 || dotIndex == filename.length() - 1) return "";
        return filename.substring(dotIndex + 1);
    }

    public static Path initDirectory(Path dir) {
        return initDirectory(dir, "");
    }

    public static Path initDirectory(Path parent, String dir) {
        try {
            Path path = resolvePath(parent, trimPath(dir));
            Files.createDirectories(path);
            return path;
        } catch (Exception e) {
            throw new StorageException("Could not initialize directory " + dir, "Fehler beim Erstellen des Verzeichnisses.");
        }
    }
}
