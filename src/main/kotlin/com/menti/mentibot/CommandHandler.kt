package com.menti.mentibot

import com.github.philippheuer.events4j.simple.SimpleEventHandler
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import com.menti.mentibot.commands.*
import com.menti.mentibot.config.BotConfig
import com.menti.mentibot.properties.BotProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CommandHandler(
    @Autowired
    private val botConfig: BotConfig,

    @Autowired
    private val properties: BotProperties,

    @Autowired
    private val bot: Bot,

    @Autowired
    private val commands: Commands,

    @Autowired
    private val git: Git,

    @Autowired
    private val help: Help,

    @Autowired
    private val js: JS,

    @Autowired
    private val ping: Ping,

) {
    init {
        var commandHandler = botConfig.twitchClient.eventManager.getEventHandler(SimpleEventHandler::class.java)
            .onEvent(ChannelMessageEvent::class.java) { event ->
                if (event.message.startsWith(properties.prefix)) {
                    val message: Array<String> = event.message.split(" ").toTypedArray()
                    when (message[0]) {
                        properties.prefix + bot.commandName -> bot.call(event)
                        properties.prefix + commands.commandName -> commands.call(event)
                        properties.prefix + git.commandName -> git.call(event)
                        properties.prefix + help.commandName -> help.call(event)
                        properties.prefix + js.commandName -> js.call(event, event.message.removePrefix(properties.prefix + js.commandName))
                        properties.prefix + ping.commandName -> ping.call(event)
                    }
                }
            }
    }
}