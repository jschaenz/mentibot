package com.menti.mentibot.commands

import com.github.kittinunf.fuel.httpGet
import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import com.menti.mentibot.config.BotConfig
import com.menti.mentibot.model.UserModel
import org.springframework.data.mongodb.core.MongoTemplate
import javax.management.MBeanServerConnection

class Temp(mongoTemplate: MongoTemplate, mbeanServerConnection: MBeanServerConnection, config: BotConfig) :
    BotCommand(
        mongoTemplate, mbeanServerConnection, config
    ) {
    override val commandName: String = "temp"

    override val description: String = "returns current server room temperature"

    override val cooldown: Int = 5

    override fun call(
        message: String,
        channel: String,
        user: String,
        roles: Set<CommandPermission>,
        permissions: UserModel?,
        commands: Set<BotCommand>
    ): String {

        return "current room temperature: " + "http://192.168.0.6/temp"
            .httpGet()
            .responseString()
            .third
            .get()
            .split(",")[2]
            .replace("\"compensated\":", "")
            .replace(",\"offset\":0}", "") + "°C"
    }
}