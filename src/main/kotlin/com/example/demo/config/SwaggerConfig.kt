package com.example.demo.config

import io.swagger.v3.oas.models.OpenAPI
import io.swagger.v3.oas.models.info.Contact
import io.swagger.v3.oas.models.info.Info
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class SwaggerConfig {

    @Bean
    fun customOpenApi(): OpenAPI {
        return OpenAPI()
            .info(
                Info()
                    .title("API de álbumes de fotos")
                    .version("1.0")
                    .description("API para mostrar álbumes y fotos utilizando JSON Placeholder")
                    .contact(
                        Contact()
                            .name("Ángel David Morales")
                    )
            )
    }
}