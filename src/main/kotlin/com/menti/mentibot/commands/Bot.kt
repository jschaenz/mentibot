package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import org.springframework.stereotype.Component

@Component
class Bot : BotCommand {

    final override val commandName: String = "bot"

    override fun call(message: String, channel: String, permissions: Set<CommandPermission>): String {
        return "mentibot is a bot made by mentiofficial, written in Kotlin"
    }

}