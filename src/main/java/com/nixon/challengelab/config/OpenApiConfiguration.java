package com.nixon.challengelab.config;

import io.swagger.v3.oas.annotations.OpenAPIDefinition;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeIn;
import io.swagger.v3.oas.annotations.enums.SecuritySchemeType;
import io.swagger.v3.oas.annotations.info.Contact;
import io.swagger.v3.oas.annotations.info.Info;
import io.swagger.v3.oas.annotations.security.SecurityRequirement;
import io.swagger.v3.oas.annotations.security.SecurityScheme;
import org.springframework.context.annotation.Configuration;

@Configuration
@OpenAPIDefinition(
        info = @Info(
                title = "Challenge Lab",
                version = "v1.0.0",
                contact = @Contact(
                        name = "Nixon Sadique",
                        email = "nixonsadique2005@gmail.com"
                )
        ),
        security = {
                @SecurityRequirement(name = "Bearer Authentication")
        }
)
@SecurityScheme(
        name = "Bearer Authentication",
        type = SecuritySchemeType.HTTP,
        scheme = "bearer",
        bearerFormat = "JWT",
        in = SecuritySchemeIn.HEADER,
        description = """
                JWT Bearer authentication.
                
                Obtain a token from the authentication endpoint and include it in
                the Authorization header.
                
                Example:
                Authorization: Bearer <your-jwt-token>
                """
)
class OpenApiConfiguration {

}
