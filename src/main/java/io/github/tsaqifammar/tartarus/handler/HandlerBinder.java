package io.github.tsaqifammar.tartarus.handler;

import com.google.gson.Gson;
import io.github.tsaqifammar.tartarus.annotation.EntryPoint;
import io.github.tsaqifammar.tartarus.annotation.Route;
import io.github.tsaqifammar.tartarus.model.BaseRequest;
import io.github.tsaqifammar.tartarus.server.Server;
import spark.Request;

import java.net.URI;
import java.util.Arrays;
import java.util.Objects;

public class HandlerBinder {

    private static final Gson gson = new Gson();
    private HandlerBinder() {}

    public static <F extends BaseRequest, T> void bind(BaseHandler<F, T> handler) {

        var route = handler.getClass().getAnnotation(Route.class);
        if (Objects.isNull(route)) {
            return;
        }
        validateRoute(route);

        var http = Server.getInstance();

        switch (route.method()) {
            case GET -> http.get(route.path(), (req, res) -> invoke(handler, req, res));
            case POST -> http.post(route.path(), (req, res) -> invoke(handler, req, res));
            case PUT -> http.put(route.path(), (req, res) -> invoke(handler, req, res));
            case DELETE -> http.delete(route.path(), (req, res) -> invoke(handler, req, res));
            default -> throw new UnsupportedOperationException("Unsupported method: " + route.method());
        }
    }

    private static void validateRoute(Route route) {

        var invalidRouteException = new IllegalStateException(
                "A handler must be decorated with a valid @Route annotation"
        );

        if (!isValidPath(route.path())) {
            throw invalidRouteException;
        }

        if (Objects.isNull(route.method())) {
            throw invalidRouteException;
        }
    }

    public static boolean isValidPath(String path) {
        try {
            new URI(null, null, path, null);
            return true;
        } catch (Exception ignored) {}

        return false;
    }

    @SuppressWarnings("unchecked")
    private static <F extends BaseRequest, T> String invoke(BaseHandler<F, T> handler, spark.Request req, spark.Response res) {

        var handle = Arrays.stream(handler.getClass().getMethods())
                .filter(m -> m.isAnnotationPresent(EntryPoint.class))
                .findFirst()
                .orElse(null);

        if (Objects.isNull(handle)) {
            throw new IllegalStateException("BaseHandler was modified?");
        }

        try {


            var request = bindRequest(req, handler.getRequestType());
            var response = handle.invoke(handler, request);

            res.type("application/json");
            return gson.toJson(response);

        } catch (Exception e) {
            throw new RuntimeException("Failed to invoke route handler: " + handle, e);
        }
    }

    private static <T extends BaseRequest> T bindRequest(Request req, Class<T> clazz) {
        try {

            T instance = gson.fromJson(req.body(), clazz);
            if (instance == null) {
                instance = clazz.getDeclaredConstructor().newInstance();
            }

            instance.setPathParams(req.params());
            instance.setQueryParams(req.queryMap());

            return instance;

        } catch (Exception e) {
            throw new RuntimeException("Failed to bind request", e);
        }
    }
}
