package com.example.client;

import java.net.URI;

import org.slf4j.Logger;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NoteService {

	private static final Logger log = org.slf4j.LoggerFactory.getLogger(NoteService.class);

	private final RestTemplate restTemplate;

	private final NoteServiceConfiguration configuration;

	@Autowired
	public NoteService(NoteServiceConfiguration configuration) {
		this.configuration = configuration;
		this.restTemplate = new RestTemplate();
	}

	public Note getNote(String id) {
		URI resolvedUri = configuration.baseUri().resolve("/notes/").resolve(id);
		log.info("Retrieving note from {}", resolvedUri);
		return restTemplate.getForObject(resolvedUri, Note.class);
	}
}
