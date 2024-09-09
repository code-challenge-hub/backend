package com.cch.codechallengehub.config;

import jakarta.annotation.PostConstruct;
import jakarta.annotation.PreDestroy;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.util.StringUtils;
import redis.embedded.RedisServer;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStreamReader;

@TestConfiguration
public class TestRedisConfiguration {

    @Value("${redis.port:6379}")
    private int redisPort;

    private static final String CHECK_PORT_IS_AVAILABLE_WIN = "netstat -nao | find \"LISTEN\" | find \"%d\"";
    private static final String CHECK_PORT_IS_AVAILABLE_ANOTHER = "netstat -nat | grep LISTEN|grep %d";

    private RedisServer redisServer;

    @PostConstruct
    public void startRedis() throws IOException{
        int port = isRedisRunning() ? findAvailablePort() : redisPort;
       redisServer = RedisServer.newRedisServer()
                .port(port)
                .setting("maxmemory 128M")
                .build();
        redisServer.start();
    }

    @PreDestroy
    public void stopRedis() throws IOException {
        redisServer.stop();
    }


    private boolean isRedisRunning() throws IOException {
        return isRunning(executeGrepProcessCommand(redisPort));
    }

    private boolean isRunning(Process process) {
        String line;
        StringBuilder pidInfo = new StringBuilder();

        try (BufferedReader input = new BufferedReader(new InputStreamReader(process.getInputStream()))) {

            while ((line = input.readLine()) != null) {
                pidInfo.append(line);
            }

        } catch (Exception e) {
        }

        return StringUtils.hasText(pidInfo.toString());
    }

    private Process executeGrepProcessCommand(int port) throws IOException {
        String os = System.getProperty("os.name").toLowerCase();

        String[] shell;
        if (os.contains("win")) {
            String command = String.format(CHECK_PORT_IS_AVAILABLE_WIN, port);
            shell = new String[] {"cmd.exe", "/y", "/c", command};
        } else {
            String command = String.format(CHECK_PORT_IS_AVAILABLE_ANOTHER, port);
            shell = new String[] {"/bin/sh", "-c", command};
        }
        return Runtime.getRuntime().exec(shell);
    }

    private int findAvailablePort() throws IOException {
        for (int port = 10000; port <= 65535; port++) {
            Process process = executeGrepProcessCommand(port);
            if (!isRunning(process)) {
                return port;
            }
        }

        throw new IllegalArgumentException("Not Found Available port: 10000 ~ 65535");
    }
}
