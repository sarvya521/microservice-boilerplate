package com.unify.organization.controller;

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

import com.unify.organization.model.Department;
import com.unify.organization.model.Employee;
import com.unify.organization.model.Organization;
import com.unify.organization.repository.OrganizationRepository;

@RestController
@RequestMapping("/organization")
public class OrganizationController {

	private static final Logger LOGGER = LoggerFactory.getLogger(OrganizationController.class);

	@Autowired
	OrganizationRepository repository;

	@Autowired
	BuildProperties buildProperties;

	@Autowired
	RestTemplate restTemplate;

	@GetMapping("/ping")
	public String ping() {
		LOGGER.info("Ping: name={}, version={}", buildProperties.getName(), buildProperties.getVersion());
		String empResponse = restTemplate.getForObject("http://employee-service:8080/employee/ping", String.class);
		LOGGER.info("Calling Employee: response={}", empResponse);
		String deptResponse = restTemplate.getForObject("http://department-service:8080/department/ping", String.class);
		LOGGER.info("Calling Department: response={}", deptResponse);
		return buildProperties.getName() + ":" + buildProperties.getVersion() + ". Calling Employee... " + empResponse+ ". Calling Department... " + deptResponse;
	}

	@PostMapping
	public Organization add(@RequestBody Organization organization) {
		LOGGER.info("Organization add: {}", organization);
		return repository.add(organization);
	}

	@GetMapping
	public List<Organization> findAll() {
		LOGGER.info("Organization find");
		return repository.findAll();
	}

	@GetMapping("/{id}")
	public Organization findById(@PathVariable("id") Long id) {
		LOGGER.info("Organization find: id={}", id);
		return repository.findById(id);
	}

	@GetMapping("/{id}/with-departments")
	public Organization findByIdWithDepartments(@PathVariable("id") Long id) {
		LOGGER.info("Organization find: id={}", id);
		Organization organization = repository.findById(id);
		organization.setDepartments(restTemplate.exchange(
				  "http://department-service:8080/department/organization/{organizationId}",
				  HttpMethod.GET,
				  null,
				  new ParameterizedTypeReference<List<Department>>(){},
				  organization.getId()).getBody());
		return organization;
	}

	@GetMapping("/{id}/with-departments-and-employees")
	public Organization findByIdWithDepartmentsAndEmployees(@PathVariable("id") Long id) {
		LOGGER.info("Organization find: id={}", id);
		Organization organization = repository.findById(id);
		organization.setDepartments(restTemplate.exchange(
				  "http://department-service:8080/department/organization/{organizationId}/with-employees",
				  HttpMethod.GET,
				  null,
				  new ParameterizedTypeReference<List<Department>>(){},
				  organization.getId()).getBody());
		return organization;
	}

	@GetMapping("/{id}/with-employees")
	public Organization findByIdWithEmployees(@PathVariable("id") Long id) {
		LOGGER.info("Organization find: id={}", id);
		Organization organization = repository.findById(id);
		organization.setEmployees(restTemplate.exchange(
				  "http://employee-service:8080/employee/organization/{organizationId}",
				  HttpMethod.GET,
				  null,
				  new ParameterizedTypeReference<List<Employee>>(){},
				  organization.getId()).getBody());
		return organization;
	}

}
