package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import org.springframework.stereotype.Component
import javax.script.ScriptEngineManager
import javax.script.ScriptException

@Component
class JS : BotCommand {

    final override val commandName: String = "js"

    override fun call(message: String, channel: String, permissions: Set<CommandPermission>): String {

        if (!permissions.contains(CommandPermission.OWNER)) {
            return ""
        }

        return try {
            val manager = ScriptEngineManager()
            val engine = manager.getEngineByName("JavaScript")
            return engine.eval(message).toString()
        } catch (e: ScriptException) {
            "${e.message}"
        }
    }
}