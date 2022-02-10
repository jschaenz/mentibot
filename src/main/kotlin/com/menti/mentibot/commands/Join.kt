package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import com.menti.mentibot.config.BotConfig
import com.menti.mentibot.enums.CustomPermissionEnum
import com.menti.mentibot.model.UserModel
import org.springframework.data.mongodb.core.MongoTemplate
import javax.management.MBeanServerConnection

class Join(mongoTemplate: MongoTemplate, mbeanServerConnection: MBeanServerConnection, config: BotConfig) :
    BotCommand(
        mongoTemplate, mbeanServerConnection, config
    ) {
    override val commandName: String = "join"
    override val description: String = "Joins the given channel"
    override val cooldown: Int = 10

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
        var channelToJoin = message.split(" ")[0].lowercase()
        try {
            config.twitchClient.chat.joinChannel(channelToJoin)
            return "joined channel $channelToJoin"
        } catch (e: Exception) {
            return "${e.message}"
        }
    }
}