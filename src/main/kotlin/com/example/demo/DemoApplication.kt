package com.example.demo

import io.swagger.v3.oas.annotations.OpenAPIDefinition
import io.swagger.v3.oas.annotations.info.Info
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication
@SpringBootApplication
@OpenAPIDefinition(info = Info(title = "Album API", version = "1.0", description = "API for managing albums and photos"))
class DemoApplication

fun main(args: Array<String>) {
	runApplication<DemoApplication>(*args)
}
