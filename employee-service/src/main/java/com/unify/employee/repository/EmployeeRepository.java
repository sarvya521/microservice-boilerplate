package com.unify.employee.repository;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.stereotype.Repository;

import com.unify.employee.model.Employee;

@Repository
public interface EmployeeRepository extends MongoRepository<Employee, Long>{

	List<Employee> findByDepartmentId(Long departmentId);
	
	List<Employee> findByOrganizationId(Long organizationId);
}
