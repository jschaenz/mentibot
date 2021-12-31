package com.menti.mentibot.commands

import com.github.twitch4j.chat.events.channel.ChannelMessageEvent
import org.springframework.stereotype.Component
import javax.script.ScriptEngineManager
import javax.script.ScriptException


@Component
class JS {

    final val commandName: String = "js"

    fun call(event: ChannelMessageEvent, message: String) {

        val manager = ScriptEngineManager()
        val engine = manager.getEngineByName("JavaScript")

        try {
            val res = engine.eval(message)
            event.twitchChat.sendMessage(event.channel.name, res.toString())
        } catch (e: ScriptException) {
            event.twitchChat.sendMessage(event.channel.name, e.message)
        }

    }
}