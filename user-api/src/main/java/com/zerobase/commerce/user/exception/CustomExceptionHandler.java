package com.zerobase.commerce.user.exception;

import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.ToString;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.ControllerAdvice;
import org.springframework.web.bind.annotation.ExceptionHandler;

@Slf4j
@ControllerAdvice
public class CustomExceptionHandler {

	@ExceptionHandler({
		CustomException.class
	})
	protected ResponseEntity<ExceptionResponse> handleCustomException(CustomException e) {

		log.warn("api Exception : {}", e.getErrorCode());

		return ResponseEntity
			.status(e.getErrorCode().getHttpStatus())
			.body(new ExceptionResponse(e.getErrorCode().getHttpStatus(), e.getErrorCode().name(),
				e.getErrorCode().getDetail()));
	}

	@Getter
	@ToString
	@AllArgsConstructor
	public class ExceptionResponse {

		private final HttpStatus httpStatus;
		private final String code;
		private final String detail;

	}
}
