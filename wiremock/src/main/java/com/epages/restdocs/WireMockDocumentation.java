package com.epages.restdocs;

import static org.springframework.restdocs.curl.CurlDocumentation.curlRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpRequest;
import static org.springframework.restdocs.http.HttpDocumentation.httpResponse;
import static org.springframework.restdocs.mockmvc.MockMvcRestDocumentation.document;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessRequest;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.preprocessResponse;
import static org.springframework.restdocs.operation.preprocess.Preprocessors.prettyPrint;

import org.springframework.restdocs.mockmvc.MockMvcRestDocumentation;
import org.springframework.restdocs.mockmvc.RestDocumentationResultHandler;
import org.springframework.restdocs.snippet.Snippet;

/**
 * Wrapper around the static API from {@link MockMvcRestDocumentation}, to
 * integrate generation of WireMock stubs.
 */
public final class WireMockDocumentation {

	private WireMockDocumentation() {
	}

	/**
	 * Factory method for adding wiremock to all REST documentation tasks
	 * 
	 * @return a Mock MVC {@code ResultHandler} that will produce the
	 *         documentation
	 */
	public static RestDocumentationResultHandler documentWithWireMock() {
		return documentWithWireMock("{method-name}_{step}");
	}

	/**
	 * Factory method for producing REST-documentation including wiremock stubs.
	 * 
	 * @see MockMvcRestDocumentation#document(String, Snippet...)
	 * @param identifier
	 *            an identifier for the API call that is being documented
	 * @param snippets
	 *            the snippets
	 * @return a Mock MVC {@code ResultHandler} that will produce the
	 *         documentation
	 */
	public static RestDocumentationResultHandler documentWithWireMock(String identifier, Snippet... snippets) {
		return documentWithWireMock(identifier).snippets(snippets);
	}

	/**
	 * Factory method for producing REST-documentation including wiremock stubs.
	 * 
	 * @see MockMvcRestDocumentation#document(String,
	 *      org.springframework.restdocs.operation.preprocess.OperationRequestPreprocessor,
	 *      org.springframework.restdocs.operation.preprocess.OperationResponsePreprocessor,
	 *      Snippet...)
	 * @param identifier
	 *            an identifier for the API call that is being documented
	 * @return a Mock MVC {@code ResultHandler} that will produce the
	 *         documentation
	 */
	public static RestDocumentationResultHandler documentWithWireMock(String identifier) {
		return document(identifier, preprocessRequest(prettyPrint()), preprocessResponse(prettyPrint()))
				.snippets(curlRequest(), httpRequest(), httpResponse(), wiremockJson());
	}

	/**
	 * Returns a json {@code Snippet} that will generate the wiremock stub from the API operation.
	 *
	 * @return the json snippet
	 */
	public static Snippet wiremockJson() {
		return new WireMockJsonSnippet();
	}

}
