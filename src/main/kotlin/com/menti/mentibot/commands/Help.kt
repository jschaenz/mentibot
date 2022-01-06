package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand

class Help : BotCommand {

    override val commandName: String = "help"

    override val description: String = "Gives the description of the specified command"

    override val cooldown: Int = 0

    override fun call(
        message: String,
        channel: String,
        permissions: Set<CommandPermission>,
        commands: Set<BotCommand>
    ): String {

        if(message.isEmpty()){
            return "Please specify a command!"
        }

        commands.forEach { c ->
            if (c.commandName == message.split(" ")[1]) {
                return c.description;
            }
        }
        return "Command not found!"
    }

}