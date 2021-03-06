package com.menti.mentibot.handler

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import com.menti.mentibot.config.BotConfig
import com.menti.mentibot.enums.CustomPermissionEnum
import com.menti.mentibot.model.UserModel
import com.menti.mentibot.properties.BotProperties
import com.mongodb.client.model.Filters
import org.springframework.beans.factory.annotation.Autowired
import org.springframework.stereotype.Component
import kotlinx.coroutines.*
import org.springframework.data.mongodb.core.MongoTemplate
import org.springframework.data.mongodb.core.query.Criteria
import org.springframework.data.mongodb.core.query.Query

/**
 * Handles all public (regular chat) messages
 */
@Component
class PublicMessageHandler(
    @Autowired
    private val config: BotConfig,

    @Autowired
    private val properties: BotProperties,

    @Autowired
    private val commandHandler: CommandHandler,

    @Autowired
    private val mongoTemplate: MongoTemplate
) {

    private var lastMessage: String = ""

    private var timedOut = false

    init {
        setup()
    }

    /**
     * If prefix is detected, invokes the given command in a coroutine.
     * Then sets the timedOut flag to true to adhere to global 1s slowmode.
     * Another coroutine then waits 1s after the flag is set to true to set it back to false.
     */
    @OptIn(DelicateCoroutinesApi::class)
    private fun setup() = runBlocking {
        config.twitchClient.eventManager.onEvent(ChannelMessageEvent::class.java) { event ->
            if (event.message.startsWith(properties.prefix) && !timedOut) {

                addUserToDB(event.user.name)

                GlobalScope.launch {
                    send(
                        commandHandler.invokeCommand(
                            event.message.replace(properties.prefix, ""),
                            event.user.name,
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


    private fun addUserToDB(name: String) {

        val userInDb = mongoTemplate.getCollection("users")
            .find(Filters.eq("name", name))
            .first()

        if (userInDb == null) {
            mongoTemplate.insert(
                UserModel(
                    name,
                    CustomPermissionEnum.DEFAULT
                ),
                "users"
            )
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