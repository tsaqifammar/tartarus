package io.github.tsaqifammar.tartarus.test.configuration;

import io.github.tsaqifammar.tartarus.configuration.ServerConfiguration;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;

class ServerConfigurationTest {

    @Test
    void testPortIsStoredCorrectly() {
        var config = new ServerConfiguration(9090);
        assertEquals(9090, config.getPort(), "Port should match the one passed to constructor");
    }

    @Test
    void testDefaultConfigIsSingleton() {
        ServerConfiguration config1 = ServerConfiguration.getDefaultConfig();
        ServerConfiguration config2 = ServerConfiguration.getDefaultConfig();

        assertSame(config1, config2, "getDefaultConfig should always return the same instance");
    }

    @Test
    void testDefaultConfigHasExpectedPort() {
        ServerConfiguration defaultConfig = ServerConfiguration.getDefaultConfig();
        assertEquals(8080, defaultConfig.getPort(), "Default config should use port 8080");
    }
}