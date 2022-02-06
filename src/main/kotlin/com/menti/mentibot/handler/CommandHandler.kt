package com.menti.mentibot.handler

import com.github.twitch4j.common.enums.CommandPermission
import com.google.common.reflect.ClassPath
import com.menti.mentibot.config.BotCommand
import kotlinx.coroutines.*
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.stereotype.Component
import java.util.stream.Collectors

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
            commandsInstances.add(command.newInstance())
        }
    }

    /**
     * Invokes the correct command
     */
    fun invokeCommand(message: String, user: String, channel: String, permissions: Set<CommandPermission>): String {
        for (command in commandsInstances) {
            if (message.startsWith(command.commandName) && !timeOuts.contains(Pair(user, command.commandName))) {
                setTimeout(user, command)
                return command.call(
                    message.removePrefix(command.commandName).trim(),
                    channel,
                    user,
                    permissions,
                    commandsInstances,
                    mongoTemplate
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
