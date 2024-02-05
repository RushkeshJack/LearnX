package com.springboot.LearnX.Service;

import java.util.List;

import com.springboot.LearnX.DTO.EmployeeDTO;
import com.springboot.LearnX.Entity.Admin;

public interface ProjectServices {

	// **** Admin function **** //
	public List<Admin> getAdmin();

	// **** Employee function **** //
	public List<EmployeeDTO> getAllEmployees();

	public EmployeeDTO getEmployeeById(int id);

	public List<EmployeeDTO> getEmployeesByName(String name);

	public List<EmployeeDTO> getEmployeeByAccessProvided();

	public List<EmployeeDTO> addEmployee(EmployeeDTO employeeDTO);

	public List<EmployeeDTO> deleteEmployee(int id);

	public List<EmployeeDTO> updateEmployee(EmployeeDTO employeeDTO);

	public List<EmployeeDTO> updateAccessOfEmployee(int id, int value);

	public List<EmployeeDTO> udpateEmployeeById(int id, EmployeeDTO employeeDTO);

}
