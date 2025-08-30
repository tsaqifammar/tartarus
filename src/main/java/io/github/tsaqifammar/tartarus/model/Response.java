package io.github.tsaqifammar.tartarus.model;

import io.github.tsaqifammar.tartarus.model.error.BaseError;
import io.github.tsaqifammar.tartarus.model.error.BusinessException;
import lombok.Getter;

@Getter
public class Response<T> {

    private final boolean success;
    private final BaseError error;

    private final T data;

    public static <T> Response<T> ok() {
        return new Response<>(null, true, null);
    }

    public static <T> Response<T> ok(T data) {
        return new Response<>(data, true, null);
    }

    public static <T> Response<T> bad(Exception ex) {
        return new Response<>(null, false, new BaseError(ex));
    }

    public static <T> Response<T> bad(BusinessException ex) {

        var error = ex.getErrorsWithFallback();
        return new Response<>(null, false, error);
    }

    public Response(T data, boolean success, BaseError error) {
        this.data = data;
        this.success = success;
        this.error = error;
    }
}
