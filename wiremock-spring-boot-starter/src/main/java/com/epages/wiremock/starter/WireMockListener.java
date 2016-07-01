package com.epages.wiremock.starter;

import static com.github.tomakehurst.wiremock.core.WireMockConfiguration.wireMockConfig;

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
import org.springframework.test.context.TestExecutionListener;
import org.springframework.test.context.support.AbstractTestExecutionListener;
import org.springframework.test.util.ReflectionTestUtils;

import com.github.tomakehurst.wiremock.WireMockServer;
import com.github.tomakehurst.wiremock.common.ClasspathFileSource;
import com.github.tomakehurst.wiremock.core.WireMockConfiguration;
import com.github.tomakehurst.wiremock.standalone.JsonFileMappingsSource;

class WireMockListener implements TestExecutionListener, Ordered {

	private static final Logger log = LoggerFactory.getLogger(WireMockListener.class);

	private WireMockStrategyFactory factory;
	
	public WireMockListener() {
		this.factory = new WireMockStrategyFactory();
	}
	
	@Override
	public void beforeTestClass(TestContext testContext) throws Exception {
		factory.get(testContext).beforeTestClass(testContext);
	}

	@Override
	public void prepareTestInstance(TestContext testContext) throws Exception {
		factory.get(testContext).prepareTestInstance(testContext);
	}

	@Override
	public void beforeTestMethod(TestContext testContext) throws Exception {
		factory.get(testContext).beforeTestMethod(testContext);
	}

	@Override
	public void afterTestMethod(TestContext testContext) throws Exception {
		factory.get(testContext).afterTestMethod(testContext);
	}

	@Override
	public void afterTestClass(TestContext testContext) throws Exception {
		factory.get(testContext).afterTestClass(testContext);
	}

	@Override
	public int getOrder() {
		return Ordered.HIGHEST_PRECEDENCE;
	}

	static class WireMockStrategyFactory {
		private WireMockStrategy strategy;

		WireMockStrategy get(TestContext testContext) {
			if(strategy != null) {
				return strategy;
			}

			synchronized (this) {
				if(strategy != null) {
					return strategy;
				}

				WireMockTest classAnnotation = testContext.getTestClass().getAnnotation(WireMockTest.class);
				if (classAnnotation == null) {
					strategy = new NullStrategy();
				} else {
					strategy = new WireMockStrategyImpl(classAnnotation);
				}
			}
			return strategy;
		}
	}

	interface WireMockStrategy extends TestExecutionListener {
	}

	/**
	 * No-Op implementation, if there is no WireMockTest annotation on the test.
	 */
	static class NullStrategy extends AbstractTestExecutionListener implements WireMockStrategy {
	}

	/**
	 * Actual WireMock implementation.
	 */
	static class WireMockStrategyImpl extends AbstractTestExecutionListener implements WireMockStrategy {

		private WireMockServer server;

		private int port;

		private final WireMockTest classAnnotation;

		/**
		 * @param classAnnotation WireMock test annotation (must not be null).
		 */
		public WireMockStrategyImpl(WireMockTest classAnnotation) {
			this.classAnnotation = classAnnotation;
			start();
		}

		@Override
		public void prepareTestInstance(TestContext testContext) throws Exception {
				ArrayList<String> properties = new ArrayList<>();
				properties.add("wiremock.port=" + server.port());
				properties.add("ribbon.eureka.enabled=false");
				for (String service : classAnnotation.ribbonServices()) {
					properties.add(service + ".ribbon.listOfServers=localhost:" + server.port());
				}

				addPropertySourceProperties(testContext, properties.toArray(new String[0]));
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

		static class MergedContextConfigurationProperties {

			private final MergedContextConfiguration configuration;

			MergedContextConfigurationProperties(MergedContextConfiguration configuration) {
				this.configuration = configuration;
			}

			void add(String[] properties, String... additional) {
				Set<String> merged = new LinkedHashSet<>(
						Arrays.asList(this.configuration.getPropertySourceProperties()));
				merged.addAll(Arrays.asList(properties));
				merged.addAll(Arrays.asList(additional));
				ReflectionTestUtils.setField(this.configuration, "propertySourceProperties",
						merged.toArray(new String[merged.size()]));
			}

		}

		@Override
		public void beforeTestClass(TestContext testContext) throws Exception {

			ConfigurableApplicationContext applicationContext = (ConfigurableApplicationContext) testContext
					.getApplicationContext();

			applicationContext.getBeanFactory().registerSingleton("wireMockServer", server);
		}

		@Override
		public void beforeTestMethod(TestContext testContext) throws Exception {
			WireMockTest methodAnnotation = testContext.getTestMethod().getAnnotation(WireMockTest.class);
			if (methodAnnotation == null || methodAnnotation.stubPath() == null) {
				return;
			}
			String stubPath;
			if(this.classAnnotation.stubPath() != null) {
				stubPath = this.classAnnotation.stubPath() + "/" + methodAnnotation.stubPath();
			} else {
				stubPath = methodAnnotation.stubPath();
			}
			this.server.resetMappings();
			this.server.loadMappingsUsing(new JsonFileMappingsSource(new ClasspathFileSource(stubPath)));
		}

		@Override
		public void afterTestMethod(TestContext testContext) throws Exception {
			this.server.resetToDefaultMappings();
		}

		@Override
		public void afterTestClass(TestContext testContext) throws Exception {
			stop();
		}

		// ---------------------- server lifecycle ---------------------

		private void start() {
			WireMockConfiguration cfg = wireMockConfig();
			if(this.classAnnotation.port() > 0) {
				cfg.port(this.classAnnotation.port());
			} else {
				cfg.dynamicPort();
			}

			if (this.classAnnotation.stubPath() != null && !"".equals(this.classAnnotation.stubPath())) {
				cfg.fileSource(new ClasspathFileSource(this.classAnnotation.stubPath()));
			}

			this.server = new WireMockServer(cfg);
			log.info("Starting WireMock server");
			this.server.start();
			this.port = server.port();
			log.info("WireMock server running on port " + port);
		}

		private void stop() {
			log.info("Stopping WireMock server");
			server.stop();
		}

		// ---------------------- server lifecycle ---------------------

	}

}
