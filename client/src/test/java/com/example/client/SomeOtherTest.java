package com.example.client;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.SpringApplicationConfiguration;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringJUnit4ClassRunner;

import com.github.tomakehurst.wiremock.WireMockServer;

@RunWith(SpringJUnit4ClassRunner.class)
@SpringApplicationConfiguration(classes = { ClientApplication.class })
@ActiveProfiles("test")
public class SomeOtherTest {

	@Autowired(required = false)
	private WireMockServer server;

	@Test
	public void should_not_have_wiremock_server() {
		Assert.assertNull(server);
	}
}
