package io.github.tsaqifammar.tartarus.server;

import io.github.abcqwq.magia.injection.core.ComponentRegistrator;
import io.github.tsaqifammar.tartarus.configuration.ServerConfiguration;
import io.github.tsaqifammar.tartarus.handler.BaseHandler;
import io.github.tsaqifammar.tartarus.handler.HandlerBinder;
import spark.Service;

import java.util.Objects;
import java.util.Set;

public class Server {

    private static Service http;

    public static void start(String basePackage) {
        start(basePackage, ServerConfiguration.getDefaultConfig());
    }

    public static void start(String basePackage, ServerConfiguration config) {
        start(Set.of(basePackage), config);
    }

    public static void start(Set<String> packages, ServerConfiguration config) {

        http = Service.ignite();
        http.port(config.getPort());

        ComponentRegistrator.registerComponents(packages);
        ComponentRegistrator.apply(BaseHandler.class, HandlerBinder::bind);
    }

    public static Service getInstance() {

        if (Objects.isNull(http)) {
            throw new IllegalStateException(
                    "Server has not been initialized. Please invoke Server.start() first."
            );
        }

        return http;
    }
}
