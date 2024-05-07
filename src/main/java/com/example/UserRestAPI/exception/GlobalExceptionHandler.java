package com.example.UserRestAPI.exception;

import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.servlet.NoHandlerFoundException;

import java.nio.file.AccessDeniedException;

@ControllerAdvice
@Slf4j
public class GlobalExceptionHandler {

	// Handle 404 - Resource Not Found
	@ExceptionHandler(NoHandlerFoundException.class)
	public ResponseEntity<String> handleResourceNotFoundException() {
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Resource Not Found");
	}

	// Handle 500 - Internal Server Error
	@ExceptionHandler(Exception.class)
	public ResponseEntity<String> handleInternalServerError() {
		return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Internal Server Error");
	}

	// Handle 403 - Forbidden
	@ExceptionHandler(AccessDeniedException.class)
	public ResponseEntity<String> handleAccessDeniedException() {
		return ResponseEntity.status(HttpStatus.FORBIDDEN).body("Forbidden");
	}
}
