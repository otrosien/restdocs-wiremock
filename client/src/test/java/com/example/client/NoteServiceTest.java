package com.example.client;

import static org.junit.Assert.assertEquals;

import org.junit.FixMethodOrder;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.junit.runners.MethodSorters;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;
import org.springframework.web.client.HttpClientErrorException;

import com.epages.wiremock.starter.WireMockTest;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { ClientApplication.class })
@ActiveProfiles("test")
@WireMockTest(stubPath = "wiremock/restdocs-server")
@FixMethodOrder(MethodSorters.NAME_ASCENDING)
public class NoteServiceTest {

	@Autowired
	private NoteService noteService;

	@Test
	@WireMockTest(stubPath = "wiremock/restdocs-server/mappings/note-get-example")
	public void should_1_use_dedicated_wiremock_stub() {
		assertEquals("REST maturity model", noteService.getNote("1").getTitle());
	}

	@Test(expected = HttpClientErrorException.class)
	@WireMockTest(stubPath = "wiremock/restdocs-server/mappings/note-badrequest-example")
	public void should_2_use_different_wiremock_stub() {
		noteService.getNote("xy");
	}

	@Test
	public void should_3_use_default_wiremock_stubs() {
		assertEquals("REST maturity model", noteService.getNote("1").getTitle());
	}

}
