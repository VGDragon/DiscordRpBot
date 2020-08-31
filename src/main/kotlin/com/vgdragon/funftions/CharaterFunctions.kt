package com.vgdragon.funftions

import com.vgdragon.*
import com.vgdragon.MassageFunktions.CreateOutputText
import com.vgdragon.MassageFunktions.SendingErrorText
import com.vgdragon.dataclass.*
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class CharaterFunctions (val botData: BotData){

    val sendingErrorText = SendingErrorText()
    val createOutputText = CreateOutputText()

    fun characterMassage(event: MessageReceivedEvent,
        prefix: String,
        messageSplitted: MutableList<String> = mutableListOf()){

            if(messageSplitted.size < 2){
                defaultMassage(event, prefix)
                return
            }
            messageSplitted.removeAt(0)

            when(messageSplitted.get(0).toLowerCase()){
                "add" -> characterAdd(event)
                "edit" -> characterEdit(event, prefix, messageSplitted)
                "del" -> characterDel(event, prefix, messageSplitted)
                "delete" -> characterDel(event, prefix, messageSplitted)
                "list" -> characterList(event)
                "show" -> characterShow(event, prefix, messageSplitted)
                "info" -> characterShow(event, prefix, messageSplitted)
                "template" -> characterTemplate(event)
            }




    }
    fun characterAdd(event: MessageReceivedEvent){
        val characterClass = CharacterClass()

        convertCharacterInfos(event.message.contentRaw, characterClass)

        characterClass.charId = botData.nextCharId
        val privatUserData = botData.privatUserData
        var user = privatUserData.get(event.author.id)
        if(user == null){
            user = PrivateUserData(event.author.id)
            privatUserData.put(event.author.id, user)
        }
        user.characters.add(botData.nextCharId)

        botData.nextCharId++
        event.channel.sendMessage("Character Added").submit()
    }
    fun characterDel(event: MessageReceivedEvent,
                     prefix: String,
                     messageSplitted: MutableList<String> = mutableListOf()){
        if(messageSplitted.size < 2){
            defaultMassage(event, prefix)
            return
        }
        try {
            val toInt = messageSplitted.get(1).trim().toInt() - 1
            val privatUserData = botData.privatUserData
            val user = privatUserData.get(event.author.id)
            if(user == null){
                sendingErrorText.noCharacterOwnCharaters(event)
                return
            }
            val characters = user.characters
            val characterID = characters.get(toInt)
            if (characterID == 0L){
                sendingErrorText.noCharacterInSlotOwnCharaters(event)
                return
            }
            botData.characters.remove(characterID)
            characters.removeAt(toInt)
            event.channel.sendMessage("Your character Nr. ${toInt +1} is Deleted.").submit()
        } catch (e: Exception){
            e.printStackTrace()
        }

    }
    fun characterEdit(event: MessageReceivedEvent,
                      prefix: String,
                      messageSplitted: MutableList<String> = mutableListOf()){
        if(messageSplitted.size < 2){
            defaultMassage(event, prefix)
            return
        }
        messageSplitted.removeAt(0)

        val characterID = try{
             messageSplitted[0].trim().toInt()
        } catch (e: Exception){
            defaultMassage(event, prefix)
            return
        }

        val userData = botData.privatUserData.get(event.author.id)
        if(userData == null){
            sendingErrorText.noCharacterOwnCharaters(event)
            return
        }

        val charaterId = try{
            userData.characters.get(characterID - 1)
        } catch (e: Exception){
            sendingErrorText.noCharacterInSlotOwnCharaters(event)
            return
        }

        val characterClass = botData.characters.get(charaterId)!!
        convertCharacterInfos(event.message.contentRaw, characterClass)


        event.channel.sendMessage("Character Updated").submit()
    }
    fun characterList(event: MessageReceivedEvent){
        var privateUserData = botData.privatUserData.get(event.author.id)
        if (privateUserData == null){
            privateUserData = PrivateUserData(event.author.id)
            botData.privatUserData.put(event.author.id, privateUserData)
        }

        val fieldList: MutableList<MessageEmbed.Field> = mutableListOf()
        fieldList.add(MessageEmbed.Field("Character List from ${event.author.name}", "You have ${privateUserData.characters.size} Characters", true))
        val gd = botData.guildDataClass.get(event.guild.id)!!

        val serverUserCharacterMap = if (event.channelType.isGuild) {
            gd.userMap.get(event.author.id)!!.characters
        } else {
            mutableListOf()
        }

        for((i, charLong) in privateUserData.characters.withIndex()){
            val charClass = botData.characters.get(charLong) ?: continue
            val serverMassage = if(!event.isFromGuild){
                ""
            } else if(serverUserCharacterMap.contains(charLong)){
                "\nCharacter is on Server: Yes"
            } else{
                "\nCharacter is on Server: No"
            }

            createOutputText.characterList(fieldList, charClass, "${i + 1}", serverMassage)
        }
        event.channel.sendMessage(convertRichMessage(fields = fieldList)).submit()
    }
    fun characterShow(event: MessageReceivedEvent,
                      prefix: String,
                      messageSplitted: MutableList<String> = mutableListOf()){
        if(messageSplitted.size < 2){
            defaultMassage(event, prefix)
            return
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
        val privateUserData = botData.privatUserData.get(event.author.id)
        if(privateUserData == null){

            sendingErrorText.noCharacterOwnCharaters(event)
            return
        }
        val charLong = privateUserData.characters.get(charInt)
        if(charLong == 0L){
            sendingErrorText.noCharacterInSlotOwnCharaters(event)
            return
        }
        event.channel.sendMessage(botData.characters.get(charLong)!!.getCharacterEmbed()).submit()
    }
    fun characterTemplate(event: MessageReceivedEvent){
        val massageString =
            "\$name \n" +
                "\$nickname \n" +
                "\$age \n" +
                "\$race \n" +
                "\$gender \n" +
                "\$sexuality \n" +
                "\$role \n" +
                "\$height \n" +
                "\$weight \n" +
                "\$build \n" +
                "\$hair_color \n" +
                "\$hair_style \n" +
                "\$eye_color \n" +
                "\$nationality \n" +
                "\$tattoos \n" +
                "\$piercings \n" +
                "\$markings \n" +
                "\$family \n" +
                "\$clothes \n" +
                "\$cup_size \n" +
                "\$nipples_sensitivity \n" +
                "\$cock_length \n" +
                "\$cock_thickness \n" +
                "\$sexual_weakness \n" +
                "\$powers \n" +
                "\$kinks \n" +
                "\$limits \n" +
                "\$personality \n" +
                "\$hobbies \n" +
                "\$dislikes \n" +
                "\$likes \n" +
                "\$backstory "

        event.channel.sendMessage(massageString).submit()
        val fieldList: MutableList<MessageEmbed.Field> = mutableListOf()
        fieldList.add(MessageEmbed.Field("age", "Only numbers supported.", true))
        fieldList.add(MessageEmbed.Field("gender", "Male, Female or Futa.", true))
        fieldList.add(MessageEmbed.Field("sexuality", "Straight, Homosexual or Bi.", true))
        fieldList.add(MessageEmbed.Field("role", "Sub, Switch or Dom", true))
        fieldList.add(MessageEmbed.Field("height", "We support cm and foot. If you use foot, please use \"'\" to separate the numbers. Use cm or cm ft the number, to categorise it.", true))
        fieldList.add(MessageEmbed.Field("weight", "We support kg and pound. Use kg or lbs after the number, to categorise it.", true))
        fieldList.add(MessageEmbed.Field("cock_length", "We support cm and inches. Use cm or in after the number, to categorise it.", true))
        fieldList.add(MessageEmbed.Field("cock_thickness", "We support cm and inches. Use cm or in after the number, to categorise it.", true))
        fieldList.add(MessageEmbed.Field("You want more?", "Ask the Bot Admin if you want to add more ${botAdminName()}.", true))
        event.channel.sendMessage(convertRichMessage(fields = fieldList)).submit()
    }




    fun convertCharacterInfos(msg: String,
                              characterClass: CharacterClass) {



        val defaultVariableCharacter = "$"
        val defaultIgnoreCharacterStart = "["
        val defaultIgnoreCharacterEnd = "]"


        val tempSplit = msg.split(defaultIgnoreCharacterStart)
        var msgNew = ""

        for((i, s ) in tempSplit.withIndex()){
            if(i == 0){
                msgNew = s.trim()
            }
            val split = s.split(defaultIgnoreCharacterEnd)
            if(split.size > 1){
                msgNew += split[1].trim()
                continue
            }
            msgNew += split[0].trim()

        }

        val workSplit = msgNew.split(defaultVariableCharacter)

        for (l in workSplit) {
            val trim = l.trim()

            val split = trim.split(" ")

            if(split.size < 2){
                continue
            }
            val currentVariable = split[0]
            var text = ""

            for ((i, s) in split.withIndex()) {
                if (i == 0)
                    continue
                if(i == 1) {
                    text = s
                    continue
                }
                text += " $s"
            }
            setVariable(text, characterClass, currentVariable)



        }
    }
    private fun setVariable(text: String ,characterClass: CharacterClass, currentVariable: String){
        when {
            currentVariable.equals("name", true) -> characterClass.name = text
            currentVariable.equals("nickname", true) -> characterClass.nickname = text
            currentVariable.equals("age", true) -> characterClass.age = ageString(text)
            currentVariable.equals("race", true) -> characterClass.race = text
            currentVariable.equals("gender", true) -> characterClass.gender = genderString(text)
            currentVariable.equals("sexuality", true) -> characterClass.sexuality = sexualityString(text)
            currentVariable.equals("role", true) -> characterClass.role = roleString(text)
            currentVariable.equals("height", true) -> heightString(text, characterClass)
            currentVariable.equals("weight", true) -> weightString(text, characterClass)
            currentVariable.equals("build", true) -> characterClass.build = text
            currentVariable.equals("hair_color", true) -> characterClass.hairColor = text
            currentVariable.equals("hair_style", true) -> characterClass.hairStyle = text
            currentVariable.equals("eye_color", true) -> characterClass.eyeColor = text
            currentVariable.equals("nationality", true) -> characterClass.nationality = text
            currentVariable.equals("tattoos:", true) -> characterClass.tattoos = text
            currentVariable.equals("piercings:", true) -> characterClass.piercings = text
            currentVariable.equals("markings", true) -> characterClass.bodyMarkingsOrScars = text
            currentVariable.equals("family:", true) -> characterClass.family = text
            currentVariable.equals("clothes:", true) -> characterClass.clothes = text
            currentVariable.equals("cup_size", true) -> characterClass.cupSize = text
            currentVariable.equals("nipples_sensitivity", true) -> characterClass.nippleSensitivity = text
            currentVariable.equals("cock_length", true) -> lenghtString(text, characterClass)
            currentVariable.equals("cock_thickness", true) -> thicknesString(text, characterClass)
            currentVariable.equals("sexual_weakness", true) -> characterClass.sexualWeakness = text
            currentVariable.equals("powers", true) -> characterClass.powers = text
            currentVariable.equals("kinks", true) -> characterClass.kinksExtra = text
            currentVariable.equals("limits", true) -> characterClass.limitsExtra = text
            currentVariable.equals("personality", true) -> characterClass.personality = text
            currentVariable.equals("hobbies", true) -> characterClass.hobbies = text
            currentVariable.equals("dislikes", true) -> characterClass.name = text
            currentVariable.equals("likes", true) -> characterClass.likes = text
            currentVariable.equals("backstory", true) -> characterClass.backstory = text
        }
    }


    private fun ageString(string: String): Int{
        try {
            return string.trim().toInt()
        } catch (e: Exception){}
        return 0
    }
    private fun genderString(string: String): Int{
        when(string.toLowerCase().trim()){
            "woman" -> return 1
            "girl" -> return 1
            "female" -> return 1
            "1" -> return 1
            "man" -> return 2
            "boy" -> return 2
            "male" -> return 2
            "2" -> return 2
            "futa" -> return 3
            "3" -> return 3

            else -> return 0
        }
    }
    private fun sexualityString(string: String): Int{
        when(string.toLowerCase().trim()){
            "straight" -> return 1
            "1" -> return 1
            "gay" -> return 2
            "homosexual" -> return 2
            "2" -> return 2
            "bi" -> return 3
            "pansexual" -> return 3
            "3" -> return 3

            else -> return 0
        }

    }
    private fun roleString(string: String): Int{
        when(string.toLowerCase().trim()){
            "sub" -> return 1
            "1" -> return 1
            "switch" -> return 2
            "2" -> return 2
            "dom" -> return 3
            "3" -> return 3

            else -> return 0
        }
    }
    private fun heightString(string: String, characterClass: CharacterClass){
        try {
            if (!string.contains("'")) {
                characterClass.height = SizeCm(string.toLowerCase().replace("cm", "").trim().toInt())
                characterClass.heightUS = convertCmInFeetAndInches(characterClass.height)
                return
            }

            val split = string.replace("ft","").split("'")
            characterClass.heightUS = SizeFeetAndInches(split.get(0).trim().toInt(), split.get(1).trim().toDouble())
            characterClass.height = convertFeetAndInchesInCm(characterClass.heightUS)
        } catch (e: Exception){}
    }
    private fun weightString(string: String, characterClass: CharacterClass){
        try {
            if (string.toLowerCase().contains("lbs", true) || string.toLowerCase().contains("lb", true)) {
                val replace = string.replace("lbs", "", true).replace("lb", "", true)
                characterClass.weightUS = replace.trim().toInt()
                characterClass.weight = convertLbsToKg(characterClass.weightUS)
                return
            }

            val replace = string.replace("kg", "", true)
            characterClass.weight = replace.trim().toInt()
            characterClass.weightUS = convertKgToLbs(characterClass.weight)


        } catch (e: Exception){}

    }
    private fun lenghtString(string: String, characterClass: CharacterClass){
        if(string.toLowerCase().contains("inch") || string.toLowerCase().contains("inches") || string.toLowerCase().contains("in")){
            var replace = string.replace("inch", "")
            replace = replace.replace("inches", "")
            replace = replace.replace("in", "")
            characterClass.cockLengthUS = SizeFeetAndInches(0, replace.trim().toDouble())
            characterClass.cockLength = convertFeetAndInchesInCm(characterClass.cockLengthUS)
            return
        }

        if(string.toLowerCase().contains("cm")){
            val replace = string.replace("cm", "")
            characterClass.cockLength = SizeCm(replace.trim().toInt())
            characterClass.cockLengthUS = convertCmInFeetAndInches(characterClass.cockLength)
        }

    }
    private fun thicknesString(string: String, characterClass: CharacterClass){

        if(string.toLowerCase().contains("inch") || string.toLowerCase().contains("inches") || string.toLowerCase().contains("in")){
            var replace = string.replace("inch", "")
            replace = replace.replace("inches", "")
            replace = replace.replace("in", "")
            characterClass.cockThicknessUS = SizeFeetAndInches(0, replace.trim().toDouble())
            characterClass.cockThickness = convertFeetAndInchesInCm(characterClass.cockThicknessUS)
            return
        }

        if(string.toLowerCase().contains("cm")){
            characterClass.cockThickness = SizeCm(string.replace("cm", "").trim().toInt())
            characterClass.cockThicknessUS = convertCmInFeetAndInches(characterClass.cockThickness)
        }


    }

    fun defaultMassage(event: MessageReceivedEvent,
                       prefix: String){
        val fieldList: MutableList<MessageEmbed.Field> = mutableListOf()
        fieldList.add(MessageEmbed.Field("add", "To make a new Character.\n" +
                "Example: ${prefix}character add [character Info]", true))
        fieldList.add(MessageEmbed.Field("edit", "To edit a Character.\n" +
                "Example: ${prefix}character edit (id of the character) [character Info]", true))
        fieldList.add(MessageEmbed.Field("delete", "To delete a Character.\n" +
                "Example: ${prefix}character delete (id of the character)", true))
        fieldList.add(MessageEmbed.Field("list", "To get a list of your Characters.\n" +
                "Example: ${prefix}character list", true))
        fieldList.add(MessageEmbed.Field("show", "To show all infos of a Characters.\n" +
                "Example: ${prefix}character show", true))
        fieldList.add(MessageEmbed.Field("template", "To show all infos you can put in your Characters and some infos.\n" +
                "Example: ${prefix}character template", true))
        fieldList.add(MessageEmbed.Field("Special Info", "() need to be included.\n[] is optional.", true))
        event.channel.sendMessage(convertRichMessage(fields = fieldList)).submit()
    }



}

































