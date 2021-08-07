package ru.ugasu.app;

import com.github.dockerjava.api.DockerClient;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.junit4.SpringRunner;

import static org.junit.Assert.*;

@SpringBootTest
@RunWith(SpringRunner.class)
public class AppRunnerApplicationTest {

    @Autowired
    DockerClient dockerClient;

    @Test
    public void dockerCmd() {
        dockerClient.pingCmd().exec();
    }

}