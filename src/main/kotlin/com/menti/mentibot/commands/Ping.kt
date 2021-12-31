package com.menti.mentibot.commands

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import org.springframework.stereotype.Component

@Component
class Ping {
    fun call(event: ChannelMessageEvent) {
        event.twitchChat.sendMessage(event.channel.name, "ping")
    }
}