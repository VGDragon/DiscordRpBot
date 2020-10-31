package com.vgdragon.funftions

import com.vgdragon.convertCmInFeetAndInches
import com.vgdragon.convertFeetAndInchesInCm
import com.vgdragon.convertRichMessage
import com.vgdragon.dataclass.*
import net.dv8tion.jda.api.entities.Member
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.MessageReceivedEvent
import java.time.OffsetDateTime
import java.util.*

class ModFunction (val botData: BotData){


    fun modMassage(event: MessageReceivedEvent,
                   prefix: String,
                   messageSplitted: MutableList<String> = mutableListOf()): Boolean {
        val owner = event.guild.owner ?: return false
        var isOK = false
        if(event.author.id.equals(owner.id)){
            isOK = true
        }
        val guildData = botData.guildDataClass.get(event.guild.id)
        if(guildData == null)
            return false
        if(guildData.modChannel != null)
            if(event.channel.id.equals(guildData.modChannel))
                isOK = true
        if(!isOK)
            return false

        if(messageSplitted.size < 2){
            return defaultMassage(event, prefix)
        }
        messageSplitted.removeAt(0)

        return when(messageSplitted.get(0).toLowerCase()){
            "list" -> {listMassage(event, prefix, messageSplitted); false}
            "show" -> {showMassage(event, prefix, messageSplitted); false}
            "del" -> deleteMassage(event, prefix, messageSplitted)
            "delete" -> deleteMassage(event, prefix, messageSplitted)
            "decline" -> declineMassage(event, prefix, messageSplitted)
            "accept" ->acceptMassage(event, prefix, messageSplitted)
            "limit" -> limitMassage(event, prefix, messageSplitted)
            "limitlist" -> limitListMassage(event, prefix, messageSplitted)
            "modchannel" -> modChannelMassage(event, prefix, messageSplitted)
            "prune" -> {pruneMessage(event, prefix, messageSplitted); false}
            else -> defaultMassage(event, prefix)
        }

    }

