package ch.no1hardy.service.storage;

import ch.no1hardy.service.exception.StorageException;

import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;

public abstract class StorageUtils {
    public static Path resolvePath(String path) {
        return Paths.get(trimPath(path));
    }

    public static Path resolvePath(Path parent, String child) {
        return parent.resolve(trimPath(child));
    }

    public static String trimPath(String path) {
        return path.replace("../", "./");
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
