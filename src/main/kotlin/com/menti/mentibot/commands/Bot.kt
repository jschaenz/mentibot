package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import org.springframework.data.mongodb.core.MongoTemplate
import javax.management.MBeanServerConnection

class Bot : BotCommand {

    override val commandName: String = "bot"

    override val description: String = "Gives some basic Information about the bot"

    override val cooldown: Int = 5

    override fun call(
        message: String,
        channel: String,
        user: String,
        permissions: Set<CommandPermission>,
        commands: Set<BotCommand>,
        mongoTemplate: MongoTemplate,
        mbeanServerConnection: MBeanServerConnection
    ): String {
        return "mentibot is a bot made by mentiofficial, written in Kotlin"
    }

}