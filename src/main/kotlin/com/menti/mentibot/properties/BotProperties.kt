package com.menti.mentibot.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "mentibot")
data class BotProperties(
    val irc: String,
    val channels: Array<String>,
    val prefix: String,
    val owner: String
)