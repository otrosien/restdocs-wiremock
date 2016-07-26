package com.epages.wiremock.starter;

import org.springframework.boot.context.properties.ConfigurationProperties;

@ConfigurationProperties(prefix="wiremock")
public class WireMockProperties {

	/**
	 * Port to bind WireMock server to. By default this is a dynamic port.
	 */
	private int port;

	/**
	 * Base directory on the classpath to look for json-stub mappings.
	 */
	private String stubPath;

	/**
	 * Enables the integration of WireMock in your tests.
	 */
	private boolean enabled;

	public int getPort() {
		return port;
	}

	public void setPort(int port) {
		this.port = port;
	}

	public String getStubPath() {
		return stubPath;
	}

	public void setStubPath(String stubPath) {
		this.stubPath = stubPath;
	}

	public boolean isEnabled() {
		return enabled;
	}

	public void setEnabled(boolean enabled) {
		this.enabled = enabled;
	}

	
}
