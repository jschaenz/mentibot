package com.menti.mentibot.properties

import org.springframework.boot.context.properties.ConfigurationProperties
import org.springframework.boot.context.properties.ConstructorBinding

@ConstructorBinding
@ConfigurationProperties(prefix = "mentibot")
data class BotProperties(
    val owner: String,
    val ownerId: String,
    val irc: String,
    val prefix: String,
    val channels: Array<String>
)