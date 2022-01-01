package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import java.lang.management.ManagementFactory
import java.lang.management.RuntimeMXBean

class Ping : BotCommand {

    override val commandName: String = "ping"

    override val description: String = ""

    override fun call(
        message: String,
        channel: String,
        permissions: Set<CommandPermission>,
        commands: Set<BotCommand>
    ): String {
        val rb: RuntimeMXBean = ManagementFactory.getRuntimeMXBean()
        return "uptime ${rb.uptime / 1000}s running on JVM ${rb.vmVersion}"
    }
}