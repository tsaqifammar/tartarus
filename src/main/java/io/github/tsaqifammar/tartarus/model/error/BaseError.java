package io.github.tsaqifammar.tartarus.model.error;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
public class BaseError {

    private final String code;
    private final String message;

    public BaseError(BusinessError error) {
        this.code = error.getCode();
        this.message = error.getMessage();
    }

    public BaseError(Exception ex) {
        this.code = BusinessError.INTERNAL_SERVER_ERROR.getCode();
        this.message = ex.getMessage();
    }
}
