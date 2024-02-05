package com.springboot.LearnX.Service;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import org.modelmapper.ModelMapper;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.env.Environment;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.springboot.LearnX.DTO.AddressDTO;
import com.springboot.LearnX.DTO.EmployeeAddressDTO;
import com.springboot.LearnX.DTO.EmployeeDTO;
import com.springboot.LearnX.Entity.Admin;
import com.springboot.LearnX.Entity.Employee;
import com.springboot.LearnX.Exception.EmployeeNotFoundException;
import com.springboot.LearnX.Exception.EmployeeeAlreadyPresentException;
import com.springboot.LearnX.Exception.EmployeeeInputException;
import com.springboot.LearnX.ExternalService.AddressService;
import com.springboot.LearnX.Repositories.AdminRepositories;
import com.springboot.LearnX.Repositories.EmployeeRepositories;
import com.springboot.util.LearnxConstants;

@Service
public class ProjectServiceImpl implements ProjectServices {

	@Autowired
	public EmployeeRepositories employeeRepositories;

	@Autowired
	public AdminRepositories adminRepositories;

	@Autowired
	public Environment environment;

	@Autowired
	public ModelMapper modelMapper;

	@Autowired
	public RestTemplate restTemplate;
	
	@Autowired
	public AddressService addressService;

	// **** Admin function **** //
	@Override
	public List<Admin> getAdmin() {
		List<Admin> allAdmin = (List<Admin>) adminRepositories.findAll();
		return allAdmin;
	}

	// **** Employee function **** //
	@Override
	public List<EmployeeDTO> getAllEmployees() {
		List<Employee> allEmployees = (List<Employee>) employeeRepositories.findAll();
		List<EmployeeDTO> allEmployeesDtos = new ArrayList<>();
		if (allEmployees.isEmpty()) {
			throw new EmployeeNotFoundException(environment.getProperty(LearnxConstants.EMPLOYEE_NOT_FOUND.toString()));
		}
		for (Employee employee : allEmployees) {
			allEmployeesDtos.add(modelMapper.map(employee, EmployeeDTO.class));
		}

		return allEmployeesDtos;
	}

	@Override
	public List<EmployeeDTO> getEmployeesByName(String name) {
		if (name.isEmpty()) {
			throw new EmployeeeInputException(environment.getProperty(LearnxConstants.BAD_REQUEST.toString()));
		}

		List<Employee> allEmployeesWithSearchName = this.employeeRepositories.findByNameContaining(name);
		List<EmployeeDTO> allEmployeesDtos = new ArrayList<>();

		if (allEmployeesWithSearchName.isEmpty() || allEmployeesWithSearchName == null) {
			throw new EmployeeNotFoundException(environment.getProperty(LearnxConstants.EMPLOYEE_NOT_FOUND.toString()));
		}

		for (Employee employee : allEmployeesWithSearchName) {
			allEmployeesDtos.add(modelMapper.map(employee, EmployeeDTO.class));
		}

		return allEmployeesDtos;
	}

	@Override
	public EmployeeDTO getEmployeeById(int id) {

		Optional<Employee> findById = this.employeeRepositories.findById(id);
		if (findById.isEmpty()) {
			throw new EmployeeNotFoundException(environment.getProperty(LearnxConstants.EMPLOYEE_NOT_FOUND.toString()));
		}

		Employee employee = findById.get();
		return modelMapper.map(employee, EmployeeDTO.class);
	}

	@Override
	public List<EmployeeDTO> getEmployeeByAccessProvided() {
		List<Employee> findEmployeeListByAccessProvided = this.employeeRepositories.findEmployeeListByAccessProvided();
		List<EmployeeDTO> allEmployeesDtos = new ArrayList<>();
		if (findEmployeeListByAccessProvided.isEmpty() || findEmployeeListByAccessProvided == null) {
			throw new EmployeeNotFoundException(environment.getProperty(LearnxConstants.EMPLOYEE_NOT_FOUND.toString()));
		}

		for (Employee emp : findEmployeeListByAccessProvided) {
			allEmployeesDtos.add(modelMapper.map(emp, EmployeeDTO.class));
		}
		return allEmployeesDtos;
	}

	@Override
	public List<EmployeeDTO> addEmployee(EmployeeDTO employeeDTO) {
		Optional<Employee> option = this.employeeRepositories.findById(employeeDTO.getId());

		List<EmployeeDTO> allEmployeesDtos = new ArrayList<>();
		if (option.isEmpty()) {

			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			String encodedEmployeePassword = passwordEncoder.encode(employeeDTO.getPassword());
			employeeDTO.setPassword(encodedEmployeePassword);
			this.employeeRepositories.save(modelMapper.map(employeeDTO, Employee.class));

		} else {
			throw new EmployeeeAlreadyPresentException(
					environment.getProperty(LearnxConstants.ALREADY_PRESENT.toString()));
		}

		List<Employee> allEmployees = this.employeeRepositories.findAll();

		for (Employee emp : allEmployees) {
			allEmployeesDtos.add(modelMapper.map(emp, EmployeeDTO.class));
		}

		return allEmployeesDtos;

	}

