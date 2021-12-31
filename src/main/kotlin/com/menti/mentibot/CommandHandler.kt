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
    private val help: Help,

    @Autowired
    private val ping: Ping,

    @Autowired
    private val test: Test

) {
    init {
        var commandHandler = botConfig.twitchClient.eventManager.getEventHandler(SimpleEventHandler::class.java)
            .onEvent(ChannelMessageEvent::class.java) { event ->
                if (event.message.startsWith(properties.prefix)) {
                    val message: Array<String> = event.message.split(" ").toTypedArray()
                    when (message[0]) {
                        properties.prefix + "bot" -> bot.call(event)
                        properties.prefix + "commands" -> commands.call(event)
                        properties.prefix + "help" -> help.call(event)
                        properties.prefix + "ping" -> ping.call(event)
                        properties.prefix + "test" -> test.call(event)
                    }
                }
            }
    }
}