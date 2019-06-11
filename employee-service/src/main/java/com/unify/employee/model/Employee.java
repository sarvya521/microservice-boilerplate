package com.unify.employee.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.mapping.Document;
import org.springframework.data.mongodb.core.mapping.Field;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import lombok.ToString;

@Document(collection = "employee")
@Data
@ToString
@AllArgsConstructor
@NoArgsConstructor
public class Employee {
    @Id
	private String id;
	private Long organizationId;
	private Long departmentId;
	@Field("name")
	private String name;
}