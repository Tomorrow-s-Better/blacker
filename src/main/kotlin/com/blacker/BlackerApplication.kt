package com.blacker

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class BlackerApplication

fun main(args: Array<String>) {
    runApplication<BlackerApplication>(*args)
}
