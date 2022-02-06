package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import com.menti.mentibot.enums.SuggestionStatus
import com.menti.mentibot.model.SuggestionModel
import org.springframework.data.mongodb.core.MongoTemplate
import javax.management.MBeanServerConnection

class Suggest : BotCommand {
    override val commandName: String = "suggest"

    override val description: String = "Adds the given suggestion to the waiting list!"

    override val cooldown: Int = 10

    override fun call(
        message: String,
        channel: String,
        user: String,
        permissions: Set<CommandPermission>,
        commands: Set<BotCommand>,
        mongoTemplate: MongoTemplate,
        mbeanServerConnection: MBeanServerConnection
    ): String {
        val id: Long = mongoTemplate.getCollection("suggestions").countDocuments()

        mongoTemplate.insert(SuggestionModel(id, message, user, SuggestionStatus.NEW))

        return "suggestion saved with ID: $id"
    }
}