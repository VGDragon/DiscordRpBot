package com.vgdragon

import com.github.salomonbrys.kotson.fromJson
import com.github.salomonbrys.kotson.typedToJson
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.vgdragon.MassageFunktions.MassageHandler
import com.vgdragon.dataclass.BotData
import com.vgdragon.dataclass.GuildData
import com.vgdragon.dataclass.ServerUserData
import com.vgdragon.funftions.UserMapUpdate
import net.dv8tion.jda.api.events.DisconnectEvent
import net.dv8tion.jda.api.events.ReadyEvent
import net.dv8tion.jda.api.events.ReconnectedEvent
import net.dv8tion.jda.api.events.ResumedEvent
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.io.File


class EventListener : ListenerAdapter() {

    val backupFile = File("DiscordRpBotData.json")

    val botData = backupRead()
    val botDataLock = Object()

    val userMapUpdate = UserMapUpdate(botData)
    val defaultPrefix = "rp!"




    override fun onReconnect(event: ReconnectedEvent) {
        synchronized(botDataLock) {
            userMapUpdate.updateAll(event.jda.guilds)
        }
    }

    override fun onResume(event: ResumedEvent) {
        synchronized(botDataLock) {
            userMapUpdate.updateAll(event.jda.guilds)
        }
    }

    override fun onReady(event: ReadyEvent) {
        synchronized(botDataLock) {
            userMapUpdate.updateAll(event.jda.guilds)
            backupWrite()
        }
    }

    override fun onGuildJoin(event: GuildJoinEvent) {
        synchronized(botDataLock) {
            userMapUpdate.updateAll(event.jda.guilds)
        }
    }

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        synchronized(botDataLock) {
            userMapUpdate.updateOnUserJoin(event.guild.id, event.member.id)
        }
    }

    override fun onGuildMemberLeave(event: GuildMemberLeaveEvent) {
        synchronized(botDataLock) {
            userMapUpdate.updateOnUserLeave(event.guild.id, event.member.id)
        }
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {


        if(event.author.isBot)
            return

        val guild = event.guild
        val isPrivate = guild == null

        val prefix = if (isPrivate) {
            defaultPrefix
        } else {
            synchronized(botDataLock) {
                if (botData.prefixMap.get(guild.id) == null) {
                    defaultPrefix
                } else {
                    botData.prefixMap.get(guild.id)!!
                }
            }

        }
        var saveDate = false
        synchronized(botDataLock) {
            val massageHandler = MassageHandler(botData)
            saveDate = massageHandler.onMassage(event, prefix)
        }
        if(saveDate)
            backupWrite()
        println()
    }

    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {

    }


    override fun onDisconnect(event: DisconnectEvent) {
        synchronized(botDataLock) {
            backupWrite()
        }
    }




    fun backupWrite() {
        val dataClassString = Gson().typedToJson(botData)

        backupFile.writeText(dataClassString)

    }

    fun backupRead(): BotData {

        if (!backupFile.exists())
            return BotData()
        return Gson().fromJson(JsonParser().parse(backupFile.readText()))
    }

}