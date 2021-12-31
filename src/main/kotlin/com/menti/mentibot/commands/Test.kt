package com.menti.mentibot.commands

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import org.springframework.stereotype.Component

@Component
class Test {
    fun call(event: ChannelMessageEvent) {
        event.twitchChat.sendMessage(event.channel.name, "test")
    }
}