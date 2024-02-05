package com.springboot.LearnX.DTO;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;

public class EmployeeDTO {

	@NotNull(message = "{employee.id.must}")
	private int id;
	@NotBlank(message = "{employee.name.must}")
	private String name;
	@NotBlank(message = "{employee.email.must}")
	@Email(message = "{employee.email.ivalid}")
	private String email;
	@NotBlank(message = "{employee.password.must}")
	@Pattern(regexp = "^(?=.*[0-9])(?=.*[a-z])(?=.*[A-Z])(?=\\S+$).{8,}$", message = "{customer.password.invalid}")
	private String password;
	@NotBlank(message = "{employee.role.must}")
	private String role;
	@NotBlank(message = "{employee.job.level.must}")
	private String job_level;
	@NotBlank(message = "{employee.unit.must}")
	private String unit;
	@NotBlank(message = "{employee.office.must}")
	private String office;
	@NotNull(message = "{employee.access.must}")
	private int access;

	private AddressDTO address;

	public AddressDTO getAddress() {
		return address;
	}

	public void setAddress(AddressDTO address) {
		this.address = address;
	}

	public int getId() {
		return id;
	}

	public void setId(int id) {
		this.id = id;
	}

	public String getName() {
		return name;
	}

	public void setName(String name) {
		this.name = name;
	}

	public String getEmail() {
		return email;
	}

	public void setEmail(String email) {
		this.email = email;
	}

	public String getPassword() {
		return password;
	}

	public void setPassword(String password) {
		this.password = password;
	}

	public String getRole() {
		return role;
	}

	public void setRole(String role) {
		this.role = role;
	}

	public String getJob_level() {
		return job_level;
	}

	public void setJob_level(String job_level) {
		this.job_level = job_level;
	}

	public String getUnit() {
		return unit;
	}

	public void setUnit(String unit) {
		this.unit = unit;
	}

	public String getOffice() {
		return office;
	}

	public void setOffice(String office) {
		this.office = office;
	}

	public int getAccess() {
		return access;
	}

	public void setAccess(int access) {
		this.access = access;
	}

}
