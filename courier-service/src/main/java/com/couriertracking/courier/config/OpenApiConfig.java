package com.couriertracking.courier.config;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Contact;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.info.License;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class OpenApiConfig {

        @Bean
        public OpenAPI courierServiceOpenAPI() {
                return new OpenAPI()
                                .info(new Info()
                                                .title("Courier Service API")
                                                .description("API for courier location tracking service")
                                                .version("1.0.0")
                                                .contact(new Contact()
                                                                .name("Courier Tracking System")
                                                                .email("m.furkangulsen@gmail.com"))
                                                .license(new License()
                                                                .name("MIT License")
                                                                .url("https://opensource.org/licenses/MIT")));
        }
}