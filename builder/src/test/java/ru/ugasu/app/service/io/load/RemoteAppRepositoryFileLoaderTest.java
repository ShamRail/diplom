package ru.ugasu.app.service.io.load;

import org.junit.Assert;
import org.junit.Test;
import ru.ugasu.app.service.io.load.AppRepositoryLoader;
import ru.ugasu.app.service.io.load.RemoteAppRepositoryFileLoader;

import java.io.IOException;
import java.net.UnknownHostException;
import java.nio.file.Files;
import java.nio.file.Path;

public class RemoteAppRepositoryFileLoaderTest {

    String urlPrefix = "file:";

    String absolutePathToProject;

    {
        try {
            absolutePathToProject = Path.of("./").toRealPath().toString();
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    String pathToTestData = Path.of(absolutePathToProject,
            "src", "test", "data"
    ).toString();

    @Test
    public void whenSuccessUpload() throws Exception {
        String fileName = "file.txt";
        String sourcePath = urlPrefix + pathToTestData + "/" + fileName;
        String targetPath = Files.createTempFile("123", ".txt").toString();
        AppRepositoryLoader repositoryLoader = new RemoteAppRepositoryFileLoader();
        String output = repositoryLoader.load(sourcePath, targetPath);
        Assert.assertEquals(targetPath, output);
        Assert.assertTrue(Files.exists(Path.of(targetPath)));
        Assert.assertEquals(
                Files.readString(Path.of(sourcePath.replace("file:", ""))),
                Files.readString(Path.of(targetPath))
        );
    }

    @Test(expected = UnknownHostException.class)
    public void whenInvalidUrl() throws Exception {
        String sourcePath = "http://invalid/url";
        String target = "not checked";
        AppRepositoryLoader appRepositoryLoader = new RemoteAppRepositoryFileLoader();
        appRepositoryLoader.load(sourcePath, target);
    }

}