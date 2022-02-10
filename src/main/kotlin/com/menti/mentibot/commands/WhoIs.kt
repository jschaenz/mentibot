package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import com.menti.mentibot.config.BotConfig
import com.menti.mentibot.model.UserModel
import com.mongodb.client.model.Filters
import org.springframework.data.mongodb.core.MongoTemplate
import javax.management.MBeanServerConnection

class WhoIs(mongoTemplate: MongoTemplate, mbeanServerConnection: MBeanServerConnection, config: BotConfig) :
    BotCommand(
        mongoTemplate, mbeanServerConnection, config
    ) {
    override val commandName: String = "whois"

    override val description: String = "Gives an overview of what the bot knows about you"

    override val cooldown: Int = 10
    override fun call(
        message: String,
        channel: String,
        user: String,
        roles: Set<CommandPermission>,
        permissions: UserModel?,
        commands: Set<BotCommand>
    ): String {
        var userToQuery = message.split(" ")[0].lowercase()

        if (userToQuery.isEmpty()) {
            userToQuery = user.lowercase()
        }

        val queriedUser = mongoTemplate.getCollection("users")
            .find(Filters.eq("name", userToQuery))
            .first()
            ?: return "could not find any information about $userToQuery :("

        return "user: ${queriedUser.getString("name")} - permission: ${queriedUser.getString("permission")} - id: ${queriedUser.getObjectId("_id")}"
    }
}