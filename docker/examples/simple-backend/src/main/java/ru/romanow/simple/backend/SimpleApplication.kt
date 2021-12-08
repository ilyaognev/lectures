package ru.romanow.simple.backend

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.CrossOrigin
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RestController

@SpringBootApplication
@EnableConfigurationProperties(value = [ApplicationProperties::class])
class RestApplication

fun main(args: Array<String>) {
    SpringApplication.run(RestApplication::class.java, *args)
}

@RestController
class SimpleRestController(
    private val applicationProperties: ApplicationProperties
) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun greeting(): String {
        return applicationProperties.greetingMessage
    }
}

@ConstructorBinding
@ConfigurationProperties(prefix = "application")
class ApplicationProperties(
    val greetingMessage: String
)