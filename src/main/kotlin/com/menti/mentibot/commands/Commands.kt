package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import com.menti.mentibot.config.BotConfig
import com.menti.mentibot.model.UserModel
import org.springframework.data.mongodb.core.MongoTemplate
import javax.management.MBeanServerConnection

class Commands : BotCommand {

    override val commandName: String = "commands"

    override val description: String = "Lists all commands"

    override val cooldown: Int = 5

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
        var commandNames = ""
        commands.forEach { c ->
            commandNames += "${c.commandName}, "
        }
        return "current commands are: $commandNames".removeSuffix(", ")
    }

}