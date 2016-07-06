package com.epages.restdocs;

import org.junit.Assert;
import org.junit.Test;
import org.springframework.restdocs.JUnitRestDocumentation;

public class RestDocumentationTest {

	@Test
	public void should_create_restdocumentation() {
		JUnitRestDocumentation documentation1 = RestDocumentation.usingGradleDir();
		Assert.assertNotNull(documentation1);
		JUnitRestDocumentation documentation2 = RestDocumentation.usingMavenDir();
		Assert.assertNotNull(documentation2);
	}
}
