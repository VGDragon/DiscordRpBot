package com.vgdragon.funftions

import com.vgdragon.MassageFunktions.CreateOutputText
import com.vgdragon.MassageFunktions.SendingErrorText
import com.vgdragon.convertRichMessage
import com.vgdragon.dataclass.BotData
import com.vgdragon.dataclass.CharacterClass
import com.vgdragon.dataclass.GuildData
import com.vgdragon.dataclass.ServerUserData
import com.vgdragon.getServerUserData
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class ServerFuctions(val botData: BotData){

    val sendingErrorText = SendingErrorText()
    val createOutputText = CreateOutputText()



    fun serverMassage(event: MessageReceivedEvent,
    prefix: String,
    messageSplitted: MutableList<String> = mutableListOf()): Boolean {

        if(messageSplitted.size < 2){
            return defaultMassage(event, prefix)
        }
        messageSplitted.removeAt(0)

        return when(messageSplitted.get(0).toLowerCase()){
            "add" -> addCharacterToServer(event, prefix, messageSplitted)
            "remove" -> removeCharacterfromServer(event, prefix, messageSplitted)
            "rm" -> removeCharacterfromServer(event, prefix, messageSplitted)
            "list" -> {listCharacterOnServer(event, prefix, messageSplitted); false}
            "show" -> {showCharacterOnServer(event, prefix, messageSplitted); false}
            "limits" -> {showCharacterOnServer(event, prefix, messageSplitted); false}
            else -> defaultMassage(event, prefix)
        }
    }

    fun addCharacterToServer(event: MessageReceivedEvent,
                             prefix: String,
                             messageSplitted: MutableList<String> = mutableListOf()): Boolean {
        if(messageSplitted.size < 2){
            return defaultMassage(event, prefix)
        }
        messageSplitted.removeAt(0)

        val id = try {
            messageSplitted[0].trim().toInt()
        } catch (e: Exception){
            return defaultMassage(event, prefix)
        }

        val privateUserData = botData.privatUserData.get(event.author.id)
        if(privateUserData == null){
            sendingErrorText.noCharacterFromOtherUser(event)
            return false
        }

        val characterId = privateUserData.characters.get(id - 1)
        if(characterId == 0L){
            sendingErrorText.noCharacterInSlotFromOtherUser(event)
            return false
        }

        val serverUserData = getServerUserData(botData, event.guild.id, event.author.id) ?: ServerUserData(event.author.id)

        val characters = serverUserData.characters


        if(characters.contains(characterId)){
            sendingErrorText.thisCharacterIsOnServer(event)
            return false
        }
        val characterClass = botData.characters.get(characterId)
        if(addCharacterToServerCheck(event, characterClass!!, botData.guildDataClass.get(event.guild.id)!!) ){
            characters.add(characterId)
            event.channel.sendMessage("Character is added to the server.").submit()
            return true
        }

        return true
    }
    fun removeCharacterfromServer(event: MessageReceivedEvent,
                                  prefix: String,
                                  messageSplitted: MutableList<String> = mutableListOf()): Boolean {
        if(messageSplitted.size < 2){
            return defaultMassage(event, prefix)
        }
        messageSplitted.removeAt(0)

        val id = try {
            messageSplitted[0].trim().toInt()
        } catch (e: Exception){
            return defaultMassage(event, prefix)
        }

        val serverUserData =  getServerUserData(botData, event.guild.id, event.author.id)

        if(serverUserData == null){
            sendingErrorText.noCharacterOnServerOwn(event)
            return false
        }
        val characters = serverUserData.characters

        if(characters.size < id){
            sendingErrorText.noCharacterInSlotOwnCharaters(event)
            return false
        }

        characters.removeAt(id - 1)
        event.channel.sendMessage("Character is removed from the server.").submit()

        return true
    }

    fun listCharacterOnServer(event: MessageReceivedEvent,
                                prefix: String,
                                messageSplitted: MutableList<String> = mutableListOf()){
        val mentionedMembers = event.message.mentionedMembers
        val ownChar =
            event.message.mentionedRoles.isEmpty() && !event.message.mentionsEveryone() && !mentionedMembers.isNotEmpty()


        var userID = ""
        var userName = ""

        val ownCharList = if(ownChar && mentionedMembers.isEmpty()){
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



        var serverUserData = getServerUserData(botData, event.guild.id, userID)
        if (serverUserData == null){
            serverUserData = ServerUserData(userID)
        }



        val fieldList: MutableList<MessageEmbed.Field> = mutableListOf()
        fieldList.add(MessageEmbed.Field("Character List from $userName", "$userName have ${serverUserData.characters.size} Characters", false))


        for((i, charLong) in serverUserData.characters.withIndex()){
            val charClass = botData.characters.get(charLong) ?: continue

            createOutputText.characterList(fieldList, charClass, "ID ${i + 1}")
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
            true

        } else {
            val get = mentionedMembers.get(0)
            userID = get.id
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

        val serverUserData = getServerUserData(botData, event.guild.id, userID)


        if(serverUserData == null){
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
        event.channel.sendMessage(botData.characters.get(charLong)!!.getCharacterString()).submit()

    }


    fun defaultMassage(event: MessageReceivedEvent,
                       prefix: String): Boolean {
        val fieldList: MutableList<MessageEmbed.Field> = mutableListOf()
        fieldList.add(
            MessageEmbed.Field("add", "To make a Character to the server.\n" +
                    "Example: ${prefix}server add", true))
        fieldList.add(
            MessageEmbed.Field("remove", "To remove Character from the server.\n" +
                    "Example: ${prefix}server remove (id of the character)", true))
        fieldList.add(
            MessageEmbed.Field("list", "To get a list of your Characters or other users Characters.\n" +
                    "Example: ${prefix}server list [mention User]", true))
        fieldList.add(
            MessageEmbed.Field("show", "To show all infos of a Character.\n" +
                    "Example: ${prefix}server show (Character ID) [mention User]", true))
        fieldList.add(MessageEmbed.Field("Special Info", "() need to be included.\n[] is optional.", true))
        event.channel.sendMessage(convertRichMessage(fields = fieldList)).submit()
        return false
    }

    fun addCharacterToServerCheck(event: MessageReceivedEvent, characterClass: CharacterClass, guildData: GuildData): Boolean{
        if(!guildData.characterWithLimits && !guildData.characterModCheck)
            return true

        val serverLimitsComparing = if(guildData.characterWithLimits){
            characterClass.serverLimitsComparing(guildData.characterLimits)
        } else{
            mutableListOf()
        }
        if (!serverLimitsComparing.isEmpty()){
            event.channel.sendMessage(serverLimitsComparing.joinToString(separator = "\n")).submit()
            return false
        }
        if (guildData.characterModCheck) {
            //if (!guildData.modChannel.isNullOrBlank()) {
            //    val characterString = characterClass.getCharacterString()
            //    if(characterString.length > 2000){
            //        event.channel.sendFile(characterString.byteInputStream(),"Charater.txt").submit()
            //        val sendMessage = event.guild.getTextChannelById(guildData.modChannel)
            //            ?.sendMessage(characterClass.getCharacterString())
            //        if(sendMessage != null)
            //            sendMessage.submit()
            //    }
            //    val sendMessage = event.guild.getTextChannelById(guildData.modChannel)?.sendMessage(characterString)
            //    if(sendMessage != null)
            //        sendMessage.submit()
            //}
            if(guildData.charactersWaitingForModAccepeing == null)
                guildData.charactersWaitingForModAccepeing = mutableListOf()

            val isInList = if(guildData.charactersWaitingForModAccepeing.isNullOrEmpty()) {
                false
            } else {
                guildData.charactersWaitingForModAccepeing.find { it.second == characterClass.charId } != null
            }


            if (!isInList){
                guildData.charactersWaitingForModAccepeing.add(Pair(event.author.id, characterClass.charId))
                if (!guildData.modChannel.isNullOrEmpty()) {
                    val sendMessage = event.guild.getTextChannelById(guildData.modChannel)?.sendMessage("A new Character is waiting for being accepted")
                    if(sendMessage != null)
                        sendMessage.submit()
                }
                event.channel.sendMessage("You Character is submitted. A Mod need to confirm your Character before it is added to the server.")
                    .submit()
                return false
            }
            event.channel.sendMessage("This Character is already submitted.")
                .submit()
            return false
        }
        return true
    }

}