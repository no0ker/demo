package com.example.demo.config;

import lombok.Data;
import org.apache.commons.text.StringEscapeUtils;
import org.springframework.core.MethodParameter;
import org.springframework.data.r2dbc.query.Criteria;
import org.springframework.util.MultiValueMap;
import org.springframework.web.reactive.BindingContext;
import org.springframework.web.reactive.result.method.HandlerMethodArgumentResolver;
import org.springframework.web.server.ServerWebExchange;
import reactor.core.publisher.Mono;

import java.util.ArrayList;
import java.util.List;
import java.util.Objects;
import java.util.regex.Matcher;
import java.util.regex.Pattern;

public class CriteriaResolver implements HandlerMethodArgumentResolver {
    private static final String FILTER_KEY = "filter";
    private static final Pattern FILTER_PATTERN = Pattern.compile("([^:]+):([^:]+):(.+)");

    @Override
    public boolean supportsParameter(MethodParameter methodParameter) {
        return Criteria.class.equals(methodParameter.getParameterType());
    }

    @Override
    public Mono<Object> resolveArgument(MethodParameter methodParameter,
                                        BindingContext bindingContext,
                                        ServerWebExchange serverWebExchange) {

        List<ParseResult> parsedResult = parse(serverWebExchange);
        Criteria result = convert(parsedResult);

        return Mono.just(result);
    }


    private List<ParseResult> parse(ServerWebExchange serverWebExchange) {

        MultiValueMap<String, String> arguments = serverWebExchange.getRequest().getQueryParams();
        List<String> filterValues = arguments.get(FILTER_KEY);

        List<ParseResult> result = new ArrayList<>();

        for (String filterValue : filterValues) {
            Matcher matcher = FILTER_PATTERN.matcher(filterValue);
            if (matcher.find() && matcher.groupCount() == 3) {
                ParseResult parseResult = new ParseResult(
                        matcher.group(1), matcher.group(2), matcher.group(3));
                result.add(parseResult);
            }
        }

        return result;
    }

    @Data
    private static class ParseResult {
        public ParseResult(String field, String operator, String value) {
            this.field = field;
            this.operator = operator;
            this.value = StringEscapeUtils.unescapeHtml4(value);
        }

        private String field;
        private String operator;
        private String value;
    }

    private Criteria convert(List<ParseResult> parsedResults) {
        Criteria result = null;

        for (ParseResult parsedResult : parsedResults) {
            if ("eq".equals(parsedResult.getOperator())) {
                if (Objects.isNull(result)) {
                    result = Criteria.where(parsedResult.getField())
                            .is(parsedResult.getValue());
                } else {
                    result = result.and(parsedResult.getField())
                            .is(parsedResult.getValue());
                }
            }
        }

        return result;
    }
}
