package com.menti.mentibot.handler

import com.github.twitch4j.common.enums.CommandPermission
import com.google.common.reflect.ClassPath
import com.menti.mentibot.config.BotCommand
import com.menti.mentibot.config.BotConfig
import com.menti.mentibot.enums.CustomPermissionEnum
import com.menti.mentibot.model.UserModel
import com.sun.tools.attach.VirtualMachine
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query
import org.springframework.stereotype.Component
import java.util.stream.Collectors
import javax.management.MBeanServerConnection
import javax.management.remote.JMXConnectorFactory
import javax.management.remote.JMXServiceURL

/**
 * Handles all commands and how they are invoked
 */
@Component
class CommandHandler(
    @Autowired
    val mongoTemplate: MongoTemplate
) {

    private val commandsInstances: MutableSet<BotCommand> = mutableSetOf()

    private val timeOuts: MutableSet<Pair<String, String>> = mutableSetOf()

    private val connectedVm: VirtualMachine

    private val mbeanServerConnection: MBeanServerConnection

    /**
     * Instantiates all classes in the commands package
     */
    init {
        val commands = ClassPath.from(ClassLoader.getSystemClassLoader())
            .allClasses
            .stream()
            .filter { t -> t.packageName == "com.menti.mentibot.commands" }
            .map { t -> t.load() }
            .collect(Collectors.toSet()) as Set<Class<BotCommand>>

        for (command in commands) {
            commandsInstances.add(command.getDeclaredConstructor().newInstance())
        }

        //jvm
        val vmDescriptor = VirtualMachine.list().filter { it.displayName().contains("mentibot") }[0]
        connectedVm = VirtualMachine.attach(vmDescriptor)

        var connectorAddress =
            connectedVm.agentProperties.getProperty("com.sun.management.jmxremote.localConnectorAddress")
        if (connectorAddress == null) {
            connectedVm.startLocalManagementAgent()
            connectorAddress =
                connectedVm.agentProperties.getProperty("com.sun.management.jmxremote.localConnectorAddress")
        }

        mbeanServerConnection = JMXConnectorFactory.connect(JMXServiceURL(connectorAddress)).mBeanServerConnection
    }

    /**
     * Invokes the correct command
     */
    fun invokeCommand(message: String, user: String, channel: String, roles: Set<CommandPermission>, config: BotConfig): String {
        for (command in commandsInstances) {
            if (message.startsWith(command.commandName) && !timeOuts.contains(Pair(user, command.commandName))) {

                val permissions =
                    mongoTemplate.findOne(Query().addCriteria(Criteria.where("name").`is`(user)), UserModel::class.java)

                if (permissions?.permission == CustomPermissionEnum.DEFAULT || permissions == null) {
                    setTimeout(user, command)
                }

                return command.call(
                    message.removePrefix(command.commandName).trim(),
                    channel,
                    user,
                    roles,
                    permissions,
                    commandsInstances,
                    mongoTemplate,
                    mbeanServerConnection,
                    config
                )
            }
        }
        return ""
    }

    @OptIn(DelicateCoroutinesApi::class)
    fun setTimeout(user: String, command: BotCommand) = runBlocking {
        timeOuts.add(Pair(user, command.commandName))
        GlobalScope.launch {
            delay(command.cooldown * 1000L)
            timeOuts.remove(Pair(user, command.commandName))
        }
    }
}
