package com.springboot.LearnX.Controller;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.NoSuchElementException;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.PropertySource;
import org.springframework.http.HttpStatus;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseStatus;

import com.springboot.LearnX.DTO.AdminLogin;
import com.springboot.LearnX.DTO.EmployeeDTO;
import com.springboot.LearnX.DTO.EmployeeLogin;
import com.springboot.LearnX.DTO.EmployeeUpdatePassword;
import com.springboot.LearnX.Entity.Admin;
import com.springboot.LearnX.Exception.EmployeeNotFoundException;
import com.springboot.LearnX.Service.ProjectServiceImpl;

@Controller
public class EmployeeController {

	@Autowired
	public ProjectServiceImpl projectServiceImpl;
	
//	@Value("${AWS.bucket.name}")
	public String awsBucketName = "learnxresourcebucket";
	
//	@Value("${AWS.bucket.region}")
	public String awsBucketRegion = "ap-south-1";
	
	private String awsResourceUrl = "https://"+ awsBucketName + ".s3." + awsBucketRegion + ".amazonaws.com";

	@GetMapping(value = "/")
	@ResponseStatus(value = HttpStatus.OK)
	public String index() {
		return "index";
	}

//	@GetMapping(value = "/error")
//	@ResponseStatus(value = HttpStatus.OK)
//	public String error() {
//		return "error";
//	}

	// ********** admin handlers are below *********//

	@GetMapping(value = "/admin-login")
	@ResponseStatus(value = HttpStatus.OK)
	public String adminLogIn() {
		return "admin-login";
	}

	@PostMapping(value = "/admin-login")
	@ResponseStatus(value = HttpStatus.OK)
	public String adminLogInSuccess(@ModelAttribute("adminLogin") AdminLogin adminLogin, Model model) {

		try {

			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			List<Admin> adminsFromDatabase = this.projectServiceImpl.getAdmin();

			// System.out.println(passwordEncoder.encode(adminLogin.getPassword()));

			if (adminLogin.getEmail().isEmpty() || adminLogin.getPassword().isEmpty()) {
				throw new NullPointerException("Email or Password cannot be null !");
			}

			for (Admin admin : adminsFromDatabase) {

				if (adminLogin.getEmail().equals(admin.getEmail())
						&& passwordEncoder.matches(adminLogin.getPassword(), admin.getPassword())) {
					List<EmployeeDTO> employeesWithAccess = this.projectServiceImpl.getEmployeeByAccessProvided();
					model.addAttribute("employeesWithAccess", employeesWithAccess);
					
					return "view-employee";

				} else if (!(adminLogin.getEmail().equals(admin.getEmail()))) {
					model.addAttribute("errorMessage", "Please enter  correct email!");
					return "admin-login";

				} else if (adminLogin.getEmail().equals(admin.getEmail())
						&& !(passwordEncoder.matches(adminLogin.getPassword(), admin.getPassword()))) {
					model.addAttribute("errorMessage", "Please enter correct password!");
					return "admin-login";

				}

			}
			model.addAttribute("errorMessage", "Please enter correct email and password!");
			return "admin-login";

		} catch (NullPointerException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "admin-login";
		} catch (IllegalArgumentException e) {
			model.addAttribute("errorMessage", "Exception related BCryptPasswordEncoder occured !");
			return "admin-login";
		} catch (Exception e) {
			model.addAttribute("errorMessage", "Please contact your helpdesk !!");
			return "admin-login";
		}

	}

	// Below are Rest mapping for data show,add,delete,update in admin

	@GetMapping(value = "/admin/view-employee")
	@ResponseStatus(value = HttpStatus.OK)
	public String adminViewEmployee(Model model) {

		List<EmployeeDTO> employeesWithAccess = this.projectServiceImpl.getEmployeeByAccessProvided();
		model.addAttribute("employeesWithAccess", employeesWithAccess);
		return "view-employee";

	}

	@GetMapping(value = "/admin/add-employee")
	@ResponseStatus(value = HttpStatus.OK)
	public String adminAddEmployee() {
		return "add-employee";
	}

