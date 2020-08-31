package com.vgdragon.funftions


import net.dv8tion.jda.api.entities.Icon
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.net.URL

class DiscordWebhook {


    fun sendMassage(event: MessageReceivedEvent, msg: String, name: String, avatarURL: String = ""){
        if(!event.isFromGuild)
            return
        val guildChannelById = event.guild.getTextChannelById(event.channel.id)

        val createWebhook = guildChannelById!!.createWebhook(name)
        val icon = Icon.from(URL(avatarURL).openStream())

        createWebhook.setName(name)
        createWebhook.setAvatar(icon)
        val webhook = createWebhook.complete()
        webhook.channel.sendMessage(msg)
        webhook.delete()

    }

}