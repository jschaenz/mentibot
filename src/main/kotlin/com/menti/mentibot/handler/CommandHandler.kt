package com.menti.mentibot.handler

import com.github.twitch4j.common.enums.CommandPermission
import com.google.common.reflect.ClassPath
import com.menti.mentibot.config.BotCommand
import org.springframework.stereotype.Component
import java.util.stream.Collectors

@Component
class CommandHandler {

    private val commandsInstances: MutableSet<BotCommand> = mutableSetOf()

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

    fun invokeCommand(message: String, channel: String, permissions: Set<CommandPermission>): String {
        for (command in commandsInstances) {
            if (message.startsWith(command.commandName)) {
                return command.call(message, channel, permissions, commandsInstances)
            }
        }
        return ""
    }
}
