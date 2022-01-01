package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import javax.script.ScriptEngineManager
import javax.script.ScriptException

class JS : BotCommand{

    override val description: String = ""

    override val commandName: String = "js"

    override fun call(
        message: String,
        channel: String,
        permissions: Set<CommandPermission>,
        commands: Set<BotCommand>
    ): String {

        if (!permissions.contains(CommandPermission.OWNER)) {
            return ""
        }

        return try {
            val manager = ScriptEngineManager()
            val engine = manager.getEngineByName("JavaScript")
            engine.eval(message).toString()
        } catch (e: ScriptException) {
            "${e.message}"
        }
    }
}