package com.example.client;

import java.net.URI;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NoteService {

	private final RestTemplate restTemplate;

	private NoteServiceConfiguration configuration;

	@Autowired
	public NoteService(NoteServiceConfiguration configuration) {
		this.configuration = configuration;
		this.restTemplate = new RestTemplate();
	}

	public Note getNote(String id) {
		URI resolvedUri = configuration.baseUri().resolve("/notes/").resolve(id);
		Note note = restTemplate.getForObject(resolvedUri, Note.class);
		return note;
	}
}
