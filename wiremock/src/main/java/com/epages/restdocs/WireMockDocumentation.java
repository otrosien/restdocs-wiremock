package com.epages.restdocs;

import static org.springframework.restdocs.curl.CurlDocumentation.curlRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.snippet.Snippet;

public final class WireMockDocumentation {

    private WireMockDocumentation() {}

    public static RestDocumentationResultHandler documentWithWireMock() {
        return documentWithWireMock("{method-name}_{step}");
    }

    public static RestDocumentationResultHandler documentWithWireMock(String snippetName, Snippet... snippets) {
        return documentWithWireMock(snippetName).snippets(snippets);
    }

    public static RestDocumentationResultHandler documentWithWireMock(String snippetName) {
        return document(snippetName,
                preprocessRequest(prettyPrint()),
                preprocessResponse(prettyPrint())).snippets(
                    curlRequest(), 
                    httpRequest(),
                    httpResponse(),
                    wiremockJson()
                );
    }

    public static Snippet wiremockJson() {
            return new WireMockJsonSnippet();
    }

}
