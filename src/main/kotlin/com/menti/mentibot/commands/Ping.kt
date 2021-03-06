package com.menti.mentibot.commands

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.config.BotCommand
import com.menti.mentibot.config.BotConfig
import com.menti.mentibot.model.UserModel
import org.bson.Document
import org.springframework.data.mongodb.core.MongoTemplate
import java.lang.management.ManagementFactory
import java.lang.management.RuntimeMXBean
import javax.management.MBeanServerConnection
import javax.management.ObjectName

class Ping(mongoTemplate: MongoTemplate, mbeanServerConnection: MBeanServerConnection, config: BotConfig) :
    BotCommand(
        mongoTemplate, mbeanServerConnection, config
    ) {

    override val commandName: String = "ping"

    override val description: String = "Pings the Bot"

    override val cooldown: Int = 5

    override fun call(
        message: String,
        channel: String,
        user: String,
        roles: Set<CommandPermission>,
        permissions: UserModel?,
        commands: Set<BotCommand>
    ): String {
        //bot
        val rb: RuntimeMXBean = ManagementFactory.getRuntimeMXBean()

        //db
        val command: Document = Document().append("dbStats", 1)
        val stats = mongoTemplate.db.runCommand(command)
        val totalDbObjects = stats.getLong("objects")
        val collectionCount = stats.getInteger("collections")

        //jvm
        val loadedClasses =
            mbeanServerConnection.getAttribute(ObjectName("java.lang:type=ClassLoading"), "TotalLoadedClassCount")

        return "uptime: ${rb.uptime / 1000}s running JVM: ${rb.vmVersion} on ${rb.systemProperties["os.version"]}. Items in db: $totalDbObjects across $collectionCount collections with $loadedClasses loaded classes"
    }
}