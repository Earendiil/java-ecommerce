package com.application.ecommerce.exceptions;

import java.util.HashMap;
import java.util.Map;
import java.util.stream.Collectors;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.application.ecommerce.payload.APIResponse;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice //is the specialized annotation of @ControllerAdvice for 
//REST-APIs.Better for REST-APIs. for hybrid API & HTML use @ControllerAdvice
//Methods automatically return JSON or XML responses instead of view names
public class MyGlobalExceptionHandler {

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<Map<String, String>> myMethodArgumentNotValidException(MethodArgumentNotValidException e){
		
		Map<String, String> response = new HashMap<>();
		e.getBindingResult().getAllErrors().forEach(err -> {
		String	fieldName = ((FieldError) err).getField();
		String message= err.getDefaultMessage();
		response.put(fieldName, message);
		});
		return new  ResponseEntity<Map<String, String>>(response, HttpStatus.BAD_REQUEST);
	}
	@ExceptionHandler(ResourceNotFoundException.class)
	public ResponseEntity<APIResponse> myResourceNotFoundException(ResourceNotFoundException e){
		String message = e.getMessage();
		APIResponse apiResponse = new APIResponse(message, false);// false because there was an exception
		return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(APIException.class)
	public ResponseEntity<APIResponse> myAPIException(APIException e){
		String message = e.getMessage();
		APIResponse apiResponse = new APIResponse(message, false);
		return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.BAD_REQUEST);
	}
   
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<APIResponse> myConstraintViolationException(ConstraintViolationException e){
		//we want only the reason of the exception not the whole exception
		String message = e.getConstraintViolations().stream()
		        .map(ConstraintViolation::getMessage) // Extract the violation message
		        .collect(Collectors.joining(", "));
		
		APIResponse apiResponse = new APIResponse(message,false);
		return new ResponseEntity<APIResponse>(apiResponse, HttpStatus.BAD_REQUEST);
	}
	
}
