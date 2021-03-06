package com.menti.mentibot.commands

import com.github.kittinunf.fuel.httpGet
import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import com.menti.mentibot.config.BotConfig
import com.menti.mentibot.model.UserModel
import org.springframework.data.mongodb.core.MongoTemplate
import javax.management.MBeanServerConnection

class Kanye(mongoTemplate: MongoTemplate, mbeanServerConnection: MBeanServerConnection, config: BotConfig) :
    BotCommand(
        mongoTemplate, mbeanServerConnection, config
    ) {
    override val commandName: String = "kanye"

    override val description: String = "Returns a random Kanye West quote"

    override val cooldown: Int = 5

    override fun call(
        message: String,
        channel: String,
        user: String,
        roles: Set<CommandPermission>,
        permissions: UserModel?,
        commands: Set<BotCommand>
    ): String {
        return "https://api.kanye.rest"
            .httpGet()
            .responseString()
            .third
            .get()
            .replace("{\"quote\":\"", "")
            .replace("\"}", "")
    }
}