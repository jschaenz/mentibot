package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import com.menti.mentibot.config.BotConfig
import com.menti.mentibot.enums.CustomPermissionEnum
import com.menti.mentibot.model.ChannelsToJoinModel
import com.menti.mentibot.model.UserModel
import com.mongodb.client.model.Filters
import org.springframework.data.mongodb.core.MongoTemplate
import javax.management.MBeanServerConnection

class Join(mongoTemplate: MongoTemplate, mbeanServerConnection: MBeanServerConnection, config: BotConfig) :
    BotCommand(
        mongoTemplate, mbeanServerConnection, config
    ) {
    override val commandName: String = "join"
    override val description: String = "Joins the given channel, -save also joins the channel on restart"
    override val cooldown: Int = 10

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
        val channelToJoin = message.split(" ")[0].lowercase()

        if (message.contains("-save")) {
            val result = mongoTemplate.getCollection("channels")
                .find(Filters.eq("_id", channelToJoin))
                .first()
            return if (result == null) {
                try {
                    mongoTemplate.insert(ChannelsToJoinModel(channelToJoin), "channels")

                    config.twitchClient.chat.joinChannel(channelToJoin)
                    "persistently joined channel $channelToJoin"
                } catch (e: Exception) {
                    "${e.message}"
                }
            } else {
                "already joined $channelToJoin"
            }
        }

        return try {
            config.twitchClient.chat.joinChannel(channelToJoin)
            "joined channel $channelToJoin"
        } catch (e: Exception) {
            "${e.message}"
        }
    }
}