package com.herman.herman.config;

import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import io.swagger.v3.oas.models.Components;
import io.swagger.v3.oas.models.OpenAPI;
import io.swagger.v3.oas.models.info.Info;
import io.swagger.v3.oas.models.security.SecurityRequirement;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import static io.swagger.v3.oas.models.security.SecurityScheme.In.HEADER;
import static io.swagger.v3.oas.models.security.SecurityScheme.Type.APIKEY;

@Configuration
@SecurityScheme(name = "JWT", type = SecuritySchemeType.HTTP, scheme = "bearer", bearerFormat = "JWT", in = SecuritySchemeIn.HEADER)
public class SwaggerConfig {
  @Bean
  public OpenAPI customOpenApi() {
    return new OpenAPI()
        .info(new Info()
            .title("HERMAN")
            .version("1.0")
            .description("Herman testing api"))
        .components(
            new Components()
                .addSecuritySchemes(
                    "GRANTED-ACCESS-TOKEN", new io.swagger.v3.oas.models.security.SecurityScheme().type(APIKEY).in(HEADER).name("AUTHORIZATION")))
        .addSecurityItem(
            new SecurityRequirement()
                .addList("GRANTED-ACCESS-TOKEN"));
  }
}
