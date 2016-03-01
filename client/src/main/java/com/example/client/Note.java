package com.example.client;

import com.fasterxml.jackson.annotation.JsonCreator;
import com.fasterxml.jackson.annotation.JsonProperty;

public class Note {

	final String title;

	@JsonCreator
	public Note(@JsonProperty("title") String title) {
		this.title = title;
	}

	public String getTitle() {
		return title;
	}
}
