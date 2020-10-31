package com.vgdragon.funftions


import club.minnced.discord.webhook.WebhookClient
import com.vgdragon.defaultWebhookAvatarURL
import net.dv8tion.jda.api.entities.Icon
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.net.HttpURLConnection
import java.net.URL

class DiscordWebhook {


    fun sendMassage(event: MessageReceivedEvent, msg: String, name: String, avatarURL: String = ""){
        if(!event.isFromGuild)
            return
        val guildChannelById = event.guild.getTextChannelById(event.channel.id)

        val createWebhook = guildChannelById!!.createWebhook(name)
        println(avatarURL)


        val icon = try {
            val httpcon: HttpURLConnection = URL(avatarURL).openConnection() as HttpURLConnection;
            httpcon.addRequestProperty("User-Agent", "Mozilla/4.0")
            Icon.from(httpcon.getInputStream())
        }catch (e: Exception){
            e.printStackTrace()
            Icon.from(URL(defaultWebhookAvatarURL()).openStream())
        }

        createWebhook.setName(name)
        createWebhook.setAvatar(icon)
        val webhook = createWebhook.submit().join()

        val fromJDA = WebhookClient.fromJDA(webhook)
        fromJDA.send(msg).join()
        fromJDA.close()

        webhook.delete().submit()


    }

}