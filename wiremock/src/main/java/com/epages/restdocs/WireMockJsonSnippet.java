package com.epages.restdocs;

import java.io.IOException;
import java.io.Writer;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.restdocs.RestDocumentationContext;
import org.springframework.restdocs.operation.Operation;
import org.springframework.restdocs.operation.OperationRequest;
import org.springframework.restdocs.operation.OperationResponse;
import org.springframework.restdocs.snippet.RestDocumentationContextPlaceholderResolver;
import org.springframework.restdocs.snippet.Snippet;
import org.springframework.restdocs.snippet.StandardWriterResolver;
import org.springframework.restdocs.snippet.WriterResolver;
import org.springframework.restdocs.templates.TemplateFormat;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.SerializationFeature;
import com.google.common.collect.ImmutableMap;

final class WireMockJsonSnippet implements Snippet {

    private static final String SNIPPET_NAME = "wiremock-stub";

    private static final TemplateFormat TEMPLATE_FORMAT = new TemplateFormat() {

        @Override
        public String getId() {
            return "json";
        }

        @Override
        public String getFileExtension() {
            return "json";
        }
    };

    protected WireMockJsonSnippet() {}

    @Override
    public void document(Operation operation) throws IOException {
        RestDocumentationContext context = (RestDocumentationContext) operation
                .getAttributes().get(RestDocumentationContext.class.getName());
        WriterResolver writerResolver = new StandardWriterResolver(
                new RestDocumentationContextPlaceholderResolver(context), "UTF-8", TEMPLATE_FORMAT);
        try (Writer writer = writerResolver.resolve(operation.getName(), SNIPPET_NAME, context)) {
            writer.append(toJsonString(operation));
        }
    }

    private String toJsonString(Operation operation) throws JsonProcessingException {
        return new ObjectMapper().enable(SerializationFeature.INDENT_OUTPUT).writeValueAsString(createModel(operation));
    }

    protected ImmutableMap<Object, Object> createModel(Operation operation) {
        OperationResponse response = operation.getResponse();

        ImmutableMap.Builder<Object, Object> requestBuilder = ImmutableMap.builder()
                .put("method", operation.getRequest().getMethod())
                .put("urlPattern", operation.getRequest().getUri().getRawPath());

        ImmutableMap.Builder<Object, Object> responseBuilder = ImmutableMap.builder()
                .put("status", response.getStatus().value())
                .put("headers", response.getHeaders())
                .put("body", responseBody(response));

        Map<Object, Object> queryParams = queryParams(operation);
        if (!queryParams.isEmpty()) {
            requestBuilder.put("queryParameters", queryParams);
        }

        Map<Object, Object> headers = requestHeaders(operation.getRequest());
        if (!headers.isEmpty()) {
            requestBuilder.put("headers",headers);
        }

        return ImmutableMap.builder()
                .put("request", requestBuilder.build())
                .put("response", responseBuilder.build())
                .build();
    }

    private Map<Object, Object> queryParams(Operation operation) {
        return operation.getRequest().getParameters().entrySet().stream()
                .collect(Collectors.toMap(e -> e.getKey().toString(), e -> ImmutableMap.of("equalTo", e.getValue().get(0))));
    }

    private String responseBody(OperationResponse response) {
        return response.getContentAsString();
    }

    private Map<Object, Object> requestHeaders(OperationRequest request) {
        return request.getHeaders().entrySet().stream()
                .filter(e -> "content-type".equalsIgnoreCase(e.getKey()) || "accept".equalsIgnoreCase(e.getKey()))
                .collect(Collectors.toMap(e -> e.getKey().toString(), e -> ImmutableMap.of("equalTo", e.getValue().get(0))));

    }

}