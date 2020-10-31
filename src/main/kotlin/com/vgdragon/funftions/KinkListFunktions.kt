package com.vgdragon.funftions

import com.vgdragon.convertRichMessage
import com.vgdragon.dataclass.BotData
import com.vgdragon.dataclass.PrivateUserData
import com.vgdragon.kinkListDecoder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class KinkListFunktions (val botData: BotData){

    val massageTitel = ""

    fun kinkLitsMassage(event: MessageReceivedEvent,
                        prefix: String,
                        massageSplitted: MutableList<String> = mutableListOf()): Boolean{

        if(massageSplitted.size < 2){
            return defaultMassage(event, prefix)
        }
        massageSplitted.removeAt(0)

        return when(massageSplitted[0].toLowerCase()){

            "update" -> kinkListUpdate(event, prefix, massageSplitted)
            "show" -> {showMessage(event, prefix, massageSplitted); true}
            "link" -> {linkMessage(event, prefix, massageSplitted); true}
            "image" -> imageMessage(event, prefix, massageSplitted)
            else -> defaultMassage(event, prefix)

        }


    }
    fun linkMessage(event: MessageReceivedEvent,
                    prefix: String,
                    massageSplitted: MutableList<String> = mutableListOf()){

        val fildList: MutableList<MessageEmbed.Field> = mutableListOf()
        fildList.add(MessageEmbed.Field("update",
            "To update your KinkList, you go to\n[https://cdn.rawgit.com/Goctionni/KinkList/master/v1.0.2.html](https://cdn.rawgit.com/Goctionni/KinkList/master/v1.0.2.html),\n" +
                    "fill it out, and post it with this command.\n\n" +
                    "Example: ${prefix}kinklist update (LINK)",
            true))

        event.channel.sendMessage(convertRichMessage(title = massageTitel, fields = fildList)).submit()


    }
    fun showMessage(event: MessageReceivedEvent,
                    prefix: String,
                    massageSplitted: MutableList<String> = mutableListOf()){

        var privateUserData = botData.privatUserData.get(event.author.id)
        if(privateUserData == null){
           privateUserData = PrivateUserData(event.author.id)
            botData.privatUserData.put(event.author.id, privateUserData)
        }

        val fieldList: MutableList<MessageEmbed.Field> = mutableListOf()
        fieldList.add(
            MessageEmbed.Field("Link", privateUserData.kinks.link, false))

        event.channel.sendMessage(convertRichMessage(fields = fieldList, image = MessageEmbed.ImageInfo(privateUserData.kinks.picLink, "",500, 500))).submit()


    }
    fun imageMessage(event: MessageReceivedEvent,
                    prefix: String,
                    massageSplitted: MutableList<String> = mutableListOf()): Boolean {
        if(massageSplitted.size < 2){
            return defaultMassage(event, prefix)
        }
        massageSplitted.removeAt(0)

        var privateUserData = botData.privatUserData.get(event.author.id)
        if(privateUserData == null){
            privateUserData = PrivateUserData(event.author.id)
            botData.privatUserData.put(event.author.id, privateUserData)
        }
        privateUserData.kinks.picLink = massageSplitted.get(0)


        event.channel.sendMessage("Picture Link added.").submit()
        return true
    }


    fun defaultMassage(event: MessageReceivedEvent,
                       prefix: String): Boolean {
        val fieldList: MutableList<MessageEmbed.Field> = mutableListOf()
        fieldList.add(
            MessageEmbed.Field("update", "To update your KinkList.\n" +
                    "Example: ${prefix}kinklist update", true))
        fieldList.add(
            MessageEmbed.Field("show", "To show a Character from the List of Characters, that are waiting to be accepted.\n" +
                    "Example: ${prefix}kinklist show", true))
        fieldList.add(
            MessageEmbed.Field("link", "To get the KinkList Link.\n" +
                    "Example: ${prefix}kinklist link", true))
        fieldList.add(
            MessageEmbed.Field("image", "To add an Image of your KinkList, that will be shown, if someone look in your KinkList.\n" +
                    "Example: ${prefix}kinklist image (picture Link)", true))
        fieldList.add(MessageEmbed.Field("Special Info", "() need to be included.\n[] is optional.", true))
        event.channel.sendMessage(convertRichMessage(fields = fieldList)).submit()
        return false
    }


    fun kinkListUpdate(event: MessageReceivedEvent,
                       prefix: String,
                       massageSplitted: MutableList<String> = mutableListOf()): Boolean{

        if(massageSplitted.size < 2){
            return false
        }
        massageSplitted.removeAt(0)

        try {
            val kinkListDecoder = kinkListDecoder(massageSplitted[0])
            var privateUserData = botData.privatUserData.get(event.author.id)

            if(privateUserData == null){
                privateUserData = PrivateUserData(event.author.id)
                botData.privatUserData.put(event.author.id, privateUserData)
            }

            privateUserData.kinks = kinkListDecoder

            val charactersLong = privateUserData.characters

            for(l in charactersLong){
                val characterClass = botData.characters[l] ?: continue
                characterClass.kinks = kinkListDecoder
            }

            event.channel.sendMessage("KinkList from \"${event.author.name}\" Updated").submit()
            return true
        } catch (e: Exception){}
        return false
    }


}