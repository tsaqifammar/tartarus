package io.github.tsaqifammar.tartarus.handler;

import io.github.tsaqifammar.tartarus.annotation.EntryPoint;
import io.github.tsaqifammar.tartarus.model.BaseRequest;
import io.github.tsaqifammar.tartarus.model.Response;
import io.github.tsaqifammar.tartarus.model.error.BusinessException;
import lombok.extern.slf4j.Slf4j;
import org.apache.commons.lang3.time.StopWatch;

import java.lang.reflect.ParameterizedType;

@Slf4j
public abstract class BaseHandler<F extends BaseRequest, T> {

    private final String handlerName;
    private final Class<F> requestType;

    protected abstract void validate(F request);
    protected abstract T process(F request);

    @SuppressWarnings("unchecked")
    protected BaseHandler() {

        var type = (ParameterizedType) getClass().getGenericSuperclass();
        this.requestType = (Class<F>) type.getActualTypeArguments()[0];
        this.handlerName = getClass().getSimpleName();
    }

    @EntryPoint
    public Response<T> handle(F request) {

        var stopWatch = new StopWatch();
        stopWatch.start();

        try {

            validate(request);

            var data = process(request);
            log.info(
                    "{} successfully processed request: {}, with response data: {}",
                    handlerName, request, data
            );
            return Response.ok(data);

        } catch (BusinessException ex) {

            log.error(
                    "{} encountered business exception when handling request: {}, caused by: {}",
                    handlerName, request, ex.getMessage(), ex
            );
            return handleBusinessException(ex);

        } catch (Exception ex) {

            log.error(
                    "{} encountered exception when handling request: {}, caused by: {}",
                    handlerName, request, ex.getMessage(), ex
            );
            return handleException(ex);

        } finally {

            stopWatch.stop();
            log.info("{} processed the request in {} ms", handlerName, stopWatch.getTime());

        }
    }

    protected Response<T> handleBusinessException(BusinessException ex) {
        return Response.bad(ex);
    }

    protected Response<T> handleException(Exception ex) {
        return Response.bad(ex);
    }

    protected Class<F> getRequestType() {
        return requestType;
    }
}
