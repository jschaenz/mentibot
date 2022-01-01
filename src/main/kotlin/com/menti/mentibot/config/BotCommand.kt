package com.menti.mentibot.config

import com.github.twitch4j.common.enums.CommandPermission

interface BotCommand {
    val commandName: String
    fun call(message: String, channel: String, permissions: Set<CommandPermission>): String?
}