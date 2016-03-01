package com.example.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

@Service
public class NoteService {

	private final RestTemplate restTemplate;
	private final NoteServiceConfiguration config;

	@Autowired
	public NoteService(NoteServiceConfiguration config) {
		this.restTemplate = new RestTemplate();
		this.config = config;
	}

	public Note getNote(String id) {
		return restTemplate.getForObject(config.getBaseurl().resolve("/notes/"+id), Note.class);
	}
}
