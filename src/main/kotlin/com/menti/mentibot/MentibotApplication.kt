package com.menti.mentibot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.ConfigurationPropertiesScan
import org.springframework.boot.runApplication

@SpringBootApplication
@ConfigurationPropertiesScan
class MentibotApplication

fun main(args: Array<String>) {
    runApplication<MentibotApplication>(*args)
}
