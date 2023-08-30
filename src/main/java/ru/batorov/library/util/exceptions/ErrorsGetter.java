package ru.batorov.library.util.exceptions;

import java.util.List;

import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;

public class ErrorsGetter {
	public static String getErrors(BindingResult bindingResult) {
		StringBuilder stringBuilder = new StringBuilder();
		List<FieldError> fieldErrors = bindingResult.getFieldErrors();
		for (FieldError fieldError : fieldErrors) {
			stringBuilder.append(fieldError.getField()).append(" - ").append(fieldError.getDefaultMessage())
					.append(";");
		}
		return stringBuilder.toString();
	}
}
