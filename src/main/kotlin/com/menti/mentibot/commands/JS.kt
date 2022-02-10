package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import com.menti.mentibot.config.BotConfig
import com.menti.mentibot.enums.CustomPermissionEnum
import com.menti.mentibot.model.UserModel
import org.springframework.data.mongodb.core.MongoTemplate
import javax.management.MBeanServerConnection
import javax.script.ScriptEngineManager
import javax.script.ScriptException

class JS(mongoTemplate: MongoTemplate, mbeanServerConnection: MBeanServerConnection, config: BotConfig) :
    BotCommand(
        mongoTemplate, mbeanServerConnection, config
    ) {

    override val commandName: String = "js"

    override val description: String = "Executes the given JavaScript code"

    override val cooldown: Int = 0

    override fun call(
        message: String,
        channel: String,
        user: String,
        roles: Set<CommandPermission>,
        permissions: UserModel?,
        commands: Set<BotCommand>
    ): String {

        if (permissions?.permission == CustomPermissionEnum.DEFAULT) {
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