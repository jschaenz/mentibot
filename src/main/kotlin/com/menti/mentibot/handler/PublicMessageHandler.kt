package com.menti.mentibot.handler

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import com.menti.mentibot.config.BotConfig
import com.menti.mentibot.properties.BotProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlinx.coroutines.*

@Component
class PublicMessageHandler(
    @Autowired
    private val config: BotConfig,

    @Autowired
    private val properties: BotProperties,

    @Autowired
    private val commandHandler: CommandHandler,
) {

    private var lastMessage: String = ""

    private var timedOut = false

    init {
        setup()
    }

    @OptIn(DelicateCoroutinesApi::class)
    private final fun setup() = runBlocking {
        config.twitchClient.eventManager.onEvent(ChannelMessageEvent::class.java) { event ->
            if (event.message.startsWith(properties.prefix) && !timedOut) {
                GlobalScope.launch {
                    send(
                        commandHandler.invokeCommand(
                            event.message.replace(properties.prefix, ""),
                            event.channel.name,
                            event.permissions
                        ),
                        event.channel.name
                    )
                }
                timedOut = true
                GlobalScope.launch {
                    delay(1000L)
                    timedOut = false
                }
            }
        }
    }

    private fun send(content: String?, channel: String) {
        var message = content
        if (content == lastMessage) {
            message += "\uE0000"
        }
        config.twitchClient.chat.sendMessage(channel, content)
        lastMessage = "$content"
    }
}