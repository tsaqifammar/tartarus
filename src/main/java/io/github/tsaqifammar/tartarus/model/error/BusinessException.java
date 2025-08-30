package io.github.tsaqifammar.tartarus.model.error;

import spark.utils.StringUtils;

import java.util.Arrays;
import java.util.Optional;

public class BusinessException extends RuntimeException {

    private final BaseError error;

    public BusinessException() {
        this(BusinessError.INTERNAL_SERVER_ERROR.toBaseError());
    }

    public BusinessException(String message) {
        this(constructBaseError(message));
    }

    public BusinessException(BaseError error) {
        super(constructMessage(error));
        this.error = error;
    }

    public BaseError getErrorsWithFallback() {

        return Optional.ofNullable(this.error)
                .orElse(BusinessError.INTERNAL_SERVER_ERROR.toBaseError());
    }

    private static String constructMessage(BaseError error) {

        return "BusinessException: " +
                Optional.ofNullable(error)
                        .map(err -> String.format("[%s: %s]", err.getCode(), err.getMessage()))
                        .orElse("cannot determine the cause.");
    }

    private static BaseError constructBaseError(String message) {

        if (StringUtils.isEmpty(message)) {
            return BusinessError.INTERNAL_SERVER_ERROR.toBaseError();
        }

        var cleanedRawMessage = message.replaceAll("[^A-Za-z0-9]", " ");
        var code = String.join(
                "_",
                Arrays.stream(cleanedRawMessage.trim().split("\\s+"))
                        .filter(s -> !s.isEmpty())
                        .map(String::toUpperCase)
                        .toArray(String[]::new)
        );

        return BaseError.builder()
                .code(code)
                .message(message)
                .build();
    }
}
