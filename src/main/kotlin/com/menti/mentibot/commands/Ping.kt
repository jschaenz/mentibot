package com.menti.mentibot.commands

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import org.springframework.stereotype.Component
import java.lang.management.ManagementFactory
import java.lang.management.RuntimeMXBean

@Component
class Ping {

    final val commandName: String = "ping"

    fun call(event: ChannelMessageEvent) {
        val rb: RuntimeMXBean = ManagementFactory.getRuntimeMXBean()
        event.twitchChat.sendMessage(
            event.channel.name,
            "uptime: ${rb.uptime / 1000}s running on: JVM ${rb.vmVersion}"
        )
    }
}