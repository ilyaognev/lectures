package ru.romanow.simple.backend.domain

import javax.persistence.*

@Entity
@Table(name = "greeting_data")
data class GreetingData(

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    var id: Int? = null,

    @Column(name = "person", nullable = false)
    val person: String? = null,

    @Column(name = "message", nullable = false)
    val message: String? = null
)