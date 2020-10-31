package com.vgdragon.MassageFunktions


import com.vgdragon.convertRichMessage
import com.vgdragon.dataclass.BotData
import com.vgdragon.funftions.*
import com.vgdragon.getServerUserData
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class MassageHandler (val botData: BotData){
    val charaterFunctions =  CharaterFunctions(botData)
    val kinkListFunktions = KinkListFunktions(botData)
    val serverFuctions = ServerFuctions(botData)
    val modFuctions = ModFunction(botData)





    fun onMassage(event: MessageReceivedEvent, prefix: String): Boolean{
        if(easteregg(event, prefix))
            return false
        val contentRaw = event.message.contentRaw
        if(!contentRaw.startsWith(prefix, true)){
            onMassageWithoutPrefix(event)
            return false
        }
        val substring = contentRaw.substring(prefix.length)
        val split = substring.split(" ")
        var massageSplitList: MutableList<String> = mutableListOf()

        for (s in split){
            if(!s.isNullOrBlank())
                massageSplitList.add(s)
        }

        if(massageSplitList.size < 1){
            return defaultMassage(event, prefix)
        }

        return when(massageSplitList[0].toLowerCase()){
            "character" -> charaterFunctions.characterMassage(event, prefix, massageSplitList)
            "char" -> charaterFunctions.characterMassage(event, prefix, massageSplitList)
            "kink" -> kinkListFunktions.kinkLitsMassage(event, prefix, massageSplitList)
            "kinklist" -> kinkListFunktions.kinkLitsMassage(event, prefix, massageSplitList)
            "server" -> {
                if(event.isFromGuild) {
                    serverFuctions.serverMassage(event, prefix, massageSplitList)
                }else {
                    event.channel.sendMessage("This Functions only works on a server.")
                    false
                }
            }
            "mod" -> {
                if(event.isFromGuild) {
                    modFuctions.modMassage(event, prefix, massageSplitList)
                } else {
                    event.channel.sendMessage("This Functions only works on a server.")
                    false
                }
            }
            "invite" -> {
                inviteMassage(event)
                false
            }

            else -> defaultMassage(event, prefix)

        }



    }
    fun defaultMassage(event: MessageReceivedEvent, prefix: String): Boolean {
        val fieldList: MutableList<MessageEmbed.Field> = mutableListOf()
        fieldList.add(
            MessageEmbed.Field("character", "To work on your private Characters.\n" +
                    "Example: ${prefix}character", true))
        fieldList.add(
            MessageEmbed.Field("kinklist", "To set up your Kinks.\n" +
                    "Example: ${prefix}kinklist", true))
        fieldList.add(
            MessageEmbed.Field("server", "To transfer your character on a server or show characters of other users.\n" +
                    "Example: ${prefix}server", true))
        fieldList.add(
            MessageEmbed.Field("mod", "For moderating related stuff. It can only be used from the server owner or in the Mod Channel.\n" +
                    "Example: ${prefix}mod", true))
        fieldList.add(
            MessageEmbed.Field("invite", "To get the bot invite link.\n" +
                    "Example: ${prefix}invite", true))
        fieldList.add(MessageEmbed.Field("Special Info", "For more Infos, use the one of the examples to get the commands of the category.", true))
        event.channel.sendMessage(convertRichMessage(fields = fieldList)).submit()
        return false
    }

    fun onMassageWithoutPrefix(event: MessageReceivedEvent){


        var contentRaw = event.message.contentRaw

        if(contentRaw.length < 4)
            return
        if(!contentRaw.startsWith(">"))
            return

        contentRaw = contentRaw.substring(1)

        val endOfNumberIndex = contentRaw.indexOf(">")
        if(contentRaw.length < endOfNumberIndex + 1)
            return

        val characterID = try {
            contentRaw.substringBefore(">").toInt()
        } catch (e:Exception){
            return
        }
        contentRaw = contentRaw.substringAfter(">")

        val serverUserData = getServerUserData(botData, event.guild.id, event.author.id) ?: return
        if(serverUserData.characters.size < characterID)
            return
        val characterLong = serverUserData.characters.get(characterID - 1) ?: return
        if(characterLong < 0)
            return
        val characterClass = botData.characters.get(characterLong) ?: return

        val characterName = if(characterClass.name.isBlank()){
            "Unknown"
        } else {
            characterClass.name
        }
        val characterPictureID = try {
            val picId = contentRaw.substringBefore(">").toInt()
            contentRaw = contentRaw.substringAfter(">")
            picId
        } catch (e:Exception){
            0
        }

        val characterImgList = characterClass.img

        val imgUrlString = if(characterImgList.isNullOrEmpty())
            ""
        else if (characterImgList.size < characterPictureID || characterPictureID == 0)
            characterImgList.get(0).link
        else {
            characterImgList.get(characterPictureID - 1).link
        }
        DiscordWebhook().sendMassage(event, contentRaw, characterName, imgUrlString)
        event.message.delete().submit()
    }

    fun inviteMassage(event: MessageReceivedEvent){
        event.channel.sendMessage("https://discord.com/api/oauth2/authorize?client_id=702957258585276528&permissions=536931328&scope=bot").submit()
    }

    fun easteregg(event: MessageReceivedEvent, prefix: String): Boolean{
        val contentRaw = event.message.contentRaw
        return when(contentRaw.toLowerCase()){
            "I made this bot" -> {
                if(event.author.id.equals("82982442461302784")) {
                    event.channel.sendMessage("He did.").submit()
                    true
                }else {
                    false
                }
            }
            "${prefix}VG Dragon" -> {
                event.channel.sendMessage("SUPPORT").submit()
                true
            }
            "${prefix}Nyan" -> {
                DiscordWebhook().sendMassage(event, "Nyan~", "Neko", "https://drive.google.com/uc?export=view&id=1U5Lrl_ttL8v05oBHzwOw9aaHSz2sBK0L")
                event.channel.sendMessage("SUPPORT").submit()
                true
            }

            else -> false
        }

    }
}