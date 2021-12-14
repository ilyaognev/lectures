package ru.romanow.simple.backend.web

import org.springframework.data.domain.Example
import org.springframework.data.domain.ExampleMatcher
import org.springframework.data.domain.ExampleMatcher.matching
import org.springframework.http.MediaType
import org.springframework.web.bind.annotation.GetMapping
import org.springframework.web.bind.annotation.RequestParam
import org.springframework.web.bind.annotation.RestController
import ru.romanow.simple.backend.domain.GreetingData
import ru.romanow.simple.backend.repository.GreetingDataRepository

@RestController
class SimpleRestController(
    private val greetingDataRepository: GreetingDataRepository
) {

    @GetMapping(produces = [MediaType.APPLICATION_JSON_VALUE])
    fun greeting(@RequestParam person: String): String? {
        return greetingDataRepository
            .findOne(Example.of(GreetingData(person = person), matching().withIgnoreCase()))
            .map { it.message }
            .orElse(null)
    }
}