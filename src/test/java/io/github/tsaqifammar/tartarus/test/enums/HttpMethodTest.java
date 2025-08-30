package io.github.tsaqifammar.tartarus.test.enums;

import io.github.tsaqifammar.tartarus.enums.HttpMethod;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class HttpMethodTest {

    @Test
    void testEnumConstantsExist() {
        assertNotNull(HttpMethod.GET);
        assertNotNull(HttpMethod.POST);
        assertNotNull(HttpMethod.PUT);
        assertNotNull(HttpMethod.DELETE);
        assertNotNull(HttpMethod.PATCH);
    }

    @Test
    void testValueOf() {
        assertEquals(HttpMethod.GET, HttpMethod.valueOf("GET"));
        assertEquals(HttpMethod.POST, HttpMethod.valueOf("POST"));
        assertEquals(HttpMethod.PUT, HttpMethod.valueOf("PUT"));
        assertEquals(HttpMethod.DELETE, HttpMethod.valueOf("DELETE"));
        assertEquals(HttpMethod.PATCH, HttpMethod.valueOf("PATCH"));
    }

    @Test
    void testValuesOrder() {
        HttpMethod[] methods = HttpMethod.values();
        assertArrayEquals(
                new HttpMethod[]{HttpMethod.GET, HttpMethod.POST, HttpMethod.PUT, HttpMethod.DELETE, HttpMethod.PATCH},
                methods
        );
    }

    @Test
    void testValueOfInvalidThrowsException() {
        assertThrows(IllegalArgumentException.class, () -> HttpMethod.valueOf("INVALID"));
    }
}
