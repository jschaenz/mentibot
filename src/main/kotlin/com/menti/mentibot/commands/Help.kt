package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import com.menti.mentibot.config.BotConfig
import com.menti.mentibot.model.UserModel
import org.springframework.data.mongodb.core.MongoTemplate
import javax.management.MBeanServerConnection

class Help(mongoTemplate: MongoTemplate, mbeanServerConnection: MBeanServerConnection, config: BotConfig) :
    BotCommand(
        mongoTemplate, mbeanServerConnection, config
    ) {

    override val commandName: String = "help"

    override val description: String = "Gives the description of the specified command"

    override val cooldown: Int = 0
    override fun call(
        message: String,
        channel: String,
        user: String,
        roles: Set<CommandPermission>,
        permissions: UserModel?,
        commands: Set<BotCommand>
    ): String {
        if (message.isEmpty()) {
            return "Please specify a command!"
        }

        commands.forEach { c ->
            if (c.commandName == message.split(" ")[0]) {
                return c.description;
            }
        }
        return "command not found!"
    }

}