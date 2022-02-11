package com.menti.mentibot.config

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClient
import com.github.twitch4j.TwitchClientBuilder
import com.menti.mentibot.properties.BotProperties
import com.mongodb.client.model.Filters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.context.annotation.Configuration
import org.springframework.data.mongodb.core.MongoTemplate

/**
 * Creation of twitch4j bot client with correct properties
 */
@Configuration
class BotConfig(
    @Autowired
    private val properties: BotProperties,

    @Autowired
    private val mongoTemplate: MongoTemplate
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
        mongoTemplate.getCollection("channels")
            .find()
            .forEach { twitchClient.chat.joinChannel(it.getString("_id")) }

        for (channel in properties.channels) {
            twitchClient.chat.joinChannel(channel)
        }
    }

}