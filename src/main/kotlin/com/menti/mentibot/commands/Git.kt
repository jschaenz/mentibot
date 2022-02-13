package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import com.menti.mentibot.config.BotConfig
import com.menti.mentibot.model.UserModel
import org.springframework.data.mongodb.core.MongoTemplate
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse
import javax.management.MBeanServerConnection

class Git(mongoTemplate: MongoTemplate, mbeanServerConnection: MBeanServerConnection, config: BotConfig) :
    BotCommand(
        mongoTemplate, mbeanServerConnection, config
    ) {

    override val commandName: String = "git"

    override val description: String = "Link to the github repo with timestamp of last change"

    override val cooldown: Int = 5
    override fun call(
        message: String,
        channel: String,
        user: String,
        roles: Set<CommandPermission>,
        permissions: UserModel?,
        commands: Set<BotCommand>
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
            .split(",")[67]
            .replace("\"", "")
            .replace("pushed_at:", "")

        return "https://github.com/jschaenz/mentibot last update: $response"
    }
}