package com.unify.employee;

import java.util.List;

import org.springframework.boot.ApplicationRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.annotation.Bean;
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories;
import org.springframework.web.client.RestTemplate;

import com.unify.employee.model.Employee;
import com.unify.employee.repository.EmployeeRepository;

import springfox.documentation.builders.ApiInfoBuilder;
import springfox.documentation.builders.PathSelectors;
import springfox.documentation.builders.RequestHandlerSelectors;
import springfox.documentation.spi.DocumentationType;
import springfox.documentation.spring.web.plugins.Docket;
import springfox.documentation.swagger2.annotations.EnableSwagger2;

@SpringBootApplication
@EnableMongoRepositories
@EnableSwagger2
public class EmployeeServiceApplication {

    public static void main(String[] args) {
        SpringApplication.run(EmployeeServiceApplication.class, args);
    }

    @Bean
    public Docket swaggerPersonApi10() {
        return new Docket(DocumentationType.SWAGGER_2).select()
                .apis(RequestHandlerSelectors.basePackage("com.unify.employee.controller")).paths(PathSelectors.any())
                .build().apiInfo(new ApiInfoBuilder().version("1.0").title("Employee API")
                        .description("Documentation Employee API v1.0").build());
    }

    @Bean
    ApplicationRunner init(EmployeeRepository repository) {
        final Employee[] employees = new Employee[] { new Employee(null, 1L, 1L, "John Smith"),
                new Employee(null, 1L, 1L, "Darren Hamilton"), new Employee(null, 1L, 1L, "Tom Scott"),
                new Employee(null, 1L, 2L, "Anna London"), new Employee(null, 1L, 2L, "Patrick Dempsey"),
                new Employee(null, 2L, 3L, "Kevin Price"), new Employee(null, 2L, 3L, "Ian Scott"),
                new Employee(null, 2L, 3L, "Andrew Campton"), new Employee(null, 2L, 4L, "Steve Franklin"),
                new Employee(null, 2L, 4L, "Elisabeth Smith"), };
        repository.deleteAll();
        return args -> List.of(employees).forEach(repository::save);
    }

    @Bean
    RestTemplate restTemplate() {
        return new RestTemplate();
    }
}
