package io.github.tsaqifammar.tartarus.model.error;

import java.util.Optional;

public class BusinessException extends RuntimeException {

    private final BusinessError error;

    public BusinessException() {
        this(BusinessError.INTERNAL_SERVER_ERROR);
    }

    public BusinessException(BusinessError error) {
        super(constructMessage(error));
        this.error = error;
    }

    public BusinessError getErrorsWithFallback() {

        return Optional.ofNullable(this.error)
                .orElse(BusinessError.INTERNAL_SERVER_ERROR);
    }

    private static String constructMessage(BusinessError error) {

        return "BusinessException: " +
                Optional.ofNullable(error)
                        .map(err -> String.format("[%s: %s]", err.getCode(), err.getMessage()))
                        .orElse("cannot determine the cause.");
    }
}
