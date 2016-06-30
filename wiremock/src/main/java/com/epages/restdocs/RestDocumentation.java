package com.epages.restdocs;

import org.springframework.restdocs.JUnitRestDocumentation;

/**
 * Convenience helper for running Spring REST Docs in JUnit.
 * 
 */
public final class RestDocumentation {

	// utility class.
	private RestDocumentation() {
	}

	/**
	 * @return JUnit {@link TestRule} for Spring REST Docs, preconfigured for
	 *         writing to build/generated-snippets.
	 */
	public static JUnitRestDocumentation usingGradleDir() {
		return new JUnitRestDocumentation("build/generated-snippets");
	}

	/**
	 * @return JUnit {@link TestRule} for Spring REST Docs, preconfigured for
	 *         writing to target/generated-snippets.
	 */
	public static JUnitRestDocumentation usingMavenDir() {
		return new JUnitRestDocumentation("target/generated-snippets");
	}

}
