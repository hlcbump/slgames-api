package com.slgames.store.swagger;

import java.util.Collections;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import io.swagger.v3.oas.models.security.SecurityScheme;

@Configuration
public class SwaggerConfig {
	@Bean
    OpenAPI config() {
    	return new OpenAPI()
    			.info(new Info()
    					.title("Slgames-API")
    					.description("REST API of a Online Game Store")
    					.contact(new Contact()
    							.name("Nicolas")
    							.email("limabritopro@gmail.com")
    							)
    					.license(new License()))
    			
    			.components(new Components()
    					.addSecuritySchemes("bearerAuth", new SecurityScheme()
    							.type(SecurityScheme.Type.HTTP)
    							.scheme("bearer")
    							.bearerFormat("JWT")))
    			.security(Collections.singletonList(new SecurityRequirement().addList("bearerAuth")));
    }
	
	

}
