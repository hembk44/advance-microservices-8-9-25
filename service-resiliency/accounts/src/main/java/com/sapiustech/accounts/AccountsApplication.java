package com.sapiustech.accounts;

import com.sapiustech.accounts.dto.AccountsContactInfoDto;
import io.swagger.v3.oas.annotations.ExternalDocumentation;
import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.info.License;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.boot.context.properties.EnableConfigurationProperties;
import org.springframework.cloud.openfeign.EnableFeignClients;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@SpringBootApplication
@EnableFeignClients
@OpenAPIDefinition(
		info = @Info(
				title = "Accounts microservice REST API Documentation",
				description = "Sapius Technologies LLC Accounts microservice REST API Documentation",
				version = "v1",
				contact = @Contact(
						name = "Marcus Jones",
						email = "marcus.jones@sapiustechnologies.com",
						url = "https://www.sapiustechnologies.com"
				),
				license = @License(
						name = "Apache 2.0",
						url = "https://www.sapiustechnologies.com"
				)
		),
		externalDocs = @ExternalDocumentation(
				description =  "Sapius Accounts microservice REST API Documentation",
				url = "https://www.sapiustechnologies.com/swagger-ui.html"
		)
)
@EnableConfigurationProperties(value = {AccountsContactInfoDto.class})
@EnableJpaAuditing(auditorAwareRef = "auditAwareImpl")
public class AccountsApplication {

	public static void main(String[] args) {
		SpringApplication.run(AccountsApplication.class, args);
	}

}
