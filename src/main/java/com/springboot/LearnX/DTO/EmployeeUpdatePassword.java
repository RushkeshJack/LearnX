package com.springboot.LearnX.DTO;

public class EmployeeUpdatePassword {

	private int id;
	private String password;
	private String confirmPassword;

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getConfirmPassword() {
		return confirmPassword;
	}

	public void setConfirmPassword(String confirmPassword) {
		this.confirmPassword = confirmPassword;
	}

	@Override
	public String toString() {
		return "EmployeeUpdatePassword [id=" + id + ", password=" + password + ", confirmPassword=" + confirmPassword
				+ "]";
	}

}
