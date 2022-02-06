package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import org.springframework.data.mongodb.core.MongoTemplate

class Help : BotCommand {

    override val commandName: String = "help"

    override val description: String = "Gives the description of the specified command"

    override val cooldown: Int = 0

    override fun call(
        message: String,
        channel: String,
        user: String,
        permissions: Set<CommandPermission>,
        commands: Set<BotCommand>,
        mongoTemplate: MongoTemplate
    ): String {

        if(message.isEmpty()){
            return "Please specify a command!"
        }

        commands.forEach { c ->
            if (c.commandName == message.split(" ")[1]) {
                return c.description;
            }
        }
        return "command not found!"
    }

}