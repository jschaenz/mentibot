package com.menti.mentibot.commands

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import org.springframework.stereotype.Component

@Component
class Bot {

    final val commandName: String = "bot"

    fun call(event: ChannelMessageEvent) {
        event.twitchChat.sendMessage(event.channel.name, "mentibot is a bot made by mentiofficial, written in Kotlin")
    }
}