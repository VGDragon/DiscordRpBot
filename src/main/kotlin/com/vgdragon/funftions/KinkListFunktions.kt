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
            defaultMassage(event, prefix)
            return false
        }
        massageSplitted.removeAt(0)

        return when(massageSplitted[0].toLowerCase()){

            "update" -> kinkListUpdate(event, prefix, massageSplitted)
            "help" -> {helpMessage(event, prefix, massageSplitted); true}
            else -> false

        }


    }
    fun helpMessage(event: MessageReceivedEvent,
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

    fun defaultMassage(event: MessageReceivedEvent,
                       prefix: String){

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