	@PostMapping(value = "/admin/add-employee")
	@ResponseStatus(value = HttpStatus.CREATED)
	public String adminAddEmployeeSuccess(@ModelAttribute EmployeeDTO incomingEmployee, Model model) {

		List<EmployeeDTO> employeesFromDatabase = this.projectServiceImpl.getAllEmployees();
		if (incomingEmployee.getName().isEmpty() || incomingEmployee.getEmail().isEmpty()
				|| incomingEmployee.getPassword().isEmpty()) {
			model.addAttribute("errorMessage", "Fields cannot be null !");
			return "add-employee";
		} else if (employeesFromDatabase == null)
			throw new EmployeeNotFoundException("Employees not found !");

		for (EmployeeDTO employeeDTO : employeesFromDatabase) {
			if (incomingEmployee.getId() == employeeDTO.getId()) {
				model.addAttribute("errorMessage", "Id already exist");
				return "add-employee";
			} else if (incomingEmployee.getEmail().equals(employeeDTO.getEmail())) {
				model.addAttribute("errorMessage", "Email already exist");
				return "add-employee";
			}
		}

		// Below 3 lines of code to encode the employee password in database
//		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
//		String encodedEmployeePassword = passwordEncoder.encode(incomingEmployee.getPassword());
//		incomingEmployee.setPassword(encodedEmployeePassword);

		projectServiceImpl.addEmployee(incomingEmployee);
		List<EmployeeDTO> newEmployeesFromDatabase = this.projectServiceImpl.getAllEmployees();
		model.addAttribute("employeesFromDatabase", newEmployeesFromDatabase);
		return "search-employee";

	}

	@GetMapping(value = "/admin/view-employee/remove-access/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public String adminViewEmployeeRemoveAccess(@PathVariable("id") int id, Model model) {

		// Not deleting the employee else it is updating the access value to 0

		EmployeeDTO employeeDTO = this.projectServiceImpl.getEmployeeById(id);
		if (employeeDTO == null)
			throw new EmployeeNotFoundException("Employee not found !");
		employeeDTO.setAccess(0);
		this.projectServiceImpl.updateEmployee(employeeDTO);
		List<EmployeeDTO> employeesWithAccess = this.projectServiceImpl.getEmployeeByAccessProvided();
		model.addAttribute("employeesWithAccess", employeesWithAccess);

		return "view-employee";

	}

	@GetMapping("/admin/search-employee")
	@ResponseStatus(value = HttpStatus.OK)
	public String adminSearchEmployee(@RequestParam(name = "name", required = false) String name, Model model) {

		if (name != null) {
			// Add employee and search employee have same model attribute
			List<EmployeeDTO> employeesByName = this.projectServiceImpl.getEmployeesByName(name);
			model.addAttribute("employeesFromDatabase", employeesByName);
			return "search-employee";
		}

		List<EmployeeDTO> employeesFromDatabase = this.projectServiceImpl.getAllEmployees();
		model.addAttribute("employeesFromDatabase", employeesFromDatabase);
		return "search-employee";

	}

	@GetMapping("/admin/search-employee/access/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public String adminSearchEmployeeGiveAccess(@PathVariable int id, Model model) {

		EmployeeDTO employeeDTO = this.projectServiceImpl.getEmployeeById(id);
		if (employeeDTO == null)
			throw new EmployeeNotFoundException("Employee not found !");
		employeeDTO.setAccess(1);
		this.projectServiceImpl.updateEmployee(employeeDTO);

		List<EmployeeDTO> employeesWithAccess = this.projectServiceImpl.getEmployeeByAccessProvided();
		model.addAttribute("employeesWithAccess", employeesWithAccess);

		return "view-employee";

	}

	@GetMapping("/admin/search-employee/delete-employee/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public String adminSearchEmployeeDelete(@PathVariable int id, Model model) {

		this.projectServiceImpl.deleteEmployee(id);
		List<EmployeeDTO> employeesFromDatabase = this.projectServiceImpl.getAllEmployees();
		model.addAttribute("employeesFromDatabase", employeesFromDatabase);
		return "search-employee";

	}

