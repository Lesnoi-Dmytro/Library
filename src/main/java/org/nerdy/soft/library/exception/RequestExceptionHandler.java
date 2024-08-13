package org.nerdy.soft.library.exception;

import jakarta.validation.ConstraintViolationException;
import org.springframework.http.HttpStatusCode;
import org.springframework.http.ResponseEntity;
import org.springframework.web.ErrorResponse;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

import java.util.NoSuchElementException;

@ControllerAdvice
public class RequestExceptionHandler {
	@ExceptionHandler({NoSuchElementException.class, IllegalStateException.class, ConstraintViolationException.class})
	public ResponseEntity<ErrorResponse> handleException(final Exception ex) {
		HttpStatusCode code = HttpStatusCode.valueOf(400);
		ErrorResponse response = ErrorResponse.create(
				ex, code, ex.getMessage());

		return new ResponseEntity<>(response, code);
	}
}
