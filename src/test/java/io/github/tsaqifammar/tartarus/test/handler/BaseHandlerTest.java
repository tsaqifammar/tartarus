package io.github.tsaqifammar.tartarus.test.handler;

import io.github.tsaqifammar.tartarus.handler.BaseHandler;
import io.github.tsaqifammar.tartarus.model.BaseRequest;
import io.github.tsaqifammar.tartarus.model.error.BusinessException;
import lombok.RequiredArgsConstructor;
import lombok.ToString;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BaseHandlerTest {

    private TestHandler handler;

    @ToString
    @RequiredArgsConstructor
    static class TestRequest extends BaseRequest {
        private final String value;
    }

    static class TestHandler extends BaseHandler<TestRequest, String> {

        private boolean throwBusinessException = false;
        private boolean throwException = false;

        @Override
        protected void validate(TestRequest request) {
            if ("invalid".equals(request.value)) {
                throw new BusinessException("Invalid request");
            }
        }

        @Override
        protected String process(TestRequest request) {
            if (throwBusinessException) {
                throw new BusinessException("Business error");
            }
            if (throwException) {
                throw new RuntimeException("Generic error");
            }
            return "Processed: " + request.value;
        }
    }

    @BeforeEach
    void setUp() {
        handler = new TestHandler();
    }

    @Test
    void testHandleSuccess() {
        var request = new TestRequest("hello");
        var response = handler.handle(request);

        assertTrue(response.isSuccess());
        assertEquals("Processed: hello", response.getData());
    }

    @Test
    void testHandleBusinessExceptionThrownInValidate() {
        var request = new TestRequest("invalid");
        var response = handler.handle(request);

        assertFalse(response.isSuccess());
        assertNotNull(response.getError());
        assertEquals("Invalid request", response.getError().getMessage());
    }

    @Test
    void testHandleBusinessExceptionThrownInProcess() {
        var request = new TestRequest("trigger");
        handler.throwBusinessException = true;

        var response = handler.handle(request);

        assertFalse(response.isSuccess());
        assertNotNull(response.getError());
        assertEquals("Business error", response.getError().getMessage());
    }

    @Test
    void testHandleGenericException() {
        var request = new TestRequest("trigger");
        handler.throwException = true;

        var response = handler.handle(request);

        assertFalse(response.isSuccess());
        assertNotNull(response.getError());
        assertEquals("Generic error", response.getError().getMessage());
    }
}