	@Override
	public List<EmployeeDTO> deleteEmployee(int id) {
		Optional<Employee> optional = this.employeeRepositories.findById(id);
		List<EmployeeDTO> allEmployeesDtos = new ArrayList<>();
		if (optional.isEmpty()) {
			throw new EmployeeNotFoundException(environment.getProperty(LearnxConstants.EMPLOYEE_NOT_FOUND.toString()));
		}

		employeeRepositories.deleteById(id);

		List<Employee> allEmployees = this.employeeRepositories.findAll();

		for (Employee emp : allEmployees) {
			allEmployeesDtos.add(modelMapper.map(emp, EmployeeDTO.class));
		}

		return allEmployeesDtos;
	}

	// Below method is not used in rest controller
	@Override
	public List<EmployeeDTO> updateEmployee(EmployeeDTO employeeDTO) {
		if (employeeDTO == null) {
			throw new EmployeeeInputException(environment.getProperty(LearnxConstants.BAD_REQUEST.toString()));
		}

		this.employeeRepositories.save(modelMapper.map(employeeDTO, Employee.class));

		List<EmployeeDTO> allEmployeesDtos = new ArrayList<>();
		List<Employee> allEmployees = this.employeeRepositories.findAll();

		for (Employee emp : allEmployees) {
			allEmployeesDtos.add(modelMapper.map(emp, EmployeeDTO.class));
		}
		return allEmployeesDtos;

	}

	@Override
	public List<EmployeeDTO> udpateEmployeeById(int id, EmployeeDTO employeeDTO) {

		Optional<Employee> optional = this.employeeRepositories.findById(id);
		List<EmployeeDTO> allEmployeesDtos = new ArrayList<>();

		if (optional.isEmpty()) {
			throw new EmployeeNotFoundException(environment.getProperty(LearnxConstants.EMPLOYEE_NOT_FOUND.toString()));
		} else if (employeeDTO.getAccess() == 1 || employeeDTO.getAccess() == 0) {
			Employee employeeById = new Employee();
			employeeById = optional.get();

			employeeById.setEmail(employeeDTO.getEmail());
			employeeById.setAccess(employeeDTO.getAccess());
			employeeById.setJob_level(employeeDTO.getJob_level());
			employeeById.setName(employeeDTO.getName());
			employeeById.setOffice(employeeDTO.getOffice());
			employeeById.setPassword(employeeDTO.getPassword());
			employeeById.setRole(employeeDTO.getRole());
			employeeById.setUnit(employeeDTO.getUnit());

			this.employeeRepositories.save(employeeById);
		}

		List<Employee> allEmployees = this.employeeRepositories.findAll();

		for (Employee emp : allEmployees) {
			allEmployeesDtos.add(modelMapper.map(emp, EmployeeDTO.class));
		}

		return allEmployeesDtos;

	}

	@Override
	public List<EmployeeDTO> updateAccessOfEmployee(int id, int value) {

		Optional<Employee> optional = this.employeeRepositories.findById(id);
		List<EmployeeDTO> allEmployeesDtos = new ArrayList<>();

		if (optional.isEmpty()) {
			throw new EmployeeNotFoundException(environment.getProperty(LearnxConstants.EMPLOYEE_NOT_FOUND.toString()));
		}

		Employee employee = optional.get();
		employee.setAccess(value);

		this.employeeRepositories.save(employee);

		List<Employee> allEmployees = this.employeeRepositories.findAll();

		for (Employee emp : allEmployees) {
			allEmployeesDtos.add(modelMapper.map(emp, EmployeeDTO.class));
		}

		return allEmployeesDtos;

	}

	// Below service is created for fetching data from other origin i.e. CORS -
	// Cross Origin Request Sharing learning purpose.

	public List<EmployeeAddressDTO> getEmployeeWithAddress() {
		List<Employee> allEmployees = this.employeeRepositories.findAll();
		List<EmployeeAddressDTO> allEmployeesAddressDtos = new ArrayList<>();
		
		EmployeeAddressDTO employeeAddressDTO = null;

		for (Employee employee : allEmployees) {
			//ResponseEntity<AddressDTO> allAddresses = restTemplate.getForEntity(
			//		"http://localhost:8889/api/v1/addresses/" + employee.getId(), AddressDTO.class);

			employeeAddressDTO = new EmployeeAddressDTO();
			//Implemented feignClient to connect with LearnX-Address-Service
			AddressDTO Address = this.addressService.getAddress(employee.getId());
			
			employeeAddressDTO.setId(employee.getId());
			employeeAddressDTO.setEmail(employee.getEmail());
			employeeAddressDTO.setAccess(employee.getAccess());
			employeeAddressDTO.setJob_level(employee.getJob_level());
			employeeAddressDTO.setName(employee.getName());
			employeeAddressDTO.setOffice(employee.getOffice());
			employeeAddressDTO.setPassword(employee.getPassword());
			employeeAddressDTO.setRole(employee.getRole());
			employeeAddressDTO.setUnit(employee.getUnit());
			
			//employeeAddressDTO.setAddressDTO(allAddresses.getBody());
			employeeAddressDTO.setAddress(Address);

			allEmployeesAddressDtos.add(employeeAddressDTO);
		}

		return allEmployeesAddressDtos;

	}

}
