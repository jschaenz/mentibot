package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import org.springframework.data.mongodb.core.MongoTemplate
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import javax.management.MBeanServerConnection

class Git : BotCommand {

    override val commandName: String = "git"

    override val description: String = "Link to the github repo with timestamp of last change"

    override val cooldown: Int = 5

    override fun call(
        message: String,
        channel: String,
        user: String,
        permissions: Set<CommandPermission>,
        commands: Set<BotCommand>,
        mongoTemplate: MongoTemplate,
        mbeanServerConnection: MBeanServerConnection
    ): String {
        val client = HttpClient.newBuilder()
            .build()

        val request = HttpRequest.newBuilder()
            .uri(URI.create("https://api.github.com/repos/jschaenz/mentibot"))
            .build()

        val response = client.send(
            request, HttpResponse.BodyHandlers
                .ofString()
        )
            .body()
            .split(",")[66]
            .replace("\"", "")
            .replace("updated_at", "")

        return "https://github.com/jschaenz/mentibot last update$response"
    }
}