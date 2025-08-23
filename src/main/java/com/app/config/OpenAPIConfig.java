package com.app.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import io.swagger.v3.oas.models.servers.Server;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.List;

@Configuration
public class OpenAPIConfig {

    @Value("${app.openapi.endpoint}")
    private String applicationEndpoint;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(applicationEndpoint);
        devServer.setDescription("Server Endpoint");

        Contact contact = new Contact();
        contact.setEmail("faiz.krm@gmail.com");
        contact.setName("Faiz Akram");
        contact.setUrl("https://www.faizkram.com");

        License mitLicense = new License().name("Development License").url("https://www.faizkram.com");

        Info info = new Info()
                .title("Tutorial Management API")
                .version("1.0")
                .contact(contact)
                .description("This API exposes endpoints to manage tutorials.").termsOfService("https://www.faizkram.com")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(devServer));
    }
}
