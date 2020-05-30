package com.deepthink.common.docker;

import com.github.dockerjava.api.DockerClient;
import com.github.dockerjava.core.DefaultDockerClientConfig;
import com.github.dockerjava.core.DockerClientBuilder;
import com.github.dockerjava.core.DockerClientConfig;
import org.aspectj.weaver.ast.Test;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.function.Consumer;


/**
 * @program: vending_admin
 * @description:
 * @author: td
 * @create: 2020-04-10 10:46
 **/
@Component
public class NvidiaDockerClient {

    private String dockerHost;

    private volatile DockerClient dockerClient;
    private DockerClient getDockerClient() {
        List<? extends NvidiaDockerClient> lists = null;
        NvidiaDockerClient nvidiaDockerClient = lists.get(1);

        List<String> list = new ArrayList<>();
        if (dockerClient != null) {
            return dockerClient;
        }
        synchronized (this) {
            if (dockerClient != null) {
                return dockerClient;
            }
            DockerClientConfig config = DefaultDockerClientConfig.createDefaultConfigBuilder()
                    .withDockerHost("tcp://my-docker-host.tld:2376")
                    .withDockerTlsVerify(false)
                    .withDockerConfig("/home/user/.docker")
                    .withRegistryUrl("https://index.docker.io/v1/")
                    .withRegistryUsername("dockeruser")
                    .withRegistryPassword("ilovedocker")
                    .withRegistryEmail("dockeruser@github.com")
                    .build();
            DockerClient docker = DockerClientBuilder.getInstance(config).build();
        }
        return null;
    }
}
