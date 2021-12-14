package ru.romanow.simple.backend

import org.springframework.boot.SpringApplication
import org.springframework.boot.autoconfigure.SpringBootApplication

@SpringBootApplication
class SimpleApplication

fun main(args: Array<String>) {
    SpringApplication.run(SimpleApplication::class.java, *args)
}