    fun listMassage(event: MessageReceivedEvent,
                    prefix: String,
                    messageSplitted: MutableList<String> = mutableListOf()){
        messageSplitted.removeAt(0)
        var guildData = botData.guildDataClass.get(event.guild.id)

        if(guildData == null){
            guildData = GuildData(event.guild.id)
            botData.guildDataClass.put(event.guild.id, guildData)
        }

        val charactersWaitingForModAccepting = guildData.charactersWaitingForModAccepeing
        if(charactersWaitingForModAccepting.isEmpty()){
            event.channel.sendMessage("There are no Characters that are waiting for being accepted.")
                .submit()
            return
        }

        val page = try {
            if(!messageSplitted.isEmpty()){
                messageSplitted.get(0).toInt() - 1
            } else {
                0
            }
        } catch (e: Exception){
            0
        }

        var startID = if(page < 1){0} else {page * 5}
        val endID = startID + 5
        while (startID < endID){
            try {
                val embedFieldList: MutableList<MessageEmbed.Field> = mutableListOf()

                val pair = charactersWaitingForModAccepting.get(startID)
                if(pair == null){
                    break
                }
                val charaterClass = botData.characters.get(pair.second)
                if(charaterClass == null){
                    embedFieldList.add(MessageEmbed.Field("Mod List ID", "${startID + 1}", false))
                    embedFieldList.add(MessageEmbed.Field("Character", "Deleted/Not found", false))
                    event.channel.sendMessage(convertRichMessage(fields = embedFieldList))
                        .submit()
                    startID++
                    continue
                }
                embedFieldList.add(MessageEmbed.Field("Mod List ID", "${startID + 1}", false))
                embedFieldList.add(MessageEmbed.Field("User", event.guild.getMemberById(pair.first)!!.effectiveName, true))
                embedFieldList.add(MessageEmbed.Field("Name", charaterClass.name, true))
                embedFieldList.add(MessageEmbed.Field("Age", "${charaterClass.age}", true))
                embedFieldList.add(MessageEmbed.Field("Race", charaterClass.race, true))
                embedFieldList.add(MessageEmbed.Field("Gender", charaterClass.getRoleText(), true))
                embedFieldList.add(MessageEmbed.Field("Sexuality", charaterClass.getSexualityText(), true))
                embedFieldList.add(MessageEmbed.Field("Role", charaterClass.getRoleText(), true))
                event.channel.sendMessage(convertRichMessage(fields = embedFieldList))
                    .submit()
            } catch (e: Exception){
                break
            }

            startID++
        }
        if(startID == endID - 5){
            val embedFieldList: MutableList<MessageEmbed.Field> = mutableListOf()
            embedFieldList.add(MessageEmbed.Field("Mod List ID", "${startID + 1}", true))
            embedFieldList.add(MessageEmbed.Field("Character", "Not found", true))
            event.channel.sendMessage(convertRichMessage(fields = embedFieldList))
                .submit()
        }

    }
    fun showMassage(event: MessageReceivedEvent,
                    prefix: String,
                    messageSplitted: MutableList<String> = mutableListOf()){
        if(messageSplitted.size < 2){
            defaultMassage(event, prefix)
            return
        }
        messageSplitted.removeAt(0)

        val charModListID = try{
            messageSplitted.get(0).toInt()
        } catch (e: Exception){
            defaultMassage(event, prefix)
            return
        }

        var guildData = botData.guildDataClass.get(event.guild.id)

        if(guildData == null){
            guildData = GuildData(event.guild.id)
            botData.guildDataClass.put(event.guild.id, guildData)
        }

        val charactersWaitingForModAccepting = guildData.charactersWaitingForModAccepeing
        val characterClass = try {
            botData.characters.get(charactersWaitingForModAccepting.get(charModListID).second)
        } catch (e: Exception) {
            defaultMassage(event, prefix)
            return
        }
        if(characterClass == null ){
            defaultMassage(event, prefix)
            return
        }
        val characterString = characterClass.getCharacterString()
        if(characterString.length > 2000){
            event.channel.sendFile(characterString.byteInputStream(),"Charater.txt").submit()
            return
        }

        event.channel.sendMessage(characterString).submit()

    }
    fun acceptMassage(event: MessageReceivedEvent,
                    prefix: String,
                    messageSplitted: MutableList<String> = mutableListOf()): Boolean {
        if(messageSplitted.size < 2){
            return defaultMassage(event, prefix)
        }
        messageSplitted.removeAt(0)

        val charModListID = try{
            messageSplitted.get(0).toInt()
        } catch (e: Exception){
            return defaultMassage(event, prefix)
        }

        var guildData = botData.guildDataClass.get(event.guild.id)

        if(guildData == null){
            guildData = GuildData(event.guild.id)
            botData.guildDataClass.put(event.guild.id, guildData)
            event.channel.sendMessage("There are no Characters to be accepted.").submit()
            return true
        }

        val charactersWaitingForModAccepting = guildData.charactersWaitingForModAccepeing
        val waitingForAcceptingPair = try {
            charactersWaitingForModAccepting.get(charModListID -1)
        } catch (e: Exception) {
            event.channel.sendMessage("There is no Character with that ID in the List.").submit()
            return false
        }
        var serverUserData = guildData.userMap.get(waitingForAcceptingPair.first)
        if(serverUserData == null){
            serverUserData = ServerUserData(waitingForAcceptingPair.first)
            guildData.userMap.put(waitingForAcceptingPair.first, serverUserData)
        }

        if(!serverUserData.characters.contains(waitingForAcceptingPair.second)){
            serverUserData.characters.add(waitingForAcceptingPair.second)
        }

        charactersWaitingForModAccepting.removeAt(charModListID - 1)
        event.channel.sendMessage("The Character is Accepted.").submit()

        val user = event.jda.getUserById(waitingForAcceptingPair.first) ?: return true

        val privateUser = botData.privatUserData.get(user.id)
        if(privateUser == null){
            user.openPrivateChannel().submit().join().sendMessage("Your Character was accepted on the server ${event.guild.name}").submit()
            return true
        }

        user.openPrivateChannel().submit().join().sendMessage("Your Character with the ID ${privateUser.characters.indexOf(waitingForAcceptingPair.second) + 1} was accepted on the server ${event.guild.name}").submit()

        return true
    }
    fun deleteMassage(event: MessageReceivedEvent,
                    prefix: String,
                    messageSplitted: MutableList<String> = mutableListOf()): Boolean {
        if(messageSplitted.size < 2){
            return defaultMassage(event, prefix)
        }
        messageSplitted.removeAt(0)

        val charModListID = try{
            messageSplitted.get(0).toInt()
        } catch (e: Exception){
            return defaultMassage(event, prefix)
        }

        var guildData = botData.guildDataClass.get(event.guild.id)

        if(guildData == null){
            guildData = GuildData(event.guild.id)
            botData.guildDataClass.put(event.guild.id, guildData)
            event.channel.sendMessage("There are no Characters to be deleted.").submit()
            return true
        }

        val charactersWaitingForModAccepting = guildData.charactersWaitingForModAccepeing
        val characterClass = try {
            charactersWaitingForModAccepting.get(charModListID - 1)
        } catch (e: Exception) {
            event.channel.sendMessage("There is no Character with that ID in the List.").submit()
            return false
        }
        charactersWaitingForModAccepting.removeAt(charModListID - 1)

        event.channel.sendMessage("The Character is Deleted from the List.").submit()

        return true

    }
    fun declineMassage(event: MessageReceivedEvent,
                    prefix: String,
                    messageSplitted: MutableList<String> = mutableListOf()): Boolean {
        if(messageSplitted.size < 2){
            return defaultMassage(event, prefix)
        }
        messageSplitted.removeAt(0)

        val charModListID = try{
            messageSplitted.get(0).toInt()
        } catch (e: Exception){
            return defaultMassage(event, prefix)
        }

        var guildData = botData.guildDataClass.get(event.guild.id)

        if(guildData == null){
            guildData = GuildData(event.guild.id)
            botData.guildDataClass.put(event.guild.id, guildData)
            event.channel.sendMessage("There are no Characters to be declined.").submit()
            return true
        }

        val charactersWaitingForModAccepting = guildData.charactersWaitingForModAccepeing
        val waitingForAcceptingPair = try {
            charactersWaitingForModAccepting.get(charModListID - 1)
        } catch (e: Exception) {
            event.channel.sendMessage("There is no Character with that ID in the List.").submit()
            return false
        }
        var serverUserData = guildData.userMap.get(waitingForAcceptingPair.first)
        if(serverUserData == null){
            serverUserData = ServerUserData(waitingForAcceptingPair.first)
            guildData.userMap.put(waitingForAcceptingPair.first, serverUserData)
        }

        charactersWaitingForModAccepting.removeAt(charModListID - 1)
        event.channel.sendMessage("The Character is declined.").submit()

        val user = event.jda.getUserById(waitingForAcceptingPair.first) ?: return true
        val privateUser = botData.privatUserData.get(user.id)
        if(privateUser == null){
            user.openPrivateChannel().submit().join().sendMessage("Your Character was decline on the server ${event.guild.name}").submit()
            return true
        }

        user.openPrivateChannel().submit().join().sendMessage("Your Character with the ID ${privateUser.characters.indexOf(waitingForAcceptingPair.second) + 1} was decline on the server ${event.guild.name}").submit()

        return true
    }
    fun limitMassage(event: MessageReceivedEvent,
                       prefix: String,
                       messageSplitted: MutableList<String> = mutableListOf()): Boolean{
        if(messageSplitted.size < 2){
            return defaultMassage(event, prefix)
        }
        messageSplitted.removeAt(0)

        var guildData = botData.guildDataClass.get(event.guild.id)
        if(guildData == null) {
            guildData = GuildData(event.guild.id)
            botData.guildDataClass.put(event.guild.id, guildData)
        }

        val characterLimits  =
            if(guildData.characterLimits == null){
                guildData.characterLimits = CharacterLimits()
                guildData.characterLimits
            } else{
                guildData.characterLimits
            }
        for (s in messageSplitted){
            val split = s.toLowerCase().split("=")
            if(split.size < 2)
                continue

            if (split.get(1).equals("yes", true) && !characterLimits.neededChareacterInfo.contains(split.get(0))){
                characterLimits.neededChareacterInfo.add(split.get(0))
            }
            if (split.get(1).equals("no", true) && characterLimits.neededChareacterInfo.contains(split.get(0))){
                characterLimits.neededChareacterInfo.remove(split.get(0))
            }

            if (split.get(0).equals("characterLimits", true)){
                if(split.get(1).equals("yes", true))
                    guildData.characterWithLimits = true
                else if(split.get(1).equals("no", true))
                    guildData.characterWithLimits = false
                continue
            }

            if (split.get(0).equals("ModCheck", true)){
                if(split.get(1).equals("yes", true))
                    guildData.characterModCheck = true
                else if(split.get(1).equals("no", true))
                    guildData.characterModCheck = false
                continue
            }

            if (split.get(0).equals("minimumAge", true)){
                try {
                    val age = split.get(1).toInt()
                    if(age <= 0){
                        characterLimits.minimumAge = 0
                    } else {
                        characterLimits.minimumAge = age
                    }
                } catch (e: Exception){}
                continue
            }

            if (split.get(0).equals("maximumAge", true)){
                try {
                    val age = split.get(1).toInt()
                    if(age <= 0){
                        characterLimits.maximumAge = 0
                    } else {
                        characterLimits.maximumAge = age
                    }
                } catch (e: Exception){}
                continue
            }

            if (split.get(0).equals("minimumHeight", true)) {
                if(split.get(1).equals("0")){
                    characterLimits.minimumHeight = SizeCm(0)
                    characterLimits.minimumHeightUS = SizeFeetAndInches(0,0.0)
                    continue
                }
                try {
                    if (!split.get(1).contains("'")) {
                        characterLimits.minimumHeight =
                            SizeCm(split.get(1).toLowerCase().replace("cm", "").trim().toInt())
                        characterLimits.minimumHeightUS = convertCmInFeetAndInches(characterLimits.minimumHeight)
                        continue
                    }

                    val tempSplit = split.get(1).replace("ft", "").split("'")
                    characterLimits.minimumHeightUS =
                        SizeFeetAndInches(tempSplit.get(0).trim().toInt(), tempSplit.get(1).trim().toDouble())
                    characterLimits.minimumHeight = convertFeetAndInchesInCm(characterLimits.minimumHeightUS)
                    continue
                } catch (e: Exception) {
                }
                continue

            }

            if (split.get(0).equals("maximumHeight", true)) {
                if(split.get(1).equals("0")){
                    characterLimits.maximumHeight = SizeCm(Int.MAX_VALUE)
                    characterLimits.maximumHeightUS = SizeFeetAndInches(0,0.0)
                    continue
                }
                try {
                    if (!split.get(1).contains("'")) {
                        characterLimits.maximumHeight =
                            SizeCm(split.get(1).toLowerCase().replace("cm", "").trim().toInt())
                        characterLimits.maximumHeightUS = convertCmInFeetAndInches(characterLimits.minimumHeight)
                        continue
                    }

                    val tempSplit = split.get(1).replace("ft", "").split("'")
                    characterLimits.maximumHeightUS =
                        SizeFeetAndInches(tempSplit.get(0).trim().toInt(), tempSplit.get(1).trim().toDouble())
                    characterLimits.maximumHeight = convertFeetAndInchesInCm(characterLimits.maximumHeightUS)
                    continue
                } catch (e: Exception) {
                }
                continue

            }

        }
        event.channel.sendMessage("Limits settings updated.").submit()
        return true
    }

