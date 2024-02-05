package com.springboot.LearnX.Controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.annotation.Validated;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.springboot.LearnX.DTO.EmployeeAddressDTO;
import com.springboot.LearnX.DTO.EmployeeDTO;
import com.springboot.LearnX.Service.ProjectServiceImpl;

import jakarta.validation.Valid;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.NotNull;

@RestController
@Validated
public class EmployeeRestController {

	@Autowired
	public ProjectServiceImpl projectServiceImpl;

	@GetMapping(value = "/api/v1/employee")
	public ResponseEntity<List<EmployeeDTO>> searchByNameQuery(@NotBlank @RequestParam("name") String name) {
		return new ResponseEntity<List<EmployeeDTO>>(this.projectServiceImpl.getEmployeesByName(name), HttpStatus.OK);
	}

	@GetMapping(value = "/api/v1/employee/remove-access/{id}")
	public ResponseEntity<List<EmployeeDTO>> employeeRemoveAccess(@NotNull @PathVariable("id") int id) {
		int value = 0;
		return new ResponseEntity<List<EmployeeDTO>>(this.projectServiceImpl.updateAccessOfEmployee(id, value),
				HttpStatus.OK);
	}

	@GetMapping(value = "/api/v1/employee/access/{id}")
	public ResponseEntity<List<EmployeeDTO>> giveAccessToEmployee(@NotNull @PathVariable("id") int id) {
		int value = 1;
		return new ResponseEntity<List<EmployeeDTO>>(this.projectServiceImpl.updateAccessOfEmployee(id, value),
				HttpStatus.OK);
	}

	@GetMapping(value = "/api/v1/employees")
	public ResponseEntity<List<EmployeeDTO>> getAllEmployees() {
		return new ResponseEntity<List<EmployeeDTO>>(this.projectServiceImpl.getAllEmployees(), HttpStatus.OK);

	}

	@PostMapping(value = "/api/v1/employee")
	public ResponseEntity<List<EmployeeDTO>> addEmployee(@Valid @RequestBody EmployeeDTO employeeDTO) {
		return new ResponseEntity<List<EmployeeDTO>>(this.projectServiceImpl.addEmployee(employeeDTO),
				HttpStatus.CREATED);
	}

	@DeleteMapping(value = "/api/v1/employee/{id}")
	public ResponseEntity<List<EmployeeDTO>> deleteEmployeeById(@NotNull @PathVariable("id") int id) {
		return new ResponseEntity<List<EmployeeDTO>>(this.projectServiceImpl.deleteEmployee(id), HttpStatus.OK);
	}

	@PutMapping(value = "/api/v1/employee/{id}")
	public ResponseEntity<List<EmployeeDTO>> udpateEmployeeById(@NotNull @PathVariable("id") int id,
			@Valid @RequestBody EmployeeDTO employeeDTO) {
		return new ResponseEntity<List<EmployeeDTO>>(this.projectServiceImpl.udpateEmployeeById(id, employeeDTO),
				HttpStatus.OK);

	}

	// Below controller handler is for understanding the RestTemplate to fetch data
	// from other server. i.e CORS - Cross Origin Request Sharing
	@GetMapping(value = "/api/v1/all-employee")
	public ResponseEntity<List<EmployeeAddressDTO>> getAllEmployeeWithAddress() {
		return new ResponseEntity<List<EmployeeAddressDTO>>(this.projectServiceImpl.getEmployeeWithAddress(),
				HttpStatus.OK);
	}

}
