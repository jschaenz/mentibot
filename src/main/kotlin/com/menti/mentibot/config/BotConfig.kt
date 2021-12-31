package com.menti.mentibot.config

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClient
import com.github.twitch4j.TwitchClientBuilder
import com.menti.mentibot.properties.BotProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

@Configuration
class BotConfig(
    @Autowired private val properties: BotProperties
) {
    final var twitchClient: TwitchClient = TwitchClientBuilder.builder()
        .withChatAccount(
            OAuth2Credential(
                "twitch",
                properties.irc
            )
        )
        .withEnableChat(true)
        .withBotOwnerId("58055575")
        .withCommandTrigger(properties.prefix)
        .build()

    init {
        for (channel in properties.channels) {
            twitchClient.chat.joinChannel(channel)
        }
    }

}