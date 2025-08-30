package io.github.tsaqifammar.tartarus.model;

import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;
import spark.QueryParamsMap;

import java.util.Map;

@Setter
@NoArgsConstructor
@SuperBuilder
public class BaseRequest {

    private Map<String, String> pathParams;
    private QueryParamsMap queryParams;

}
