package com.example.client;

import java.net.URI;

import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;

@Component
@ConfigurationProperties(prefix="noteservice") class NoteServiceConfiguration {

	private URI baseurl = URI.create("http://localhost:8080");

	public URI getBaseurl() {
		return baseurl;
	}

	public void setBaseurl(URI baseurl) {
		this.baseurl = baseurl;
	}
}