    fun limitListMassage(event: MessageReceivedEvent,
                         prefix: String,
                         messageSplitted: MutableList<String> = mutableListOf()): Boolean{

        var guildData = botData.guildDataClass.get(event.guild.id)
        if(guildData == null){
            guildData = GuildData(event.guild.id)
            botData.guildDataClass.put(event.guild.id, guildData)

            event.channel.sendMessage(guildData.characterLimits.serverLimitsThatCanBeSetted()).submit()
            return true
        }
        val characterLimits = if(guildData.characterLimits == null){
            guildData.characterLimits = CharacterLimits()
            guildData.characterLimits
        } else{
            guildData.characterLimits
        }

        event.channel.sendMessage(characterLimits.serverLimitsThatCanBeSetted()).submit()
        return false
    }
    fun modChannelMassage(event: MessageReceivedEvent,
                    prefix: String,
                    messageSplitted: MutableList<String> = mutableListOf()): Boolean {
        if(messageSplitted.size < 2){
            return defaultMassage(event, prefix)
        }
        messageSplitted.removeAt(0)
        val modChannel = event.guild.getTextChannelById(messageSplitted.get(0))
        if(modChannel == null){
            event.channel.sendMessage("This is not an ID of a Channel of this server.")
                .submit()
            return false
        }
        var guildData = botData.guildDataClass.get(event.guild.id)

        if(guildData == null){
            guildData = GuildData(event.guild.id)
            botData.guildDataClass.put(event.guild.id, guildData)
        }
        guildData.modChannel = modChannel.id
        modChannel.sendMessage("This Channel is now set as the Mod channel.")
            .submit()

        return true
    }

