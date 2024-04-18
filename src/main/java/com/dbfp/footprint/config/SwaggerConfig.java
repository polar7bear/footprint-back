package com.dbfp.footprint.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.servers.Server;
import io.swagger.v3.oas.models.OpenAPI;
import org.springdoc.core.models.GroupedOpenApi;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@OpenAPIDefinition(
        servers = {
                @Server(url = "https://ke4f765103c24a.user-app.krampoline.com/", description = "prod"),
                @Server(url = "http://localhost:8080", description = "local")
        }
)
@Configuration
public class SwaggerConfig {

    @Bean
    public OpenAPI openAPI() {
        return new OpenAPI()
                .info(
                        new io.swagger.v3.oas.models.info.Info()
                                .title("DFBF Footprint API")
                                .description("DogFoot-BirdFoot: GoormthonTraining 3rd Full-Stack Final Project")
                                .version("1.0.0")
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