package com.epages.wiremock.starter;

import org.junit.Assert;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.junit4.SpringRunner;

import com.github.tomakehurst.wiremock.WireMockServer;

@RunWith(SpringRunner.class)
@SpringBootTest(classes=TestApp.class)
@ActiveProfiles("test")
public class WithoutWiremockTest {

	@Autowired(required = false)
	private WireMockServer server;

	@Test
	public void should_not_have_wiremock_server() {
		Assert.assertNull(server);
	}
	
}