    fun pruneMessage(event: MessageReceivedEvent,
                     prefix: String,
                     messageSplitted: MutableList<String> = mutableListOf()): Boolean{

        if(messageSplitted.size < 2){
            return defaultMassage(event, prefix)
        }
        messageSplitted.removeAt(0)

        var withRole = if(messageSplitted.get(0).equals("1", true)){
            true
        } else if(messageSplitted.get(0).equals("0", true)){
            false
        } else {
            return false
        }
        messageSplitted.removeAt(0)

        var timeInMinutes = 0
        try {
            timeInMinutes = messageSplitted.get(0).toInt()
        } catch (e: Exception){
            timeInMinutes = 0
        }

        messageSplitted.removeAt(0)

        val members = event.guild.members
        val kickMeberList: MutableList<Member> = mutableListOf()

        if(withRole){
            for(m in members){
                val roles = m.roles
                if(roles == null){
                    kickMeberList.add(m)
                    continue
                }
                if (roles.isEmpty()){
                    kickMeberList.add(m)
                    continue
                }
                var roleMatch = 0
                for (r in roles){
                    if(r == null)
                        continue
                    if(messageSplitted.contains(r.id))
                        roleMatch++
                }
                if(roleMatch >= roles.size)
                    kickMeberList.add(m)
            }
        } else {
            if(messageSplitted.size < 1){
                return defaultMassage(event, prefix)
            }
            for(m in members){
                val roles = m.roles
                if(roles == null){
                    continue
                }
                if (roles.isEmpty()){
                    continue
                }
                var roleMatch = 0
                for (r in roles){
                    if(r == null)
                        continue
                    if(messageSplitted.contains(r.id))
                        roleMatch++
                }
                if(roleMatch >= messageSplitted.size)
                    kickMeberList.add(m)
            }
        }
        for (km in kickMeberList){
            val timeJoined = km.timeJoined.toInstant().toEpochMilli()
            if(System.currentTimeMillis() >= timeJoined + (timeInMinutes * 60)){
                km.kick().submit()
            }
        }

        return false
    } // todo withRole timeInMinutes Roles ...