	@GetMapping("/admin/search-employee/update-employee/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public String adminSearchEmployeeUpdate(@PathVariable int id, Model model) {

		EmployeeDTO selectedEmployee = this.projectServiceImpl.getEmployeeById(id);

		List<String> jobRoleList = Arrays.asList("Mainframe developer", "Springboot developer", "Java developer",
				"Python developer", "React developer", "Cloud Engineer", "Data scientist");

		String selectedJobRole = selectedEmployee.getRole();

		List<String> jobLevelList = Arrays.asList("JL1", "JL2", "JL3", "JL4", "JL5", "JL6", "JL7");

		String selectedJobLevel = selectedEmployee.getJob_level();

		List<String> unitList = Arrays.asList("Healthcare", "Banking", "Cyber security", "Insurance",
				"Application Development", "Automotive", "Aerospace and Defense");

		String selectedUnit = selectedEmployee.getUnit();

		List<String> officeList = Arrays.asList("Pune Hinjewadi", "Pune Magarpatta", "Mumbai", "Nagpur", "Chennai",
				"Bengaluru", "Hydrabad", "Kolkatta");

		String selectedOffice = selectedEmployee.getOffice();

		model.addAllAttributes(Map.of("jobRoleList", jobRoleList, "selectedJobRole", selectedJobRole, "jobLevelList",
				jobLevelList, "selectedJobLevel", selectedJobLevel, "unitList", unitList, "selectedUnit", selectedUnit,
				"officeList", officeList, "selectedOffice", selectedOffice, "selectedEmployee", selectedEmployee));

		return "update-employee";

	}

	@PostMapping("/admin/search-employee/update-employee")
	@ResponseStatus(value = HttpStatus.OK)
	public String adminSearchEmployeeUpdateSuccess(@ModelAttribute("updatedEmployee") EmployeeDTO updatedEmployee,
			Model model) {

		if (updatedEmployee.getName().isEmpty() || updatedEmployee.getEmail().isEmpty()) {
			model.addAttribute("errorMessage", "Fields cannot be null !");
			return "update-employee";
		}

		// Below 3 lines of code to encode the employee password in database
		BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
		String encodedEmployeePassword = passwordEncoder.encode(updatedEmployee.getPassword());
		updatedEmployee.setPassword(encodedEmployeePassword);

		this.projectServiceImpl.updateEmployee(updatedEmployee);
		List<EmployeeDTO> employeesFromDatabase = this.projectServiceImpl.getAllEmployees();
		model.addAttribute("employeesFromDatabase", employeesFromDatabase);
		return "search-employee";

	}

	@GetMapping("/admin/search-employee/remove-access/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public String adminSearchEmployeeRemoveAccess(@PathVariable int id, Model model) {

		EmployeeDTO employeeDTO = this.projectServiceImpl.getEmployeeById(id);
		if (employeeDTO == null)
			throw new EmployeeNotFoundException("Employee not found !");
		employeeDTO.setAccess(0);
		this.projectServiceImpl.updateEmployee(employeeDTO);

		List<EmployeeDTO> employeesFromDatabase = this.projectServiceImpl.getAllEmployees();
		model.addAttribute("employeesFromDatabase", employeesFromDatabase);
		return "search-employee";

	}

	// Mapping for Update Page to search for employee Id
	@GetMapping(value = "/admin/update-employee")
	@ResponseStatus(value = HttpStatus.OK)
	public String adminUpdateEmployeeById() {
		return "update-employee-first";
	}

	// Mapping for update page to update employee details
	@GetMapping(value = "/admin/update-employee/employee")
	@ResponseStatus(value = HttpStatus.OK)
	public String adminUpdateEmployee(@RequestParam("id") int id, Model model) {

		EmployeeDTO selectedEmployee = this.projectServiceImpl.getEmployeeById(id);

		List<String> jobRoleList = Arrays.asList("Mainframe developer", "Springboot developer", "Java developer",
				"Python developer", "React developer", "Cloud Engineer", "Data scientist");

		String selectedJobRole = selectedEmployee.getRole();

		List<String> jobLevelList = Arrays.asList("JL1", "JL2", "JL3", "JL4", "JL5", "JL6", "JL7");

		String selectedJobLevel = selectedEmployee.getJob_level();

		List<String> unitList = Arrays.asList("Healthcare", "Banking", "Cyber security", "Insurance",
				"Application Development", "Automotive", "Aerospace and Defense");

		String selectedUnit = selectedEmployee.getUnit();

		List<String> officeList = Arrays.asList("Pune Hinjewadi", "Pune Magarpatta", "Mumbai", "Nagpur", "Chennai",
				"Bengaluru", "Hydrabad", "Kolkatta");

		String selectedOffice = selectedEmployee.getOffice();

		model.addAllAttributes(Map.of("jobRoleList", jobRoleList, "selectedJobRole", selectedJobRole, "jobLevelList",
				jobLevelList, "selectedJobLevel", selectedJobLevel, "unitList", unitList, "selectedUnit", selectedUnit,
				"officeList", officeList, "selectedOffice", selectedOffice, "selectedEmployee", selectedEmployee));

		return "update-employee";

	}

