package io.github.tsaqifammar.tartarus.test.handler;

import io.github.tsaqifammar.tartarus.annotation.Route;
import io.github.tsaqifammar.tartarus.enums.HttpMethod;
import io.github.tsaqifammar.tartarus.handler.BaseHandler;
import io.github.tsaqifammar.tartarus.handler.HandlerBinder;
import io.github.tsaqifammar.tartarus.model.BaseRequest;
import io.github.tsaqifammar.tartarus.server.Server;
import lombok.Data;
import lombok.EqualsAndHashCode;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import spark.Request;
import spark.Response;
import spark.Service;

import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

class HandlerBinderTest {

    private Service mockService;

    @BeforeEach
    void setup() {
        mockService = mock(Service.class);

        // Inject mock Service into Server via reflection
        try {
            var field = Server.class.getDeclaredField("http");
            field.setAccessible(true);
            field.set(null, mockService);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Data
    @EqualsAndHashCode(callSuper = true)
    static class HelloRequest extends BaseRequest {
        private String name;
    }

    @Route(method = HttpMethod.GET, path = "/hello")
    static class HelloHandler extends BaseHandler<HelloRequest, String> {

        @Override
        protected void validate(HelloRequest request) {}

        @Override
        protected String process(HelloRequest request) {
            return "Hello, " + (request.getName() != null ? request.getName() : "World");
        }
    }

    @Test
    void testBindRegistersRoute() {
        HelloHandler handler = new HelloHandler();

        HandlerBinder.bind(handler);

        // Verify Service.get() was called with correct path
        verify(mockService).get(eq("/hello"), any());
    }

    @Test
    void testIsValidPath() {
        assertTrue(HandlerBinder.isValidPath("/abc"));
    }

    @Test
    void testInvokeReturnsJson() throws Exception {
        HelloHandler handler = new HelloHandler();

        Request mockReq = mock(Request.class);
        Response mockRes = mock(Response.class);

        when(mockReq.body()).thenReturn("{\"name\":\"Anya\"}");
        when(mockReq.params()).thenReturn(Map.of());

        var method = HandlerBinder.class.getDeclaredMethod("invoke", BaseHandler.class, Request.class, Response.class);
        method.setAccessible(true);

        var result = (String) method.invoke(null, handler, mockReq, mockRes);

        assertEquals("{\"success\":true,\"data\":\"Hello, Anya\"}", result);
        verify(mockRes).type("application/json");
    }
}
