package com.epages.restdocs;

import org.springframework.restdocs.snippet.Snippet;

/**
 * Wrapper around the static API from {@link MockMvcRestDocumentation}, to
 * integrate generation of WireMock stubs. Most of the static API was deprecated 
 * in 0.6.x and removed in 0.7.0.
 */
public abstract class WireMockDocumentation {

	/**
	 * Returns a json {@code Snippet} that will generate the WireMock stub from
	 * the API operation.
	 *
	 * @see {@see MockMvcRestDocumentation}
	 * @return the json snippet
	 */
	public static Snippet wiremockJson() {
		return new WireMockJsonSnippet();
	}

}
