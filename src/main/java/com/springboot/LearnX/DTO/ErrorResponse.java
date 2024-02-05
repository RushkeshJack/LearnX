package com.springboot.LearnX.DTO;

public class ErrorResponse {

	private String errorTimeStamp;
	private int errorCode;
	private String errorMessage;

	public String getErrorTimeStamp() {
		return errorTimeStamp;
	}

	public void setErrorTimeStamp(String errorTimeStamp) {
		this.errorTimeStamp = errorTimeStamp;
	}

	public int getErrorCode() {
		return errorCode;
	}

	public void setErrorCode(int errorCode) {
		this.errorCode = errorCode;
	}

	public String getErrorMessage() {
		return errorMessage;
	}

	public void setErrorMessage(String errorMessage) {
		this.errorMessage = errorMessage;
	}

}
