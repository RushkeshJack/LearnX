package com.springboot.LearnX.Exception;

public class EmployeeeAlreadyPresentException extends RuntimeException {

	/**
	 * 
	 */
	private static final long serialVersionUID = 1L;

	public EmployeeeAlreadyPresentException(String message) {
		super(message);
	}
}
