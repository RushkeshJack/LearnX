package com.springboot.LearnX.Controller;

import java.time.LocalDateTime;
import java.util.stream.Collectors;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.ObjectError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;

import com.springboot.LearnX.DTO.ErrorResponse;
import com.springboot.LearnX.Exception.EmployeeNotFoundException;
import com.springboot.LearnX.Exception.EmployeeeAlreadyPresentException;
import com.springboot.LearnX.Exception.EmployeeeInputException;
import com.springboot.util.LearnxConstants;

import jakarta.validation.ConstraintViolation;
import jakarta.validation.ConstraintViolationException;

@RestControllerAdvice
public class EmployeeRestExceptionController {

	@Autowired
	public Environment environment;

	@ExceptionHandler(value = EmployeeNotFoundException.class)
	public ResponseEntity<ErrorResponse> employeeNotFoundException(EmployeeNotFoundException ex) {

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorTimeStamp(LocalDateTime.now().toString());
		errorResponse.setErrorCode(HttpStatus.NOT_FOUND.value());
		errorResponse.setErrorMessage(ex.getMessage());

		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.NOT_FOUND);
	}

	@ExceptionHandler(value = EmployeeeInputException.class)
	public ResponseEntity<ErrorResponse> employeeeInputException(EmployeeeInputException ex) {

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorTimeStamp(LocalDateTime.now().toString());
		errorResponse.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errorResponse.setErrorMessage(ex.getMessage());

		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	@ExceptionHandler(value = EmployeeeAlreadyPresentException.class)
	public ResponseEntity<ErrorResponse> employeeeAlreadyPresentException(EmployeeeAlreadyPresentException ex) {

		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorTimeStamp(LocalDateTime.now().toString());
		errorResponse.setErrorCode(HttpStatus.CONFLICT.value());
		errorResponse.setErrorMessage(ex.getMessage());

		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.CONFLICT);
	}

	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErrorResponse> methodArgumentExceptionHandler(MethodArgumentNotValidException ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorTimeStamp(LocalDateTime.now().toString());
		errorResponse.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errorResponse.setErrorMessage(ex.getBindingResult().getAllErrors().stream().map(ObjectError::getDefaultMessage)
				.collect(Collectors.joining(", ")));
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

	// Handler for validation failures w.r.t URI parameters
	@ExceptionHandler(ConstraintViolationException.class)
	public ResponseEntity<ErrorResponse> constraintViolationExceptionHandler(ConstraintViolationException ex) {
		ErrorResponse errorResponse = new ErrorResponse();
		errorResponse.setErrorTimeStamp(LocalDateTime.now().toString());
		errorResponse.setErrorCode(HttpStatus.BAD_REQUEST.value());
		errorResponse.setErrorMessage(ex.getConstraintViolations().stream().map(ConstraintViolation::getMessage)
				.collect(Collectors.joining(", ")));
		return new ResponseEntity<>(errorResponse, HttpStatus.BAD_REQUEST);
	}

//	@ExceptionHandler(value = Exception.class)
//	public ResponseEntity<ErrorResponse> exception(Exception ex) {
//
//		ErrorResponse errorResponse = new ErrorResponse();
//		errorResponse.setErrorTimeStamp(LocalDateTime.now().toString());
//		errorResponse.setErrorCode(HttpStatus.INTERNAL_SERVER_ERROR.value());
//		errorResponse.setErrorMessage(environment.getProperty(LearnxConstants.GENERAL_EXCEPTION_MESSAGE.toString()));
//
//		// ex.printStackTrace();
//
//		return new ResponseEntity<ErrorResponse>(errorResponse, HttpStatus.INTERNAL_SERVER_ERROR);
//	}

}
