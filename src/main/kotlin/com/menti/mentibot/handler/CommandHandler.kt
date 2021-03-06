package com.menti.mentibot.handler

import com.github.twitch4j.common.enums.CommandPermission
import com.google.common.reflect.ClassPath
import com.menti.mentibot.config.BotCommand
import com.menti.mentibot.config.BotConfig
import com.menti.mentibot.config.VMConnector
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
    private val mongoTemplate: MongoTemplate,
    @Autowired
    private val config: BotConfig,
    @Autowired
    private val vmConnector: VMConnector
) {

    private val commandsInstances: MutableSet<BotCommand> = mutableSetOf()

    private val timeOuts: MutableSet<Pair<String, String>> = mutableSetOf()

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
            var params = Array<Class<*>>(command.constructors[0].parameterCount) { mongoTemplate::class.java }
            command.constructors.forEach {
                it.parameters.forEachIndexed { index, parameter -> params[index] = parameter.type }
            }
            commandsInstances.add(
                command.getConstructor(*params)
                    .newInstance(mongoTemplate, vmConnector.mbeanServerConnection, config)
            )
        }
    }

    /**
     * Invokes the correct command
     */
    fun invokeCommand(
        message: String,
        user: String,
        channel: String,
        roles: Set<CommandPermission>
    ): String {
        for (command in commandsInstances) {
            if (message.startsWith(command.commandName) && !timeOuts.contains(Pair(user, command.commandName))) {

                val permissions =
                    mongoTemplate.findOne(
                        Query().addCriteria(
                            Criteria.where("name").`is`(user)
                        ),
                        UserModel::class.java
                    )

                if (permissions?.permission == CustomPermissionEnum.DEFAULT) {
                    setTimeout(user, command)
                }

                return command.call(
                    message.removePrefix(command.commandName).trim(),
                    channel,
                    user,
                    roles,
                    permissions,
                    commandsInstances
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
