package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import org.springframework.stereotype.Component
import java.lang.management.ManagementFactory
import java.lang.management.RuntimeMXBean

@Component
class Ping : BotCommand {

    final override val commandName: String = "ping"

    override fun call(message: String, channel: String, permissions: Set<CommandPermission>): String {
        val rb: RuntimeMXBean = ManagementFactory.getRuntimeMXBean()
        return "uptime ${rb.uptime / 1000}s running on JVM ${rb.vmVersion}"
    }
}