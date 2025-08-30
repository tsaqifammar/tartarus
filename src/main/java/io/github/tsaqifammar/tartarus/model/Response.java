package io.github.tsaqifammar.tartarus.model;

import io.github.tsaqifammar.tartarus.model.error.BaseError;
import io.github.tsaqifammar.tartarus.model.error.BusinessException;

public class Response<T> {

    private boolean success;
    private BaseError error;

    private T data;

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

        var error = ex.getErrorsWithFallback().toBaseError();
        return new Response<>(null, false, error);
    }

    private Response(T data, boolean success, BaseError error) {
        this.data = data;
        this.success = success;
        this.error = error;
    }
}
