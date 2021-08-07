package ru.ugasu.app.service.io.load;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.stereotype.Service;

import java.io.FileOutputStream;
import java.net.URL;
import java.nio.ByteBuffer;
import java.nio.channels.Channels;
import java.nio.channels.ReadableByteChannel;
import java.nio.channels.WritableByteChannel;
import java.nio.file.Files;
import java.nio.file.Path;

@Service
public class RemoteAppRepositoryFileLoader implements AppRepositoryLoader {

    private static final int BUFFER_SIZE = 4096;

    private static final Logger LOGGER = LoggerFactory.getLogger(RemoteAppRepositoryFileLoader.class.getSimpleName());

    /**
     * Загружает файл по URL. Все директории по пути создаются предварительно
     * @param source источник репозитория
     * @param destination путь куда нужно загрузить проект
     *                    Предполагается, что последний элемент в пути это имя файла
     * @return путь к файлу в системе destination
     * @throws Exception клиент ответственен за обработку ислючений
     */
    @Override
    public String load(String source, String destination) throws Exception {
        URL url = new URL(source);
        try (ReadableByteChannel in = Channels.newChannel(url.openStream())) {
            Path target = Path.of(destination);
            ByteBuffer buffer = ByteBuffer.allocate(BUFFER_SIZE);
            try (WritableByteChannel out = new FileOutputStream(target.toFile()).getChannel()) {
                while (in.read(buffer) > 0) {
                    buffer.flip();
                    while (buffer.hasRemaining()) {
                        out.write(buffer);
                    }
                    buffer.clear();
                }
            }
        } catch (Exception e) {
            LOGGER.error("Failed to load project, because {} is thrown", e.getClass().getName());
            throw e;
        }
        return destination;
    }
}
