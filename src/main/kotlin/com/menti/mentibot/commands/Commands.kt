package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand

class Commands : BotCommand {

    override val commandName: String = "commands"

    override val description: String = "Lists all commands"

    override val cooldown: Int = 5

    override fun call(
        message: String,
        channel: String,
        permissions: Set<CommandPermission>,
        commands: Set<BotCommand>
    ): String {
        var commandNames = ""
        commands.forEach { c ->
            commandNames += "${c.commandName}, "
        }
        return "current commands are: $commandNames".removeSuffix(", ")
    }

}