package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import com.menti.mentibot.model.SuggestionModel
import org.springframework.data.mongodb.core.MongoTemplate

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
        mongoTemplate: MongoTemplate
    ): String {
        val id: Long = mongoTemplate.getCollection("suggestions").countDocuments()

        mongoTemplate.insert(SuggestionModel(id, message))

        return "suggestion saved with ID: $id"
    }
}