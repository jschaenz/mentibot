package com.menti.mentibot.config

import com.github.philippheuer.credentialmanager.domain.OAuth2Credential
import com.github.twitch4j.TwitchClient
import com.github.twitch4j.TwitchClientBuilder
import org.springframework.context.annotation.Bean
import org.springframework.context.annotation.Configuration

@Configuration
class BotConfig {

    private final val irc:String = ""

    @Bean
    fun twitchClient(): TwitchClient {
        val credential = OAuth2Credential(
            "twitch",
            irc
        )

        val twitchClient = TwitchClientBuilder.builder()
            .withChatAccount(credential)
            .withEnableChat(true)
            .build()

        twitchClient.chat.joinChannel("mentiofficial")
        twitchClient.chat.joinChannel("hotbear1110")

        return twitchClient
    }

}