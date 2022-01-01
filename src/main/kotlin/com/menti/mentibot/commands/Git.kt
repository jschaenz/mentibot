package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Component
class Git : BotCommand {
    final override val commandName: String = "git"

    override fun call(message: String, channel: String, permissions: Set<CommandPermission>): String {
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
            .split(",")[65]
            .replace("\"", "")
            .replace("created_at", "")

        return "https://github.com/jschaenz/mentibot last update$response"
    }
}