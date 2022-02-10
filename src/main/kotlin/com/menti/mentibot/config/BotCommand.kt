package com.menti.mentibot.config

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.model.UserModel
import org.springframework.data.mongodb.core.MongoTemplate
import javax.management.MBeanServerConnection

/**
 * Template for Bot Commands
 */
abstract class BotCommand(
    val mongoTemplate: MongoTemplate,
    val mbeanServerConnection: MBeanServerConnection,
    val config: BotConfig
) {
    abstract val commandName: String
    abstract val description: String
    abstract val cooldown: Int
    abstract fun call(
        message: String,
        channel: String,
        user: String,
        roles: Set<CommandPermission>,
        permissions: UserModel?,
        commands: Set<BotCommand>,
        ): String
}