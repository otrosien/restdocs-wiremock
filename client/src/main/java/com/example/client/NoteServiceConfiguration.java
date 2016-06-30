package com.example.client;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Component;

@Component
class NoteServiceConfiguration {

	private final Environment env;

	@Autowired
	public NoteServiceConfiguration(Environment env) {
		this.env = env;
	}

	public URI baseUri() {
		String baseStr = env.getProperty("noteservice.baseUri");
		return URI.create(baseStr);
	}
}