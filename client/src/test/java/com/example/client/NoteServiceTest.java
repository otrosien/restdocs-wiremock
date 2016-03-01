package com.example.client;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;
import static org.junit.Assert.assertEquals;

import java.net.URI;

import org.junit.ClassRule;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.context.annotation.Profile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.github.tomakehurst.wiremock.junit.WireMockClassRule;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = {ClientApplication.class, NoteServiceTest.MockConfig.class})
@ActiveProfiles("test")
public class NoteServiceTest {

	@ClassRule
	public static WireMockClassRule server = new WireMockClassRule(wireMockConfig()
			.fileSource(new ClasspathFileSource("wiremock/spring-restdocs-server")).dynamicPort());

	@Autowired
	private NoteService noteService;

	@Test
	public void test() {
		assertEquals("REST maturity model", noteService.getNote("6").getTitle());
	}

	@Configuration
	@Profile("test")
	static class MockConfig {

		@Bean
		public NoteServiceConfiguration noteServiceConfiguration() {
			return new NoteServiceConfiguration() {
				@Override
				public URI getBaseurl() {
					return URI.create("http://localhost:" + NoteServiceTest.server.port());
				}
			};
		}
	}
}
