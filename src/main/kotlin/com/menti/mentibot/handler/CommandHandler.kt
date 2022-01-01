package com.menti.mentibot.handler

import com.github.twitch4j.common.enums.CommandPermission
import com.menti.mentibot.commands.Bot
import com.menti.mentibot.commands.Git
import com.menti.mentibot.commands.JS
import com.menti.mentibot.commands.Ping
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CommandHandler(
    @Autowired
    private val bot: Bot,

    @Autowired
    private val git: Git,

    @Autowired
    private val js: JS,

    @Autowired
    private val ping: Ping,

    ) {

    fun invokeCommand(message: String, channel: String, permissions: Set<CommandPermission>): String {
        val message = message.replace("]", "")
        return when (message.split(" ").toTypedArray()[0].replace("]", "")) {
            bot.commandName -> bot.call(message, channel, permissions)
            git.commandName -> git.call(message, channel, permissions)
            js.commandName -> js.call(message.replace(js.commandName, ""), channel, permissions)
            ping.commandName -> ping.call(message, channel, permissions)
            else -> ""
        }
    }
}

/*
val commands: Set<Class<BotCommand>> = ClassPath.from(ClassLoader.getSystemClassLoader())
    .allClasses
    .stream()
    .filter { t -> t.packageName == "com.menti.mentibot.commands" }
    .map { t -> t.load() }
    .collect(Collectors.toSet()) as Set<Class<BotCommand>>

for (command in commands) {
    command.methods[0].invoke(null, "123", "123", setOf(CommandPermission.OWNER))
}
 */