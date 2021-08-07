package ru.ugasu.app.service.io;

import java.io.InputStream;
import java.nio.file.Path;

/**
 * Сервис по разахирированию файлов
 */
public interface DecompressService {
    /**
     * Разархивирует файл в папку target
     * @param source путь к исходному архиву
     * @param target путь к папку куда происходит разархивирование
     * @return target если все прошло успешно
     * @throws Exception клиент ответственно за ошибки IO
     */
    Path decompress(Path source, Path target) throws Exception;

    /**
     * Разархивирует содержимое стрима в папку target
     * @param source стрим архива
     * @param target путь к папку куда происходит разархивирование
     * @return target если все прошло успешно
     * @throws Exception клиент ответственно за ошибки IO
     */
    Path decompress(InputStream source, Path target) throws Exception;
}
