package com.menti.mentibot

import org.springframework.boot.autoconfigure.SpringBootApplication
import org.springframework.boot.runApplication

@SpringBootApplication
class MentibotApplication

fun main(args: Array<String>) {
	runApplication<MentibotApplication>(*args)
}
