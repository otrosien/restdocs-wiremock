package com.example.notes;

import org.springframework.core.convert.ConversionFailedException;
import org.springframework.data.rest.webmvc.RepositoryRestExceptionHandler;
import org.springframework.data.rest.webmvc.support.ExceptionMessage;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.ResponseBody;

@ControllerAdvice(basePackageClasses = RepositoryRestExceptionHandler.class)
public class NotesExceptionHandler {

	@ExceptionHandler(ConversionFailedException.class)
	@ResponseBody
	ResponseEntity<ExceptionMessage> handle(ConversionFailedException e) {
		return new ResponseEntity<>(new ExceptionMessage(e), new HttpHeaders(), HttpStatus.BAD_REQUEST);
	}
}
