package com.example.springsocial.config;

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

    @Value("${project.openapi.dev-url}")
    private String devUrl;

    @Value("${project.openapi.prod-url}")
    private String prodUrl;

    @Bean
    public OpenAPI myOpenAPI() {
        Server devServer = new Server();
        devServer.setUrl(devUrl);
        devServer.setDescription("Server URL in Development environment");

        Server prodServer = new Server();
        prodServer.setUrl(prodUrl);
        prodServer.setDescription("Server URL in Production environment");

        Contact contact = new Contact();
        contact.setEmail("lethop@gmail.com");
        contact.setName("LetHop");
        contact.setUrl("https://www.lethop.com");

        License mitLicense = new License().name("MIT License").url("https://lethop.com/licenses/mit/");

        Info info = new Info()
                .title("lethop Management API")
                .version("1.0")
                .contact(contact)
                .description("This API documentation.").termsOfService("https://www.lethop.com")
                .license(mitLicense);

        return new OpenAPI().info(info).servers(List.of(devServer, prodServer));
    }

}
