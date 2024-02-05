package com.springboot.LearnX.Repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import com.springboot.LearnX.Entity.Employee;

public interface EmployeeRepositories extends JpaRepository<Employee, Integer> {

	public List<Employee> findByNameContaining(String name);

	@Query(value = "SELECT * FROM userdata WHERE access=1", nativeQuery = true)
	public List<Employee> findEmployeeListByAccessProvided();

//	@Query(value = "SELECT * FROM userdata WHERE email=:email", nativeQuery = true)
//	public Employee findByEmail(@Param("email") String email);

//	@Query(value = "SELECT * FROM userdata WHERE email=':email'", nativeQuery = true)
//	public Employee findByEmail(@Param("email") String email);

//	@Query(value = "SELECT u FROM userdata u WHERE u.name =:name AND u.job_level=:job_level AND u.role=:role AND u.unit=:unit AND u.office=:office", nativeQuery = true)
//	public List<User> findUsers(@Param("name") String name, @Param("job_level") String job_level,
//			@Param("role") String role, @Param("unit") String unit, @Param("office") String office);

//	@Query(value = "SELECT * FROM userdata WHERE " +
//		    "(:name IS NULL OR name LIKE '%:name%') or " +
//		    "(:job_level IS NULL OR job_level LIKE '%:job_level%') or " +
//		    "(:role IS NULL OR role LIKE '%:role%') or " +
//		    "(:unit IS NULL OR unit LIKE '%:unit%') or " +
//		    "(:office IS NULL OR office LIKE '%:office%')", nativeQuery = true)
//
//	public List<User> findUsersByParameters(@Param("name") String name, @Param("job_level") String job_level,
//			@Param("role") String role, @Param("unit") String unit, @Param("office") String office);

	//
//	public List<User> findByNameAndRole(String name, String role);

//	@Query(value = "SELECT * FROM userdata WHERE " + "name LIKE CONCAT('%', :name, '%') OR "
//			+ "job_level LIKE CONCAT('%', :job_level, '%') OR " + "role LIKE CONCAT('%', :role, '%') OR "
//			+ "unit LIKE CONCAT('%', :unit, '%') OR " + "office LIKE CONCAT('%', :office, '%')", nativeQuery = true)
//	public List<User> findEmployeeByAnyParameter(@Param("name") String name, @Param("job_level") String job_level,
//			@Param("role") String role, @Param("unit") String unit, @Param("office") String office);

//	public List<User> findByNameOrJob_levelOrRoleOrUnitOrOffice(String name, String job_level, String role, String Unit,
//			String office);

}
