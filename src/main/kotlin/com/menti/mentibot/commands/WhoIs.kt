package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import com.menti.mentibot.config.BotConfig
import com.menti.mentibot.model.UserModel
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import javax.management.MBeanServerConnection

class WhoIs : BotCommand {
    override val commandName: String = "whois"

    override val description: String = "Gives an overview of what the bot knows about you"

    override val cooldown: Int = 10

    override fun call(
        message: String,
        channel: String,
        user: String,
        roles: Set<CommandPermission>,
        permissions: UserModel?,
        commands: Set<BotCommand>,
        mongoTemplate: MongoTemplate,
        mbeanServerConnection: MBeanServerConnection,
        config: BotConfig
    ): String {
        var userToQuery = message.split(" ")[0].lowercase()

        if(userToQuery.isEmpty()){
            userToQuery = user.lowercase()
        }

        val queriedUser =
            mongoTemplate.findOne(Query().addCriteria(Criteria.where("name").`is`(userToQuery)), UserModel::class.java)
                ?: return "could not find any information about $userToQuery :("

        return "${queriedUser.name} - permission: ${queriedUser.permission} - join on restart: ${queriedUser.joinedChannel}"
    }
}