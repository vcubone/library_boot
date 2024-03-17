package ru.batorov.library.controllers;


import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.AccessDeniedException;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import ru.batorov.library.util.exceptions.BookNotFoundException;
import ru.batorov.library.util.exceptions.ErrorResponse;
import ru.batorov.library.util.exceptions.PersonNotFoundException;
import ru.batorov.library.util.exceptions.RoleNotFoundException;

@ControllerAdvice
public class GlobalControllerExceptionHandler {
	@ExceptionHandler(IllegalArgumentException.class)
	private ResponseEntity<ErrorResponse> handlerException(IllegalArgumentException e) {
		ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);// 400
	}
	
	@ExceptionHandler(BadCredentialsException.class)
	private ResponseEntity<ErrorResponse> handlerException(BadCredentialsException e) {
		ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(errorResponse, HttpStatus.UNAUTHORIZED);// 401
	}
	
	@ExceptionHandler(AccessDeniedException.class)
	private ResponseEntity<ErrorResponse> handlerException(AccessDeniedException e) {
		ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(errorResponse, HttpStatus.FORBIDDEN);// 403
	}
	
	@ExceptionHandler({PersonNotFoundException.class, RoleNotFoundException.class, BookNotFoundException.class})
	private ResponseEntity<ErrorResponse> handlerException(RuntimeException e) {
		ErrorResponse errorResponse = new ErrorResponse(e.getMessage(), System.currentTimeMillis());
		return new ResponseEntity<>(errorResponse, HttpStatus.NOT_FOUND);// 404
	}
}
