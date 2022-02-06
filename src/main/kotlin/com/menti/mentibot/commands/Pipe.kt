package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import com.menti.mentibot.config.BotConfig
import com.menti.mentibot.model.UserModel
import org.springframework.data.mongodb.core.MongoTemplate
import javax.management.MBeanServerConnection

class Pipe : BotCommand {
    override val commandName: String = "pipe"

    override val description: String = "Allows chaining of multiple commands. ]pipe command1 > command2 > command3"

    override val cooldown: Int = 15

    override fun call(
        message: String,
        channel: String,
        user: String,
        roles: Set<CommandPermission>,
        permissions: UserModel?,
        commands: Set<BotCommand>,
        mongoTemplate: MongoTemplate,
        mbeanServerConnection: MBeanServerConnection,
        config: BotConfig
    ): String {
        if (message.isEmpty()) {
            return "at least 1 command must be piped"
        }

        val commandsToInvoke = message.split(">").map { it.trim() }

        var lastResult: String = commandsToInvoke[0]

        for (commandToInvoke in commandsToInvoke) {
            for (command in commands) {
                if (commandToInvoke.startsWith(command.commandName)) {
                    lastResult = command.call(
                        lastResult,
                        channel,
                        user,
                        roles,
                        permissions,
                        commands,
                        mongoTemplate,
                        mbeanServerConnection,
                        config
                    )
                }
            }
        }
        return lastResult
    }
}