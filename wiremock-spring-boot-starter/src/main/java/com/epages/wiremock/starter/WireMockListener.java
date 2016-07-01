package com.epages.wiremock.starter;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

import java.io.IOException;
import java.net.ServerSocket;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.LinkedHashSet;
import java.util.Set;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.Ordered;
import org.springframework.test.context.MergedContextConfiguration;
import org.springframework.test.context.TestContext;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.util.ReflectionTestUtils;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.client.WireMock;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.standalone.JsonFileMappingsLoader;

class WireMockListener extends AbstractTestExecutionListener {

	private static final Logger log = LoggerFactory.getLogger(WireMockListener.class);

	int port;

	private WireMockServer server;

	public WireMockListener() throws IOException {
		port = getFreeServerPort();
	}

	@Override
	public void prepareTestInstance(TestContext testContext) throws Exception {
		WireMockTest annotation = testContext.getTestClass().getAnnotation(WireMockTest.class);

		if (annotation != null) {
			ArrayList<String> properties = new ArrayList<>();
			properties.add("wiremock.port=" + port);
			properties.add("ribbon.eureka.enabled=false");
			for (String service : annotation.ribbonServices()) {
				properties.add(service + ".ribbon.listOfServers=localhost:" + port);
			}

			addPropertySourceProperties(testContext, properties.toArray(new String[0]));
		}
	}

	private void addPropertySourceProperties(TestContext testContext, String[] properties) {
		try {
			MergedContextConfiguration configuration = (MergedContextConfiguration) ReflectionTestUtils
					.getField(testContext, "mergedContextConfiguration");
			new MergedContextConfigurationProperties(configuration).add(properties);
		} catch (RuntimeException ex) {
			throw ex;
		} catch (Exception ex) {
			throw new IllegalStateException(ex);
		}
	}

	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		WireMockTest annotation = testContext.getTestClass().getAnnotation(WireMockTest.class);
		if (annotation == null) {
			return;
		}

		WireMockConfiguration cfg = wireMockConfig().port(port);
		if (annotation.stubPath() != null && !"".equals(annotation.stubPath())) {
			cfg.fileSource(new ClasspathFileSource(annotation.stubPath()));
		}
		server = new WireMockServer(cfg);

		ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) testContext
				.getApplicationContext();

		applicationContext.getBeanFactory().registerSingleton("wireMockServer", server);
		start();
	}

	private void start() {
		if (server != null) {
			log.info("Starting WireMock server on port " + port);
			server.start();
			WireMock.configureFor(server.port());
		}
	}

	private void stop() {
		if (server != null) {
			log.info("Stopping WireMock server");
			server.stop();
		}
	}

	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		WireMockTest annotation = testContext.getTestMethod().getAnnotation(WireMockTest.class);
		if (annotation == null || annotation.stubPath() == null) {
			return;
		}
		server.loadMappingsUsing(new JsonFileMappingsLoader(new ClasspathFileSource(annotation.stubPath())));
	}

	@Override
	public void afterTestMethod(TestContext testContext) throws Exception {
		if (server != null) {
			server.resetToDefaultMappings();
		}
	}

	@Override
	public void afterTestClass(TestContext testContext) throws Exception {
		stop();
	}

	private int getFreeServerPort() throws IOException {
		try (ServerSocket serverSocket = new ServerSocket(0)) {
			return serverSocket.getLocalPort();
		} catch (IOException e) {
			log.error("Socket Exception while opening socket");
			throw e;
		}
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	class MergedContextConfigurationProperties {

		private final MergedContextConfiguration configuration;

		MergedContextConfigurationProperties(MergedContextConfiguration configuration) {
			this.configuration = configuration;
		}

		public void add(String[] properties, String... additional) {
			Set<String> merged = new LinkedHashSet<String>(
					(Arrays.asList(this.configuration.getPropertySourceProperties())));
			merged.addAll(Arrays.asList(properties));
			merged.addAll(Arrays.asList(additional));
			ReflectionTestUtils.setField(this.configuration, "propertySourceProperties",
					merged.toArray(new String[merged.size()]));
		}

	}

}
