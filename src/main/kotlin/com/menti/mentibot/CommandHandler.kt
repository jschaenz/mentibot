package com.menti.mentibot

import com.github.philippheuer.events4j.simple.SimpleEventHandler
import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import com.menti.mentibot.commands.Bot
import com.menti.mentibot.config.BotConfig
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class CommandHandler(
    @Autowired
    private val botConfig: BotConfig,

    @Autowired
    private val bot: Bot

) {
    init {
        var handler = botConfig.twitchClient.eventManager.getEventHandler(SimpleEventHandler::class.java)
            .onEvent(ChannelMessageEvent::class.java) { event ->
                if (event.message.startsWith("]")) {
                    val message: Array<String> = event.message.split(" ").toTypedArray()
                    when (message[0]) {
                        "]bot" -> bot.call(event)
                    }
                }
            }
    }
}