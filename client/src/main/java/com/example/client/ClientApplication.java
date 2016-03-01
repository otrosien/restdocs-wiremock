package com.example.client;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.builder.SpringApplicationBuilder;

@SpringBootApplication
public class ClientApplication implements CommandLineRunner {

	@Autowired
	private NoteService noteService;

	public static void main(String[] args) {
		new SpringApplicationBuilder(ClientApplication.class).run(args);
	}

	@Override
	public void run(String... args) throws Exception {
		if(args.length > 0) {
			String noteId = args[0];
			Note note = noteService.getNote(noteId);
			System.out.println("Note title:" + note.getTitle());
		}
	}
}
