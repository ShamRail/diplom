package ru.ugasu.app.config;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientConfig;
import com.github.dockerjava.core.DockerClientImpl;
import com.github.dockerjava.httpclient5.ApacheDockerHttpClient;
import com.github.dockerjava.transport.DockerHttpClient;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.concurrent.ExecutorService;
import java.util.concurrent.Executors;

@Configuration
public class DockerConfig {

    @Bean
    public DockerClientConfig dockerClientConfig() {
        return DefaultDockerClientConfig
                .createDefaultConfigBuilder()
                .withDockerTlsVerify(false)
                .withApiVersion("1.41")
                .withDockerHost(System.getenv("DOCKER_HOST"))
                .withRegistryUsername(System.getenv("DOCKER_USER"))
                .withRegistryPassword(System.getenv("DOCKER_PASSWORD"))
                .withRegistryEmail("rail.shamsemukhametov@mail.ru")
                .withRegistryUrl("https://index.docker.io/v1/")
                .build();
    }

    @Bean
    public DockerHttpClient dockerHttpClient() {
        DockerClientConfig config = dockerClientConfig();
        return new ApacheDockerHttpClient.Builder()
                .dockerHost(config.getDockerHost())
                .sslConfig(config.getSSLConfig())
                .build();
    }

    @Bean
    public DockerClient dockerClient() {
        return DockerClientImpl.getInstance(dockerClientConfig(), dockerHttpClient());
    }

    @Bean
    public ExecutorService executorService() {
        return Executors.newFixedThreadPool(Runtime.getRuntime().availableProcessors());
    }


}
