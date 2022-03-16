package com.invoice.handler;

import java.util.Date;

import io.jsonwebtoken.ExpiredJwtException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.context.request.WebRequest;
import org.springframework.web.servlet.mvc.method.annotation.ResponseEntityExceptionHandler;

import io.jsonwebtoken.JwtException;

@ControllerAdvice
public class CrudExceptionHandler extends ResponseEntityExceptionHandler {


	@ExceptionHandler(Throwable.class)
	public final ResponseEntity<ExceptionResponse> handlerBadRequestException(Exception ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),request.getDescription(false));
		HttpStatus status = HttpStatus.BAD_REQUEST;
		
		if(JwtException.class.isAssignableFrom(ex.getClass())) {
			exceptionResponse.setMessage(ex.getMessage());
			status = HttpStatus.UNAUTHORIZED;
		}
		
		return new ResponseEntity<ExceptionResponse>(exceptionResponse, status);
	}

	@ExceptionHandler(AuthenticationException.class)
	public final ResponseEntity<ExceptionResponse> handlerExpiredException(Exception ex, WebRequest request) {
		ExceptionResponse exceptionResponse = new ExceptionResponse(new Date(), ex.getMessage(),request.getDescription(false));
		HttpStatus status = HttpStatus.BAD_REQUEST;

		if(JwtException.class.isAssignableFrom(ex.getClass())) {
			exceptionResponse.setMessage(ex.getMessage());
			status = HttpStatus.UNAUTHORIZED;
		}

		return new ResponseEntity<ExceptionResponse>(exceptionResponse, status);
	}
}
