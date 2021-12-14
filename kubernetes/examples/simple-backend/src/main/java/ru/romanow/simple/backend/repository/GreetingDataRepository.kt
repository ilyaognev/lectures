package ru.romanow.simple.backend.repository

import org.springframework.data.jpa.repository.JpaRepository
import ru.romanow.simple.backend.domain.GreetingData

interface GreetingDataRepository : JpaRepository<GreetingData, Int>