	// ************ Employee handlers are below ***********//

	@GetMapping(value = "/employee-login")
	@ResponseStatus(value = HttpStatus.OK)
	public String employeeLogIn() {
		return "employee-login";
	}

	@PostMapping(value = "/employee-login")
	@ResponseStatus(value = HttpStatus.OK)
	public String employeeLoginSuccess(@ModelAttribute("employeeLogin") EmployeeLogin employeeLogin, Model model) {
		
		
		
		try {
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();
			List<EmployeeDTO> employeesFromDatabase = this.projectServiceImpl.getAllEmployees();

			if (employeeLogin.getEmail().isEmpty() || employeeLogin.getPassword().isEmpty()) {
				throw new NullPointerException("Email or Password cannot be null !");
			} else if (employeesFromDatabase == null) {
				throw new EmployeeNotFoundException("Employee not found!");
			}

			for (EmployeeDTO employeeDTO : employeesFromDatabase) {
				System.out.println(employeeLogin.getEmail()+"  "+employeeDTO.getEmail());
				System.out.println(employeeLogin.getPassword()+"  "+employeeDTO.getPassword());
				if (employeeLogin.getEmail().equals(employeeDTO.getEmail())
						&& passwordEncoder.matches(employeeLogin.getPassword(), employeeDTO.getPassword())) {
					
					if (employeeDTO.getAccess() == 1) {
						model.addAttribute("s3ImageUrl", awsResourceUrl+"/images/login.jpg");
						model.addAttribute("s3VideoUrl", awsResourceUrl+"/videos/website_intro.mp4");
						return "home";
					} 
//					else {
//						//throw new EmployeeNotFoundException("Employee not have access");
//						throw new EmployeeNotFoundException("Employee doesn't exist !!");
//					}

				} 
//				else if (!(employeeLogin.getEmail().equals(employeeDTO.getEmail()))) {
//					System.out.println(employeeLogin.getEmail()+" "+employeeDTO.getEmail());
//					model.addAttribute("errorMessage", "Please enter correct email!");
//					return "employee-login";
//				} else if (employeeLogin.getEmail().equals(employeeDTO.getEmail())
//						&& !(passwordEncoder.matches(employeeLogin.getPassword(), employeeDTO.getPassword()))) {
//					model.addAttribute("errorMessage", "Please enter correct password!");
//					return "employee-login";
//				}

			}
			
			System.out.println();
			throw new EmployeeNotFoundException("Employee doesn't exist !!");
//			return "employee-login";

		} catch (NullPointerException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "employee-login";

		} catch (IllegalArgumentException e) {
			model.addAttribute("errorMessage", "Exception related BCryptPasswordEncoder occured !");
			return "employee-login";

		} catch (EmployeeNotFoundException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "employee-login";
		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "Please contact your helpdesk !");
			return "employee-login";
		}

	}

	@GetMapping(value = "/employee-login/reset-password")
	@ResponseStatus(value = HttpStatus.OK)
	public String employeeChangePassword() {
		return "forget-password";
	}

	@PostMapping(value = "/employee-login/reset-password")
	@ResponseStatus(value = HttpStatus.OK)
	public String employeeChangePasswordSuccess(
			@ModelAttribute("employeeUpdatePassword") EmployeeUpdatePassword employeeUpdatePassword, Model model) {

		try {

			if (Integer.toString(employeeUpdatePassword.getId()).isEmpty()
					|| employeeUpdatePassword.getPassword().isEmpty()
					|| employeeUpdatePassword.getConfirmPassword().isEmpty()) {
				throw new NullPointerException("Employee Id or Password cannot be null !");
			}

			EmployeeDTO employeeById = this.projectServiceImpl.getEmployeeById(employeeUpdatePassword.getId());
			// System.out.println(employeeById);
			BCryptPasswordEncoder passwordEncoder = new BCryptPasswordEncoder();

			if (employeeUpdatePassword.getPassword().equals(employeeUpdatePassword.getConfirmPassword())) {

				String encodePassword = passwordEncoder.encode(employeeUpdatePassword.getPassword());
				employeeById.setPassword(encodePassword);
				this.projectServiceImpl.updateEmployee(employeeById);
				model.addAttribute("errorMessage", "Password Updated Successfully !!");
				return "employee-login";

			} else {
				model.addAttribute("errorMessage", "Password and Confirm password not same !!");
				return "forget-password";
			}
		} catch (NullPointerException e) {
			model.addAttribute("errorMessage", e.getMessage());
			return "forget-password";

		} catch (IllegalArgumentException e) {
			// e.printStackTrace();
			model.addAttribute("errorMessage", "Exception related BCryptPasswordEncoder occured !");
			return "forget-password";

		} catch (NoSuchElementException e) {
			model.addAttribute("errorMessage", "Employee not found !");
			return "forget-password";

		} catch (Exception e) {
			e.printStackTrace();
			model.addAttribute("errorMessage", "Please contact your healpdesk !");
			return "forget-password";
		}

	}

	@GetMapping(value = "/home")
	@ResponseStatus(value = HttpStatus.OK)
	public String learnXHome(Model model) {
		model.addAttribute("s3ImageUrl", awsResourceUrl+"/images/login.jpg");
		model.addAttribute("s3VideoUrl", awsResourceUrl+"/videos/website_intro.mp4");
		return "home";
	}

	@GetMapping(value = "/home/kt-sessions")
	@ResponseStatus(value = HttpStatus.OK)
	public String learnXKtSessionsPage(Model model, @ModelAttribute("content") ArrayList<String> content) {

		if (content.isEmpty()) {
			content.add("This is the content of KT Session. Please read it carefully.");
		}

		model.addAttribute("topHeaders", "This is top header");
		return "kt-sessions";
	}

	@GetMapping("/home/kt-sessions/{id}")
	@ResponseStatus(value = HttpStatus.OK)
	public String learnXKtSessionsPages(@PathVariable("id") int option, Model model) {

		String topHeaders = "";
		String videos = "";
		String content = "";
		
		// ArrayList<String> content = new ArrayList<String>();

		if (option == 1) {
			topHeaders = "Java Introduction";

			content = "Java is a class-based, object-oriented programming language that is designed to have as few implementation dependencies as possible. It is intended to let application developers write once, and run anywhere (WORA), meaning that compiled Java code can run on all platforms that support Java without the need for recompilation. Java was first released in 1995 and is widely used for developing applications for desktop, web, and mobile devices. Java is known for its simplicity, robustness, and security features, making it a popular choice for enterprise-level applications.\r\n"
					+ "\r\n"
					+ "JAVA was developed by James Gosling at Sun Microsystems Inc in the year 1995 and later acquired by Oracle Corporation. It is a simple programming language. Java makes writing, compiling, and debugging programming easy. It helps to create reusable code and modular programs. Java is a class-based, object-oriented programming language and is designed to have as few implementation dependencies as possible. A general-purpose programming language made for developers to write once run anywhere that is compiled Java code can run on all platforms that support Java. Java applications are compiled to byte code that can run on any Java Virtual Machine. The syntax of Java is similar to c/c++.\r\n"
					+ "\r\n"
					+ "History: Java’s history is very interesting. It is a programming language created in 1991. James Gosling, Mike Sheridan, and Patrick Naughton, a team of Sun engineers known as the Green team initiated the Java language in 1991. Sun Microsystems released its first public implementation in 1996 as Java 1.0. It provides no-cost -run-times on popular platforms. Java1.0 compiler was re-written in Java by Arthur Van Hoff to strictly comply with its specifications. With the arrival of Java 2, new versions had multiple configurations built for different types of platforms.";

			videos = awsResourceUrl+"/videos/java introduction.mp4";

		} else if (option == 2) {
			topHeaders = "Lets create Java project";

			content = "Java is a class-based, object-oriented programming language that is designed to have as few implementation dependencies as possible. It is intended to let application developers write once, and run anywhere (WORA), meaning that compiled Java code can run on all platforms that support Java without the need for recompilation. Java was first released in 1995 and is widely used for developing applications for desktop, web, and mobile devices. Java is known for its simplicity, robustness, and security features, making it a popular choice for enterprise-level applications.\\r\\n\"\r\n"
					+ "	JAVA was developed by James Gosling at Sun Microsystems Inc in the year 1995 and later acquired by Oracle Corporation. It is a simple programming language. Java makes writing, compiling, and debugging programming easy. It helps to create reusable code and modular programs. Java is a class-based, object-oriented programming language and is designed to have as few implementation dependencies as possible. A general-purpose programming language made for developers to write once run anywhere that is compiled Java code can run on all platforms that support Java. Java applications are compiled to byte code that can run on any Java Virtual Machine. The syntax of Java is similar to c/c++.\\r\\n\"\r\n"
					+ "	History: Java’s history is very interesting. It is a programming language created in 1991. James Gosling, Mike Sheridan, and Patrick Naughton, a team of Sun engineers known as the Green team initiated the Java language in 1991. Sun Microsystems released its first public implementation in 1996 as Java 1.0. It provides no-cost -run-times on popular platforms. Java1.0 compiler was re-written in Java by Arthur Van Hoff to strictly comply with its specifications. With the arrival of Java 2, new versions had multiple configurations built for different types of platforms.";

			videos = awsResourceUrl+"/videos/Eclipse - Create Java Project.mp4";

		} else if (option == 3) {
			topHeaders = "What is springMVC";

			content = "A Spring MVC is a Java framework which is used to build web applications. It follows the Model-View-Controller design pattern. It implements all the basic features of a core spring framework like Inversion of Control, Dependency Injection.\r\n"
					+ "\r\n"
					+ "A Spring MVC provides an elegant solution to use MVC in spring framework by the help of DispatcherServlet. Here, DispatcherServlet is a class that receives the incoming request and maps it to the right resource such as controllers, models, and views.\r\n"
					+ "\r\n" + "Spring Web Model-View-Controller\r\n" + "Spring MVC Tutorial\r\n"
					+ "Model - A model contains the data of the application. A data can be a single object or a collection of objects.\r\n"
					+ "Controller - A controller contains the business logic of an application. Here, the @Controller annotation is used to mark the class as the controller.\r\n"
					+ "View - A view represents the provided information in a particular format. Generally, JSP+JSTL is used to create a view page. Although spring also supports other view technologies such as Apache Velocity, Thymeleaf and FreeMarker.\r\n"
					+ "Front Controller - In Spring Web MVC, the DispatcherServlet class works as the front controller. It is responsible to manage the flow of the Spring MVC application.";

			videos = awsResourceUrl+"/videos/Introduction to Spring MVC.mp4";

		} else if (option == 4) {
			topHeaders = "What is Spring Boot";

			content = "Spring Boot offers a fast way to build applications. It looks at your classpath and at the beans you have configured, makes reasonable assumptions about what you are missing, and adds those items. With Spring Boot, you can focus more on business features and less on infrastructure.\r\n"
					+ "\r\n" + "The following examples show what Spring Boot can do for you:\r\n" + "\r\n"
					+ "Is Spring MVC on the classpath? There are several specific beans you almost always need, and Spring Boot adds them automatically. A Spring MVC application also needs a servlet container, so Spring Boot automatically configures embedded Tomcat.\r\n"
					+ "\r\n"
					+ "Is Jetty on the classpath? If so, you probably do NOT want Tomcat but instead want embedded Jetty. Spring Boot handles that for you.\r\n"
					+ "\r\n"
					+ "Is Thymeleaf on the classpath? If so, there are a few beans that must always be added to your application context. Spring Boot adds them for you.\r\n"
					+ "\r\n"
					+ "These are just a few examples of the automatic configuration Spring Boot provides. At the same time, Spring Boot does not get in your way. For example, if Thymeleaf is on your path, Spring Boot automatically adds a SpringTemplateEngine to your application context. But if you define your own SpringTemplateEngine with your own settings, Spring Boot does not add one. This leaves you in control with little effort on your part.";

			videos = awsResourceUrl+"/videos/Introduction to Spring Boot.mp4";

		} else if (option == 5) {
			topHeaders = "Git commands and Github";

			content = "A repository, or Git project, encompasses the entire collection of files and folders associated with a project, along with each file's revision history. The file history appears as snapshots in time called commits. The commits can be organized into multiple lines of development called branches. Because Git is a DVCS, repositories are self-contained units and anyone who has a copy of the repository can access the entire codebase and its history. Using the command line or other ease-of-use interfaces, a Git repository also allows for: interaction with the history, cloning the repository, creating branches, committing, merging, comparing changes across versions of code, and more.\r\n"
					+ "\r\n"
					+ "Through platforms like GitHub, Git also provides more opportunities for project transparency and collaboration. Public repositories help teams work together to build the best possible final product.";

			videos = awsResourceUrl+"/videos/Git && Github.mp4";
		}

		model.addAttribute("topHeaders", topHeaders);
		model.addAttribute("content", content);
		model.addAttribute("s3VideoUrl", videos);

		return "kt-sessions";
	}

	@GetMapping(value = "/home/email-templates")
	@ResponseStatus(value = HttpStatus.OK)
	public String learnXEmailTemplates(Model model) {
		model.addAttribute("activeLink", "emailtemplates");
		return "email-templates";
	}

	@GetMapping(value = "/home/repetitive-tasks")
	@ResponseStatus(value = HttpStatus.OK)
	public String learnXRepetativeTasks(Model model) {
		model.addAttribute("activeLink", "repetitivetasks");
		
		String img1_path = awsResourceUrl+"/images/slide1.jpg";
		String img2_path = awsResourceUrl+"/images/slide2.jpg";
		String img3_path = awsResourceUrl+"/images/slide3.jpg";

		HashMap<Integer, String> images = new HashMap<Integer, String>();
		images.put(1, img1_path);
		images.put(2, img2_path);
		images.put(3, img3_path);

		model.addAttribute("s3ImageUrl", images);

		return "repetitive-tasks";
	}

	@GetMapping(value = "/home/repetitive-tasks/{option}")
	@ResponseStatus(value = HttpStatus.OK)
	public String learnXRepetitiveTasksPages(@PathVariable int option, Model model) {

		String topheaders = "";
		ArrayList<String> steps = new ArrayList<String>();
		HashMap<Integer, String> images = new HashMap<Integer, String>();
		
		if (option == 1) {

			topheaders = "Creating Java Project";

			images.put(1, awsResourceUrl+"/images/repetitive_task/create_Java1.jpeg");
			images.put(2, awsResourceUrl+"/images/repetitive_task/create_Java2.jpeg");
			images.put(3, awsResourceUrl+"/images/repetitive_task/create_Java3.jpeg");
			images.put(4, awsResourceUrl+"/images/repetitive_task/create_Java4.jpeg");
			images.put(5, awsResourceUrl+"/images/repetitive_task/create_Java5.jpeg");

			steps.add("Step 1 : Click on File");
			steps.add("Step 2 : New -> Java Project");
			steps.add("Step 3 : Enter Project Name");
			steps.add("Step 4 : Click on Finish");
			steps.add("Step 5 : Project gets created eclipse workspace");

		} else if (option == 2) {

			topheaders = "Creating Spring MVC Project";

			
			
			images.put(1, awsResourceUrl+"/images/repetitive_task/mvc1.jpeg");
			images.put(2, awsResourceUrl+"/images/repetitive_task/mvc2.jpeg");
			images.put(3, awsResourceUrl+"/images/repetitive_task/mvc3.jpeg");
			images.put(4, awsResourceUrl+"/images/repetitive_task/mvc4.jpeg");
			images.put(5, awsResourceUrl+"/images/repetitive_task/mvc5.jpeg");
			images.put(6, awsResourceUrl+"/images/repetitive_task/mvc6.jpeg");
			images.put(7, awsResourceUrl+"/images/repetitive_task/mvc7.jpeg");
			images.put(8, awsResourceUrl+"/images/repetitive_task/mvc8.jpeg");
			images.put(9, awsResourceUrl+"/images/repetitive_task/mvc9.jpeg");

			steps.add("Step 1 : Open Eclipse");
			steps.add("Step 2 : Click on file");
			steps.add("Step 3 : New -> Others");
			steps.add("Step 4 : Maven -> Maven Project");
			steps.add("Step 5 : Next-> Choose correct workspace location to create this project");
			steps.add("Step 6 : Next -> select Artifact Id as 'maven-archetype-webapp' ");
			steps.add("Step 7 : Next -> Type correct Group Id and Artifact Id -> Finish");
			steps.add("Step 8 : Configure 'web.xml' for Dispatcher Servlet");
			steps.add("Step 9 : Create and configure servelt.xml for base package and viewResolver");
			steps.add("Step 10 : Add server Runtime library in build path");
			steps.add("Step 11 : Add Spring MVC library and plugin(maven-war-plugin) in 'pom.xml'");
			steps.add("Step 12 : Create controller with base package and use @controller annotation");
			steps.add("Step 13 : Add request ampping for index page");
			steps.add("Step 14 : Restart the Tomcat server after changes");

		} else if (option == 3) {

			topheaders = "How to add Maven dependancies in project";

			images.put(1, awsResourceUrl+"/images/repetitive_task/maven_1.jpeg");
			images.put(2, awsResourceUrl+"/images/repetitive_task/maven_2.jpeg");
			images.put(3, awsResourceUrl+"/images/repetitive_task/maven_3.jpeg");
			images.put(4, awsResourceUrl+"/images/repetitive_task/maven_4.jpeg");
			images.put(5, awsResourceUrl+"/images/repetitive_task/maven_5.jpeg");
			images.put(6, awsResourceUrl+"/images/repetitive_task/maven_6.jpeg");

			steps.add("Step 1 : Search for maven repository and click on first link of maven repository");
			steps.add("Step 2 : Search for spring MVC");
			steps.add("Step 3 : Click on first spring web MVC dependency");
			steps.add("Step 4 : Choose version with have high usage because it will have less vulnerability");
			steps.add("Step 5 : Click on Maven tab");
			steps.add("Step 6 : Right click on dependency");
			steps.add("Step 7 : Copy the dependency");
			steps.add("Step 8 : paste the dependecy in pom.xml file of springMVC project");

		} else if (option == 4) {

			topheaders = "How to install postman for API testing";

			images.put(1, awsResourceUrl+"/images/repetitive_task/postman1.jpeg");
			images.put(2, awsResourceUrl+"/images/repetitive_task/postman2.jpeg");
			images.put(3, awsResourceUrl+"/images/repetitive_task/postman3.jpeg");

			steps.add("Step 1 : Search for Postman in chrome and click on download link");
			steps.add("Step 2 : Click on windows 64 button");
			steps.add("Step 3 : Download will automatically gets started");
			steps.add("Step 4 : Install the software after it gets downloaded");

		} else if (option == 5) {

			topheaders = "Test API in postman";

			images.put(1, awsResourceUrl+"/images/repetitive_task/test1.jpeg");
			images.put(2, awsResourceUrl+"/images/repetitive_task/test2.jpeg");
			images.put(3, awsResourceUrl+"/images/repetitive_task/test3.jpeg");
			images.put(4, awsResourceUrl+"/images/repetitive_task/test4.jpeg");
			images.put(5, awsResourceUrl+"/images/repetitive_task/test5.jpeg");
			images.put(6, awsResourceUrl+"/images/repetitive_task/test6.jpeg");
			images.put(7, awsResourceUrl+"/images/repetitive_task/test7.jpeg");
			images.put(8, awsResourceUrl+"/images/repetitive_task/test8.jpeg");

			steps.add("Step 1 : Create new collection");
			steps.add("Step 2 : Name the collection as springMVCTest");
			steps.add("Step 3 : Create new tab in collection");
			steps.add("Step 4 : Enter the URL for specific API");
			steps.add("Step 5 : Set HTTP method to GET");
			steps.add("Step 6 : Hit the request by pressing Send button");
			steps.add("Step 7 : Response in form JSON format in body");

		}

		// model.addAttribute("images",images);
		// System.out.println("controller 1");

		model.addAttribute("topHeaders", topheaders);
		model.addAttribute("stepsData", steps);
		model.addAttribute("s3ImageUrl", images);

		return "repetitive-tasks";
	}

	// This below is for data flow HTML <- JS <- Spring boot handler <-Database
//	@GetMapping(value = "/api/employee")
//	public ResponseEntity<List<Employee>> getAllEmployees() {
//		return new ResponseEntity<List<Employee>>(this.projectServiceImpl.getAllEmployees(), HttpStatus.OK);
//	}

}