package com.zerobase.commerce.order.exception;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Getter
public class CustomException extends RuntimeException{
	private final ErrorCode errorCode;

}
