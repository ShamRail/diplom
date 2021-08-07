package ru.ugasu.app.service.io.archive;

import net.lingala.zip4j.ZipFile;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileNotFoundException;
import java.io.FileOutputStream;
import java.io.InputStream;
import java.io.OutputStream;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

@Service
public class ZipDecompressService implements DecompressService {

    private static final Logger LOGGER = LoggerFactory.getLogger(ZipDecompressService.class.getSimpleName());

    /**
     * Разархивирует файл zip в папку target
     * @param source путь к исходному архиву
     * @param target путь к папке куда происходит разархивирование
     * @return target если все прошло успешно
     * @throws Exception клиент ответственнен за ошибки IO
     * @throws FileNotFoundException если не найден путь target
     * @throws InvalidPathException если исходных файл это не zip
     * @throws InvalidPathException если target это не директория
     */
    @Override
    public Path decompress(Path source, Path target) throws Exception {
        try {
            if (!Files.exists(target) || !Files.exists(source)) {
                throw new FileNotFoundException("Invalid target or source path!");
            }
            if (Files.isRegularFile(target)) {
                throw new InvalidPathException(" is not a folder!", target.toString());
            }
            if (!source.toString().endsWith(".zip")) {
                throw new InvalidPathException(" is not a zip file!", source.toString());
            }
            LOGGER.debug("Start compressing ...");
            ZipFile zipFile = new ZipFile(source.toFile());
            zipFile.extractAll(target.toString());
            LOGGER.debug("Compressing complete");
        } catch (Exception e) {
            LOGGER.debug("Failed to unzip, because {} is thrown. Message: {}", e.getClass().getSimpleName(), e.getMessage());
            throw e;
        }
        return target;
    }

    /**
     * Разархивирует содержимое стрима zip файла в папку target
     * @param source стрим архива
     * @param target путь к папке куда происходит разархивирование
     * @return target если все прошло успешно
     * @throws Exception клиент ответственнен за ошибки IO
     * @throws FileNotFoundException если не найден путь target
     * @throws InvalidPathException если исходных файл это не zip
     * @throws InvalidPathException если target это не директория
     */
    @Override
    public Path decompress(InputStream source, Path target) throws Exception {
        LOGGER.debug("Create temp file");
        Path tempFile = Files.createTempFile("zipping", ".zip");
        try (OutputStream out = new FileOutputStream(tempFile.toFile())) {
            LOGGER.debug("Write to temp file");
            source.transferTo(out);
            LOGGER.debug("Writing is complete. Start Decompressing ...");
            decompress(tempFile, target);
            LOGGER.debug("Decompressing is complete");
        } catch (Exception e) {
            LOGGER.debug("Failed to unzip, because {} is thrown. Message: {}", e.getClass().getSimpleName(), e.getMessage());
            LOGGER.debug("Remove temp file");
            Files.deleteIfExists(tempFile);
        }
        return target;
    }
}
