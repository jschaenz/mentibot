package com.menti.mentibot.commands

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import com.menti.mentibot.config.BotConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class Bot(
    @Autowired
    private val botConfig: BotConfig
) {
    init {
        val botHandler = botConfig.twitchClient.eventManager.onEvent(ChannelMessageEvent::class.java) { event ->
            println("[${event.channel.name}][${event.permissions}] ${event.user.name}: ${event.message}")
        }
    }

}