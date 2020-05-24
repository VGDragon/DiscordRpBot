package com.vgdragon.MassageFunktions

import com.vgdragon.dataclass.BotData
import com.vgdragon.funftions.CharaterFunctions
import com.vgdragon.funftions.KinkListFunktions
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class MassageHandler (val botData: BotData){
    val charaterFunctions =  CharaterFunctions(botData)
    val kinkListFunktions = KinkListFunktions(botData)





    fun onMassage(event: MessageReceivedEvent, prefix: String){
        val contentRaw = event.message.contentRaw
        if(!contentRaw.startsWith(prefix)){
            onMassageWithoutPrefix(event)
            return
        }
        val substring = contentRaw.substring(prefix.length)
        val split = substring.split(" ")
        var massageSplitList: MutableList<String> = mutableListOf()

        for (s in split){
            if(!s.isNullOrBlank())
                massageSplitList.add(s)
        }

        if(massageSplitList.size < 1){
            defaultMassage(event)
            return
        }

        when(massageSplitList[0].toLowerCase()){
            "character" -> charaterFunctions.characterMassage(event, prefix, massageSplitList)
            "char" -> charaterFunctions.characterMassage(event, prefix, massageSplitList)
            "kink" -> kinkListFunktions.kinkLitsMassage(event, prefix, massageSplitList)
            "kinklist" -> kinkListFunktions.kinkLitsMassage(event, prefix, massageSplitList)



        }



    }
    fun defaultMassage(event: MessageReceivedEvent){

    }

    fun onMassageWithoutPrefix(event: MessageReceivedEvent){

    }
}