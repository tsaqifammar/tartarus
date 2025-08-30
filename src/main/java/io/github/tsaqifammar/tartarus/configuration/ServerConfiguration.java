package io.github.tsaqifammar.tartarus.configuration;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class ServerConfiguration {

    private static final ServerConfiguration instance = new ServerConfiguration(8080);

    @Getter
    private final int port;

    public static ServerConfiguration getDefaultConfig() {
        return instance;
    }
}
