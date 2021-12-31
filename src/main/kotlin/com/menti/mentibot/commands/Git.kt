package com.menti.mentibot.commands

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import org.springframework.stereotype.Component
import java.net.URI
import java.net.http.HttpClient
import java.net.http.HttpRequest
import java.net.http.HttpResponse

@Component
class Git {
    final val commandName: String = "git"

    fun call(event: ChannelMessageEvent) {
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

        event.twitchChat.sendMessage(
            event.channel.name,
            "https://github.com/jschaenz/mentibot last update$response"
        )
    }
}