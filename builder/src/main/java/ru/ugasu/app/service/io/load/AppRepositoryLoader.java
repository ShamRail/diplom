package ru.ugasu.app.service.io.load;

import java.net.MalformedURLException;

/**
 * Абстракция над загрузкой проекта.
 * Под репозиторием понимается репозиторий с исходным кодом.
 */
public interface AppRepositoryLoader {
    /**
     * Метод, загружающий проект.
     * @param source источник репозитория. Может быть путем к файлу
     * @param destination путь куда нужно загрузить проект. Может быть путем или URI
     * @return путь куда произошла загрузка
     * @throws Exception клиент ответственнен обрабывать исключения, связанные с созданием файлов и т.д.
     */
    String load(String source, String destination) throws Exception;
}
