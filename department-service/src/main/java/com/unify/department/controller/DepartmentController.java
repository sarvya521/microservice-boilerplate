package com.unify.department.controller;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.info.BuildProperties;
import org.springframework.core.ParameterizedTypeReference;
import org.springframework.http.HttpMethod;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.client.RestTemplate;

import com.unify.department.model.Department;
import com.unify.department.model.Employee;
import com.unify.department.repository.DepartmentRepository;

@RestController
@RequestMapping("/department")
public class DepartmentController {

	private static final Logger LOGGER = LoggerFactory.getLogger(DepartmentController.class);

	@Autowired
	DepartmentRepository repository;

	@Autowired
	BuildProperties buildProperties;

	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/ping")
	public String ping() {
		LOGGER.info("Ping: name={}, version={}", buildProperties.getName(), buildProperties.getVersion());
		String empResponse = restTemplate.getForObject("http://employee-service:8080/employee/ping", String.class);
		LOGGER.info("Calling Employee: response={}", empResponse);
		return buildProperties.getName() + ":" + buildProperties.getVersion() + ". Calling... " + empResponse;
	}

	@PostMapping("/")
	public Department add(@RequestBody Department department) {
		LOGGER.info("Department add: {}", department);
		return repository.add(department);
	}

	@GetMapping("/{id}")
	public Department findById(@PathVariable("id") Long id) {
		LOGGER.info("Department find: id={}", id);
		return repository.findById(id);
	}

	@GetMapping("/")
	public List<Department> findAll() {
		LOGGER.info("Department find");
		return repository.findAll();
	}

	@GetMapping("/organization/{organizationId}")
	public List<Department> findByOrganization(@PathVariable("organizationId") Long organizationId) {
		LOGGER.info("Department find: organizationId={}", organizationId);
		return repository.findByOrganization(organizationId);
	}

	@GetMapping("/organization/{organizationId}/with-employees")
	public List<Department> findByOrganizationWithEmployees(@PathVariable("organizationId") Long organizationId) {
		LOGGER.info("Department find: organizationId={}", organizationId);
		List<Department> departments = repository.findByOrganization(organizationId);
		departments.forEach(d -> d.setEmployees(restTemplate.exchange(
				  "http://employee-service:8080/employee/department/{departmentId}",
				  HttpMethod.GET,
				  null,
				  new ParameterizedTypeReference<List<Employee>>(){},
				  d.getId()).getBody()));
		return departments;
	}

}
