package com.hacken.test_task.configuration;

import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerOpenApiConfiguration {

    @Bean
    public OpenAPI swaggerConfigOpenAPI() {
        return new OpenAPI()
                .info(new Info()
                        .title("Test Task Specification")
                        .description("Test Task Specification")
                        .version("v0.0.1"));
    }
}
