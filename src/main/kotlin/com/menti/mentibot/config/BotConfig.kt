package com.menti.mentibot.config

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClient
import com.github.twitch4j.TwitchClientBuilder
import com.menti.mentibot.properties.BotProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration

/**
 * Creation of twitch4j bot client with correct properties
 */
@Configuration
class BotConfig(
    @Autowired
    private val properties: BotProperties
) {
    final var twitchClient: TwitchClient = TwitchClientBuilder.builder()
        .withChatAccount(
            OAuth2Credential(
                "twitch",
                properties.irc
            )
        )
        .withEnableChat(true)
        .withBotOwnerId(properties.ownerId)
        .withCommandTrigger(properties.prefix)
        .build()

    init {
        for (channel in properties.channels) {
            twitchClient.chat.joinChannel(channel)
        }
    }

}