    fun defaultMassage(event: MessageReceivedEvent,
                       prefix: String): Boolean {
        val fieldList: MutableList<MessageEmbed.Field> = mutableListOf()
        fieldList.add(
            MessageEmbed.Field("list", "To get a list of up to 5 Characters, that are waiting to be accepted.\n" +
                    "Example: ${prefix}mod list (page number)", true))
        fieldList.add(
            MessageEmbed.Field("show", "To show a Character from the List of Characters, that are waiting to be accepted.\n" +
                    "Example: ${prefix}mod show (id of the character)", true))
        fieldList.add(
            MessageEmbed.Field("delete", "To delete a Character from the List of Characters, that are waiting to be accepted.\n" +
                    "Example: ${prefix}mod delete (id of the character)", true))
        fieldList.add(
            MessageEmbed.Field("decline", "To decline a Character from the List of Characters, that are waiting to be accepted.\n" +
                    "Example: ${prefix}mod decline (id of the character)", true))
        fieldList.add(
            MessageEmbed.Field("accept", "To accept a Character from the List of Characters, that are waiting to be accepted.\n" +
                    "Example: ${prefix}mod accept (id of the character)", true))
        fieldList.add(
            MessageEmbed.Field("limit", "To set some limits for the characters on the server. Parameters that are set to \"yes\", are needed to be filled out on the Character.\n" +
                    "Example: ${prefix}mod limit name=yes minimumAge=18 minimumHeight=10 maximumHeight=9'10.1", true))
        fieldList.add(
            MessageEmbed.Field("limitlist", "To get a List of all limits that can be set.\n" +
                    "Example: ${prefix}mod limit", true))
        fieldList.add(
            MessageEmbed.Field("modchannel", "To set a channel, for Character related Notifications.\n" +
                    "Example: ${prefix}mod modchannel (id of the channel)", true))
        fieldList.add(MessageEmbed.Field("Special Info", "() need to be included.\n[] is optional.", true))
        event.channel.sendMessage(convertRichMessage(fields = fieldList)).submit()

        return false
    }
}