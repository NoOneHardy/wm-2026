package ch.no1hardy.service.storage;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.Mockito;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.Path;
import java.nio.file.Paths;

import static org.junit.jupiter.api.Assertions.assertEquals;

public class StorageUtilsTest {
    private boolean isLinux() {
        String os = System.getProperty("os.name").toLowerCase();
        return os.contains("nix") || os.contains("nux") || os.contains("mac");
    }

    private void checkPath(String expected, String actual) {
        if (isLinux()) {
            assertEquals(expected.replace("\\", "/"), actual);
        } else {
            assertEquals(expected.replace("/", "\\"), actual);
        }
    }

    private void checkPath(String expected, Path actual) {
        checkPath(expected, actual.toString());
    }

    @Test
    @DisplayName("Path resolvePath(String) - should resolve a path")
    void resolvePath01() {
        String dirName = "dirName";
        Path dir = StorageUtils.resolvePath(dirName);
        checkPath("dirName", dir);
    }

    @Test
    @DisplayName("Path resolvePath(String) - should trim '../' and './' from the path")
    void resolvePath02() {
        String dirName = "../dirName/./child/../grandchild";
        Path dir = StorageUtils.resolvePath(dirName);
        checkPath("dirName/child/grandchild", dir);
    }

    @Test
    @DisplayName("Path resolvePath(Path, String) - should resolve a child path against a parent path")
    void resolvePath03() {
        Path parent = StorageUtils.resolvePath("parent");
        Path resolvedPath = StorageUtils.resolvePath(parent, "child");
        checkPath("parent/child", resolvedPath);
    }

    @Test
    @DisplayName("Path resolvePath(Path, String) - should trim '../' and './' from the path")
    void resolvePath04() {
        Path parent = StorageUtils.resolvePath("parent");
        Path resolvedPath = StorageUtils.resolvePath(parent, "../child/./grandchild");
        checkPath("parent/child/grandchild", resolvedPath);
    }

    @Test
    @DisplayName("String trimPath(String) - should replace '../' with './'")
    void trimPath01() {
        String path = "../some/../path/./to/../file.txt";
        String trimmedPath = StorageUtils.trimPath(path);
        assertEquals("./some/./path/./to/./file.txt", trimmedPath);
    }

    @Test
    @DisplayName("Path trimPath(Path) - should remove '../'")
    void trimPath02() {
        Path path = Paths.get("../some/../path/to/../file.txt");
        Path trimmedPath = StorageUtils.trimPath(path);
        checkPath("some/path/to/file.txt", trimmedPath);
    }

    @Test
    @DisplayName("Path trimPath(Path) - should normalize the path")
    void trimPath03() {
        Path path = Paths.get("some/./path/../to/file.txt");
        Path trimmedPath = StorageUtils.trimPath(path);
        checkPath("some/path/to/file.txt", trimmedPath);
    }

    @Test
    @DisplayName("String getFileExtension(MultipartFile) - should return the file extension")
    void getFileExtension01() {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        Mockito.when(file.getOriginalFilename()).thenReturn("document.pdf");

        String extension = StorageUtils.getFileExtension(file);
        assertEquals("pdf", extension);
    }

    @Test
    @DisplayName("String getFileExtension(MultipartFile) - should return empty string if no extension")
    void getFileExtension02() {
        MultipartFile file = Mockito.mock(MultipartFile.class);
        Mockito.when(file.getOriginalFilename()).thenReturn(null);

        String extension = StorageUtils.getFileExtension(file);
        assertEquals("", extension);
    }

    @Test
    @DisplayName("String getFileExtension(String) - should return the file extension")
    void getFileExtension03() {
        String filename = "archive.tar.gz";
        String extension = StorageUtils.getFileExtension(filename);
        assertEquals("gz", extension);
    }

    @Test
    @DisplayName("String getFileExtension(String) - should return empty string if no extension")
    void getFileExtension04() {
        String filename = "filename_without_extension";
        String extension = StorageUtils.getFileExtension(filename);
        assertEquals("", extension);
    }

    @Test
    @DisplayName("String getFileExtension(String) - should return empty string if filename ends with dot")
    void getFileExtension05() {
        String filename = "filename_with_dot.";
        String extension = StorageUtils.getFileExtension(filename);
        assertEquals("", extension);
    }
}
