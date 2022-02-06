package com.menti.mentibot

import com.menti.mentibot.properties.BotProperties
import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.context.properties.EnableConfigurationProperties
import org.springframework.boot.runApplication
import org.springframework.data.mongodb.repository.config.EnableMongoRepositories

@SpringBootApplication
@EnableMongoRepositories
@EnableConfigurationProperties(BotProperties::class)
class MentibotApplication

fun main(args: Array<String>) {
    runApplication<MentibotApplication>(*args)
}
