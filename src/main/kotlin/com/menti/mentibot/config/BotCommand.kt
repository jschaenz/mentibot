package com.menti.mentibot.config

import com.github.twitch4j.common.enums.CommandPermission
import org.springframework.data.mongodb.core.MongoTemplate
import javax.management.MBeanServerConnection

/**
 * Template for Bot Commands
 */
interface BotCommand {
    val commandName: String
    val description: String
    val cooldown: Int
    fun call(
        message: String,
        channel: String,
        user: String,
        permissions: Set<CommandPermission>,
        commands: Set<BotCommand>,
        mongoTemplate: MongoTemplate,
        mbeanServerConnection: MBeanServerConnection
    ): String
}