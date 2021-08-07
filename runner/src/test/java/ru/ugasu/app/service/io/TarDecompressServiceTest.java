package ru.ugasu.app.service.io;

import org.junit.Test;

import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;
import java.util.List;
import java.util.stream.Collectors;

import static org.junit.Assert.*;

public class TarDecompressServiceTest {

    String pathToTestData = Path.of("./", "src", "test", "data").toString();

    @Test
    public void whenPathToPathSuccess() throws Exception {
        Path tempDir = Files.createTempDirectory("project");
        Path tarFile = Path.of(pathToTestData, "project.tar");
        DecompressService decompressService = new TarDecompressService();
        decompressService.decompress(tarFile, tempDir);
        String folderInArchive = "project";
        Path pathFoSourceCode = Path.of(tempDir.toString(), folderInArchive);
        assertTrue(Files.exists(pathFoSourceCode));
        List<Path> paths = Files.walk(pathFoSourceCode)
                .filter(p -> Files.isRegularFile(p))
                .collect(Collectors.toList());
        assertEquals(paths.size(), 4);
        String pathStr = pathFoSourceCode.toString();
        assertFalse(Files.readString(Path.of(pathStr, "Dockerfile")).isEmpty());
        assertFalse(Files.readString(Path.of(pathStr, "src", "main", "java", "demo", "Main.java")).isEmpty());
        assertFalse(Files.readString(Path.of(pathStr, ".gitignore")).isEmpty());
        assertFalse(Files.readString(Path.of(pathStr, "pom.xml")).isEmpty());
    }

    @Test
    public void whenStreamToPathSuccess() throws Exception {
        Path tempDir = Files.createTempDirectory("project");
        Path tarFile = Path.of(pathToTestData, "project.tar");
        try (InputStream in = new FileInputStream(tarFile.toFile())) {
            DecompressService decompressService = new TarDecompressService();
            decompressService.decompress(in, tempDir);
        }
        String folderInArchive = "project";
        Path pathFoSourceCode = Path.of(tempDir.toString(), folderInArchive);
        assertTrue(Files.exists(pathFoSourceCode));
        List<Path> paths = Files.walk(pathFoSourceCode)
                .filter(p -> Files.isRegularFile(p))
                .collect(Collectors.toList());
        assertEquals(paths.size(), 4);
        String pathStr = pathFoSourceCode.toString();
        assertFalse(Files.readString(Path.of(pathStr, "Dockerfile")).isEmpty());
        assertFalse(Files.readString(Path.of(pathStr, "src", "main", "java", "demo", "Main.java")).isEmpty());
        assertFalse(Files.readString(Path.of(pathStr, ".gitignore")).isEmpty());
        assertFalse(Files.readString(Path.of(pathStr, "pom.xml")).isEmpty());
    }

    @Test(expected = FileNotFoundException.class)
    public void whenInvalidSourcePath() throws Exception {
        Path tempDir = Files.createTempDirectory("project");
        Path zipFile = Path.of(pathToTestData, "invalid", "project.tar");
        DecompressService decompressService = new TarDecompressService();
        decompressService.decompress(zipFile, tempDir);
    }

    @Test(expected = FileNotFoundException.class)
    public void whenInvalidTargetPath() throws Exception {
        Path tempDir = Files.createTempDirectory("project");
        Path zipFile = Path.of(pathToTestData, "project.zip");
        DecompressService decompressService = new TarDecompressService();
        decompressService.decompress(zipFile, Path.of(tempDir.toString(), "invalid"));
    }

    @Test(expected = InvalidPathException.class)
    public void whenInvalidTargetFormat() throws Exception {
        Path tempDir = Files.createTempFile("project", "txt");
        Path zipFile = Path.of(pathToTestData, "project.zip");
        DecompressService decompressService = new TarDecompressService();
        decompressService.decompress(zipFile, tempDir);
    }

    @Test(expected = InvalidPathException.class)
    public void whenInvalidSourceFormat() throws Exception {
        Path tempDir = Files.createTempDirectory("project");
        Path zipFile = Path.of(pathToTestData, "file.txt");
        DecompressService decompressService = new TarDecompressService();
        decompressService.decompress(zipFile, tempDir);
    }

}