package com.menti.mentibot.handler

import com.github.twitch4j.common.events.user.PrivateMessageEvent
import com.menti.mentibot.config.BotConfig
import com.menti.mentibot.properties.BotProperties
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component

@Component
class PrivateMessageHandler(
    @Autowired
    private val config: BotConfig,

    @Autowired
    private val properties: BotProperties,

    @Autowired
    private val commandHandler: CommandHandler
) {

    init {
        config.twitchClient.eventManager.onEvent(PrivateMessageEvent::class.java) { event ->
            if (event.message.startsWith(properties.prefix)) {
                send(
                    commandHandler.invokeCommand(event.message.replace("]", ""), event.user.name, event.permissions),
                    event.user.name
                )
            }
        }
    }

    fun send(content: String, recipient: String) {
        config.twitchClient.chat.sendPrivateMessage(recipient, content)
    }

}