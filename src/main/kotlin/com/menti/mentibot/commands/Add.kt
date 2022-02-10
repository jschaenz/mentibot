package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import com.menti.mentibot.config.BotConfig
import com.menti.mentibot.enums.CustomPermissionEnum
import com.menti.mentibot.model.UserModel
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import javax.management.MBeanServerConnection

class Add(mongoTemplate: MongoTemplate, mbeanServerConnection: MBeanServerConnection, config: BotConfig) :
    BotCommand(
        mongoTemplate, mbeanServerConnection, config
    ) {
    override val commandName: String = "add"

    override val description: String =
        "Adds the given user with the given parameters to the db. ]add <user> <permission> <join?true:false>"

    override val cooldown: Int = 5
    override fun call(
        message: String,
        channel: String,
        user: String,
        roles: Set<CommandPermission>,
        permissions: UserModel?,
        commands: Set<BotCommand>
    ): String {
        if (permissions?.permission == CustomPermissionEnum.DEFAULT || permissions == null) {
            return ""
        }

        val userToInsert = message.split(" ")[0].lowercase()
        val permissionsToInsert = message.split(" ")[1].trim()
        var toJoin = message.split(" ")[2].trim()

        val userInDb =
            mongoTemplate.findOne(Query().addCriteria(Criteria.where("name").`is`(userToInsert)), UserModel::class.java)
        if (userInDb != null) {
            return "User Already exists in DB!"
        }

        if (toJoin.isEmpty() || toJoin == "false") {
        } else {
            commands.find { it.commandName == "join" }?.call(
                message,
                channel,
                userToInsert,
                roles,
                permissions,
                commands
            )
        }

        mongoTemplate.insert(
            UserModel(
                userToInsert,
                CustomPermissionEnum.valueOf(permissionsToInsert.uppercase()),
                toJoin.toBooleanStrict()
            ), "users"
        )

        return "user $userToInsert saved to db"
    }

}