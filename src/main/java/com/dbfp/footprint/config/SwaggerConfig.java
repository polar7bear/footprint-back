package com.dbfp.footprint.config;

import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new io.swagger.v3.oas.models.info.Info()
                                .title("IDE API DOCS")
                                .description("")
                )
//                .components(
//                        new Components()
//                                .addSecuritySchemes(
//                                        "bearer token",
//                                        new io.swagger.v3.oas.models.security.SecurityScheme()
//                                                .type(SecurityScheme.Type.HTTP)
//                                                .scheme("bearer")
//                                                .bearerFormat("JWT")
//                                )
//                )
                ;
    }

    @Bean
    public GroupedOpenApi publicApi(){
        return GroupedOpenApi.builder()
                .group("public api")
                .pathsToMatch("/api/**")
                .build();
    }

//    @Bean
//    public GroupedOpenApi adminApi(){
//        return GroupedOpenApi.builder()
//                .group("admin api")
//                .pathsToMatch("/admin/**")
//                .addOpenApiMethodFilter(method -> method.isAnnotationPresent())
//                .build();
//    }

}