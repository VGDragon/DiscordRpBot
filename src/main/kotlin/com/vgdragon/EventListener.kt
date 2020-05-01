package com.vgdragon

import com.github.salomonbrys.kotson.fromJson
import com.github.salomonbrys.kotson.typedToJson
import com.google.gson.Gson
import com.google.gson.JsonParser
import com.vgdragon.dataclass.BotData
import com.vgdragon.dataclass.GuildData
import com.vgdragon.dataclass.ServerUserData
import com.vgdragon.funftions.UserMapUpdate
import net.dv8tion.jda.api.events.DisconnectEvent
import net.dv8tion.jda.api.events.ReconnectedEvent
import net.dv8tion.jda.api.events.guild.GuildJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberJoinEvent
import net.dv8tion.jda.api.events.guild.member.GuildMemberLeaveEvent
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import net.dv8tion.jda.api.events.message.react.MessageReactionAddEvent
import net.dv8tion.jda.api.hooks.ListenerAdapter
import java.io.File


class EventListener : ListenerAdapter() {

    val botData = backupRead()
    val botDataLock = Object()

    val userMapUpdate = UserMapUpdate(botData.guildDataClass, botDataLock)
    val defaultPrefix = "rp!"

    val backupFile = File("DiscordRpBotData.json")


    override fun onReconnect(event: ReconnectedEvent) {
        userMapUpdate.updateAll(event.jda.guilds)
    }

    override fun onGuildJoin(event: GuildJoinEvent) {
        userMapUpdate.updateAll(event.jda.guilds)
    }

    override fun onGuildMemberJoin(event: GuildMemberJoinEvent) {
        userMapUpdate.updateOnUserJoin(event.guild.id, event.member.id)
    }

    override fun onGuildMemberLeave(event: GuildMemberLeaveEvent) {
        userMapUpdate.updateOnUserLeave(event.guild.id, event.member.id)
    }

    override fun onMessageReceived(event: MessageReceivedEvent) {

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


    }

    override fun onMessageReactionAdd(event: MessageReactionAddEvent) {

    }


    override fun onDisconnect(event: DisconnectEvent) {
        backupWrite()
    }

    fun backupWrite() {
        val dataClassString = synchronized(botDataLock) {
            Gson().typedToJson(botData)
        }
        backupFile.writeText(dataClassString)

    }

    fun backupRead(): BotData {
        if (!backupFile.exists())
            return BotData()
        return Gson().fromJson(JsonParser().parse(backupFile.readText()))
    }

}