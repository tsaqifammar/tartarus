package io.github.tsaqifammar.tartarus.model.error;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@Getter
@RequiredArgsConstructor
public enum BusinessError {

    INTERNAL_SERVER_ERROR("0", "Internal server error"),
    IM_A_TEAPOT("1", "I'm a teapot");

    private final String code;
    private final String message;

    public BaseError toBaseError() {
        return new BaseError(this);
    }
}
