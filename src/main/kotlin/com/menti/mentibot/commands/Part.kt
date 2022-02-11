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

class Part(mongoTemplate: MongoTemplate, mbeanServerConnection: MBeanServerConnection, config: BotConfig) :
    BotCommand(
        mongoTemplate, mbeanServerConnection, config
    ) {
    override val commandName: String = "part"
    override val description: String = "Leaves the given channel"
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

        var channelToLeave = message.split(" ")[0].lowercase()

        if (message.contains("-save")) {
            val result = mongoTemplate.getCollection("channels")
                .find(Filters.eq("_id", channelToLeave))
                .first()
            return if (result != null) {
                try {
                    mongoTemplate.remove(result, "channels")

                    config.twitchClient.chat.leaveChannel(channelToLeave)
                    "persistently left channel $channelToLeave"
                } catch (e: Exception) {
                    "${e.message}"
                }
            } else {
                "cannot leave $channelToLeave, never joined!"
            }
        }

        return try {
            config.twitchClient.chat.leaveChannel(channelToLeave)
            "left channel $channelToLeave"
        } catch (e: Exception) {
            "${e.message}"
        }
    }
}