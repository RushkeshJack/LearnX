package com.springboot.LearnX.Controller;

public class EmployeeExceptionController {

//	@ResponseStatus(value = HttpStatus.NOT_FOUND)
//	@ExceptionHandler(value = EmployeeNotFoundException.class)
//	public String exception(EmployeeNotFoundException employeeNotFoundException, Model model) {
//		model.addAttribute("errorMessage", employeeNotFoundException.getMessage());
//		return "error";
//	}
//
//	@ResponseStatus(value = HttpStatus.NOT_FOUND)
//	@ExceptionHandler(value = EmployeeNotHaveAccess.class)
//	public String exception(EmployeeNotHaveAccess employeeNotHaveAccess, Model model) {
//		model.addAttribute("errorMessage", employeeNotHaveAccess.getMessage());
//		return "employee-login";
//	}
//
//	@ResponseStatus(value = HttpStatus.BAD_REQUEST)
//	@ExceptionHandler(value = IllegalArgumentException.class)
//	public String IllegalArgumentException(Model model) {
//		model.addAttribute("errorMessage", "BCryptPasswordEncoder error. Password cannot be null!");
//		return "error";
//	}
//
//	@ResponseStatus(value = HttpStatus.INTERNAL_SERVER_ERROR)
//	@ExceptionHandler(value = Exception.class)
//	public String Exception(Exception e, Model model) {
//		e.printStackTrace();
//		model.addAttribute("errorMessage", "Internal Server Error");
//		return "error";
//	}

}
