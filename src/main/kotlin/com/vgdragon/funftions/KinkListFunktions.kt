package com.vgdragon.funftions

import com.vgdragon.convertRichMessage
import com.vgdragon.dataclass.BotData
import com.vgdragon.kinkListDecoder
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class KinkListFunktions (val botData: BotData){

    val massageTitel = ""

    fun kinkLitsMassage(event: MessageReceivedEvent,
                        prefix: String,
                        massageSplitted: MutableList<String> = mutableListOf()){

        if(massageSplitted.size < 2){
            defaultMassage(event, prefix)
            return
        }
        massageSplitted.removeAt(0)

        when(massageSplitted[0].toLowerCase()){

            "update" -> kinkListUpdate(event, prefix, massageSplitted)
            "help" -> helpMessage(event, prefix, massageSplitted)


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
                       massageSplitted: MutableList<String> = mutableListOf()){

        if(massageSplitted.size < 2){
            return
        }
        massageSplitted.removeAt(0)

        try {
            val kinkListDecoder = kinkListDecoder(massageSplitted[0])
            val privateUserData = if(botData.privatUserData.get(event.author.id) != null){
                event.channel.sendMessage("KinkList-Update: User \"${event.author.name}\" not found.").submit()
                botData.privatUserData.get(event.author.id)
            } else {
                null
            }  ?: return

            privateUserData.kinks = kinkListDecoder

            val charactersLong = privateUserData.characters

            for(l in charactersLong){
                val characterClass = botData.characters[l] ?: continue
                characterClass.kinks = kinkListDecoder
            }


            event.channel.sendMessage("KinkList from \"${event.author.name}\" Updated").submit()

        } catch (e: Exception){}

    }


}