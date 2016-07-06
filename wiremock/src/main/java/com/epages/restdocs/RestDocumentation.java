package com.epages.restdocs;

import org.junit.rules.TestRule;
import org.springframework.restdocs.JUnitRestDocumentation;

/**
 * Convenience helper for running Spring REST Docs in JUnit.
 * 
 */
public abstract class RestDocumentation {

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
