package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import org.bson.Document
import org.springframework.data.mongodb.core.MongoTemplate
import java.lang.management.ManagementFactory
import java.lang.management.RuntimeMXBean

class Ping : BotCommand {

    override val commandName: String = "ping"

    override val description: String = "Pings the Bot"

    override val cooldown: Int = 5

    override fun call(
        message: String,
        channel: String,
        permissions: Set<CommandPermission>,
        commands: Set<BotCommand>,
        mongoTemplate: MongoTemplate
    ): String {
        val rb: RuntimeMXBean = ManagementFactory.getRuntimeMXBean()
        val command: Document = Document().append("dbStats",1)
        val stats = mongoTemplate.db.runCommand(command)
        val totalDbObjects = stats.getLong("objects")
        val collectionCount = stats.getInteger("collections")

        return "uptime: ${rb.uptime / 1000}s running on JVM: ${rb.vmVersion} items in db: $totalDbObjects across $collectionCount collections"
    }
}