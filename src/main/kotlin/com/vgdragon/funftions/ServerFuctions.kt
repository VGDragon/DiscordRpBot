package com.vgdragon.funftions

import com.vgdragon.MassageFunktions.CreateOutputText
import com.vgdragon.MassageFunktions.SendingErrorText
import com.vgdragon.convertRichMessage
import com.vgdragon.dataclass.BotData
import com.vgdragon.dataclass.GuildData
import com.vgdragon.dataclass.ServerUserData
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class ServerFuctions(val botData: BotData){

    val sendingErrorText = SendingErrorText()
    val createOutputText = CreateOutputText()



    fun serverMassage(event: MessageReceivedEvent,
    prefix: String,
    messageSplitted: MutableList<String> = mutableListOf()){

        if(messageSplitted.size < 2){
            defaultMassage(event, prefix)
            return
        }
        messageSplitted.removeAt(0)

        when(messageSplitted.get(0).toLowerCase()){
            "add" -> addCharacterToServer(event, prefix, messageSplitted)
            "delete" -> deleteCharacterfromServer(event, prefix, messageSplitted)
            "del" -> deleteCharacterfromServer(event, prefix, messageSplitted)
            "list" -> listCharacterOnServer(event, prefix, messageSplitted)
            "show" -> showCharacterOnServer(event, prefix, messageSplitted)
        }
    }

    fun addCharacterToServer(event: MessageReceivedEvent,
                             prefix: String,
                             messageSplitted: MutableList<String> = mutableListOf()){
        if(messageSplitted.size < 2){
            defaultMassage(event, prefix)
            return
        }
        messageSplitted.removeAt(0)

        val id = try {
            messageSplitted[0].trim().toInt()
        } catch (e: Exception){
            defaultMassage(event, prefix)
            return
        }

        val privateUserData = botData.privatUserData.get(event.author.id)
        if(privateUserData == null){
            sendingErrorText.noCharacterFromOtherUser(event)
            return
        }

        val characterId = privateUserData.characters.get(id - 1)
        if(characterId == 0L){
            sendingErrorText.noCharacterInSlotFromOtherUser(event)
            return
        }

        var guildDataClass = botData.guildDataClass.get(event.guild.id)
        if(guildDataClass == null){
            guildDataClass = GuildData(event.guild.id)
            botData.guildDataClass.put(event.guild.id, guildDataClass)
        }

        var serverUserData = guildDataClass.userMap.get(event.author.id)

        if(serverUserData == null){
            serverUserData = ServerUserData(event.author.id)
            guildDataClass.userMap.put(event.author.id, serverUserData)
        }

        val characters = serverUserData.characters


        if(characters.contains(characterId)){
            sendingErrorText.thisCharacterIsOnServer(event)
            return
        }
        characters.add(characterId)
        event.channel.sendMessage("Character is added to the server.").submit()

    }
    fun deleteCharacterfromServer(event: MessageReceivedEvent,
                                  prefix: String,
                                  messageSplitted: MutableList<String> = mutableListOf()){
        if(messageSplitted.size < 2){
            defaultMassage(event, prefix)
            return
        }
        messageSplitted.removeAt(0)

        val id = try {
            messageSplitted[0].trim().toInt()
        } catch (e: Exception){
            defaultMassage(event, prefix)
            return
        }

        var guildDataClass = botData.guildDataClass.get(event.guild.id)
        if(guildDataClass == null){
            guildDataClass = GuildData(event.guild.id)
            botData.guildDataClass.put(event.guild.id, guildDataClass)
            sendingErrorText.noCharacterOnServerOwn(event)
            return
        }


        var serverUserData = guildDataClass.userMap.get(event.author.id)

        if(serverUserData == null){
            serverUserData = ServerUserData(event.author.id)
            guildDataClass.userMap.put(event.author.id, serverUserData)
            sendingErrorText.noCharacterOnServerOwn(event)

            return
        }
        val characters = serverUserData.characters

        if(characters.size <= id){
            sendingErrorText.noCharacterInSlotOwnCharaters(event)
            return
        }

        characters.removeAt(id - 1)
        event.channel.sendMessage("Character is deleted from the server.").submit()

    }

    fun listCharacterOnServer(event: MessageReceivedEvent,
                                prefix: String,
                                messageSplitted: MutableList<String> = mutableListOf()){
        val mentionedMembers = event.message.mentionedMembers

        var userID = ""
        var userName = ""

        val ownCharList = if(mentionedMembers.isEmpty()){
            val author = event.author
            userID = author.id
            userName = author.name
            true

        } else {
            val get = mentionedMembers.get(0)
            userID = get.id
            userName = get.effectiveName
            false
        }

        var guildDataClass = botData.guildDataClass.get(event.guild.id)
        if(guildDataClass == null){
            guildDataClass = GuildData(event.guild.id)
            botData.guildDataClass.put(event.guild.id, guildDataClass)
            if(ownCharList)
                sendingErrorText.noCharacterOnServerOwn(event)
            else
                sendingErrorText.noCharaterOnServerOtherUser(event)
            return
        }

        var serverUserClass = guildDataClass.userMap.get(userID)
        if (serverUserClass == null){
            serverUserClass = ServerUserData(userID)
            guildDataClass.userMap.put(userID, serverUserClass)
        }



        val fieldList: MutableList<MessageEmbed.Field> = mutableListOf()
        fieldList.add(MessageEmbed.Field("Character List from $userName", "$userName have ${serverUserClass.characters.size} Characters", true))


        for((i, charLong) in serverUserClass.characters.withIndex()){
            val charClass = botData.characters.get(charLong) ?: continue

            createOutputText.characterList(fieldList, charClass, "${i + 1}")
        }
        event.channel.sendMessage(convertRichMessage(fields = fieldList)).submit()

    }

    fun showCharacterOnServer(event: MessageReceivedEvent,
                              prefix: String,
                              messageSplitted: MutableList<String> = mutableListOf()){

        if(messageSplitted.size < 2){
            defaultMassage(event, prefix)
            return
        }
        val mentionedMembers = event.message.mentionedMembers

        var userID = ""
        var userName = ""

        val ownCharList = if(mentionedMembers.isEmpty()){
            val author = event.author
            userID = author.id
            userName = author.name
            true

        } else {
            val get = mentionedMembers.get(0)
            userID = get.id
            userName = get.effectiveName
            false
        }

        messageSplitted.removeAt(0)
        val charNumber = messageSplitted.get(0).trim()

        var charInt = 0
        try {
            charInt = charNumber.toInt() - 1
        } catch (e: Exception){
            defaultMassage(event, prefix)
            return
        }
        var guildData = botData.guildDataClass.get(event.guild.id)

        if(guildData == null){
            guildData = GuildData(event.guild.id)
            botData.guildDataClass.put(event.guild.id, guildData)
            if(ownCharList)
                sendingErrorText.noCharacterOnServerOwn(event)
            else
                sendingErrorText.noCharaterOnServerOtherUser(event)

            return
        }

        var serverUserData = guildData.userMap.get(userID)


        if(serverUserData == null){

            serverUserData = ServerUserData(userID)
            guildData.userMap.put(userID, serverUserData)
            if(ownCharList)
                sendingErrorText.noCharacterOnServerOwn(event)
            else
                sendingErrorText.noCharaterOnServerOtherUser(event)
            return
        }

        val charLong = serverUserData.characters.get(charInt)
        if(charLong == 0L){
            if(ownCharList)
                sendingErrorText.noCharacterInSlotOwnCharaters(event)
            else
                sendingErrorText.noCharacterInSlotFromOtherUser(event)
            return
        }
        event.channel.sendMessage(botData.characters.get(charLong)!!.getCharacterEmbed()).submit()

    }


    fun defaultMassage(event: MessageReceivedEvent,
                       prefix: String){
        val fieldList: MutableList<MessageEmbed.Field> = mutableListOf()
        fieldList.add(
            MessageEmbed.Field("add", "To make a new Character.\n" +
                    "Example: ${prefix}character add", true))
        fieldList.add(
            MessageEmbed.Field("delete", "To delete a Character.\n" +
                    "Example: ${prefix}character delete (id of the character)", true))
        fieldList.add(
            MessageEmbed.Field("list", "To get a list of your Characters or other users Characters.\n" +
                    "Example: ${prefix}character list [mention User]", true))
        fieldList.add(
            MessageEmbed.Field("show", "To show all infos of a Character.\n" +
                    "Example: ${prefix}character show (Character ID) [mention User]", true))
        fieldList.add(MessageEmbed.Field("Special Info", "() need to be included.\n[] is optional.", true))
        event.channel.sendMessage(convertRichMessage(fields = fieldList)).submit()
    }

}