package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import org.springframework.data.mongodb.core.MongoTemplate
import javax.management.MBeanServerConnection
import javax.script.ScriptEngineManager
import javax.script.ScriptException

class JS : BotCommand{

    override val commandName: String = "js"

    override val description: String = "Executes the given JavaScript code"

    override val cooldown: Int = 0

    override fun call(
        message: String,
        channel: String,
        user: String,
        permissions: Set<CommandPermission>,
        commands: Set<BotCommand>,
        mongoTemplate: MongoTemplate,
        mbeanServerConnection: MBeanServerConnection
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