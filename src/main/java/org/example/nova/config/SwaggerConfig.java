package org.example.nova.config;


import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(new Info()
                              .title("KDT NOVA API")
                              .description("HDC랩스 & KDT NOVA API 문서")
                              .version("v1.0"));
    }
}
