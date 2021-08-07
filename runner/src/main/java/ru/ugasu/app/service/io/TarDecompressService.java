package ru.ugasu.app.service.io;

import org.apache.commons.compress.archivers.tar.TarArchiveEntry;
import org.apache.commons.compress.archivers.tar.TarArchiveInputStream;
import org.apache.commons.compress.utils.IOUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.*;
import java.nio.file.Files;
import java.nio.file.InvalidPathException;
import java.nio.file.Path;

/**
 * Сервис по распаковке tar архива.
 * ВАЖНО! поддерживаются только архивы без сжатия, т.е. файл должен иметь расширение tar
 */
@Service
public class TarDecompressService implements DecompressService {

    private final static Logger LOGGER = LoggerFactory.getLogger(TarDecompressService.class.getSimpleName());

    /**
     * Разархивирует файл tar в папку target
     * @param source путь к исходному архиву
     * @param target путь к папке куда происходит разархивирование
     * @return target если все прошло успешно
     * @throws Exception клиент ответственен за ошибки IO
     * @throws FileNotFoundException если не найден путь target
     * @throws InvalidPathException если исходных файл это не tar
     * @throws InvalidPathException если target это не директория
     */
    @Override
    public Path decompress(Path source, Path target) throws Exception {
        try {
            validate(source, target);
            File out = target.toFile();
            LOGGER.debug("Start decompressing ...");
            decompress(new FileInputStream(source.toFile()), out);
            LOGGER.debug("Decompressing is done");
        } catch (Exception e) {
            LOGGER.error("Failed to decompress, because {} is thrown. Message: {}", e.getClass(), e.getMessage());
            throw e;
        }
        return target;
    }

    /**
     * Разархивирует стрим файла tar в папку target
     * @param source стрим файла
     * @param target путь к папке куда происходит разархивирование
     * @return target если все прошло успешно
     * @throws Exception клиент ответственен за ошибки IO
     * @throws FileNotFoundException если не найден путь target
     * @throws InvalidPathException если исходных файл это не tar
     * @throws InvalidPathException если target это не директория
     */
    @Override
    public Path decompress(InputStream source, Path target) throws Exception {
        validate(target);
        File out = target.toFile();
        decompress(source, out);
        return target;
    }

    private void validate(Path target) throws Exception {
        if (!Files.exists(target)) {
            throw new FileNotFoundException("Invalid target path!");
        }
        if (Files.isRegularFile(target)) {
            throw new InvalidPathException("is not a folder!", target.toString());
        }
    }

    private void validate(Path source, Path target) throws Exception {
        validate(target);
        if (!Files.exists(source)) {
            throw new FileNotFoundException("Invalid source path!");
        }
        if (!source.toString().endsWith(".tar")) {
            throw new InvalidPathException("is invalid. Only tar without compressing algorithm supported. Only tar is supported", source.toString());
        }
    }

    private void decompress(InputStream source, File out) throws Exception {
        try (TarArchiveInputStream fin = new TarArchiveInputStream(source)) {
            TarArchiveEntry entry;
            while ((entry = fin.getNextTarEntry()) != null) {
                if (entry.isDirectory()) {
                    continue;
                }
                File currentFile = new File(out, entry.getName());
                File parent = currentFile.getParentFile();
                if (!parent.exists()) {
                    parent.mkdirs();
                }
                try (OutputStream outputStream = new FileOutputStream(currentFile)) {
                    IOUtils.copy(fin, outputStream);
                }
            }
        }
    }

}
