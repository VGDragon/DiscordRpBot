package com.vgdragon.funftions

import com.vgdragon.*
import com.vgdragon.dataclass.*
import net.dv8tion.jda.api.entities.MessageEmbed
import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class CharaterFunctions (val botData: BotData){

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
            "edit" -> characterEdit()
            "del" -> characterDel(event, prefix, messageSplitted)
            "delete" -> characterDel(event, prefix, messageSplitted)
            "list" -> characterList(event, prefix, messageSplitted)
            "show" -> characterShow(event, prefix, messageSplitted)
            "info" -> characterShow(event, prefix, messageSplitted)

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
            var user = privatUserData.get(event.author.id)
            if(user == null){
                noCharacterMassage(event)
                return
            }
            val characters = user.characters
            val get = characters.get(toInt)
            if (get == null){
                noCharacterInSlot(event)
                return
            }
            botData.characters.remove(get)
            characters.removeAt(toInt)
            event.channel.sendMessage("Your character Nr. ${toInt +1} is Deleted.").submit()
        } catch (e: Exception){
            e.printStackTrace()
        }

    }
    fun characterEdit(){

    }
    fun characterList(event: MessageReceivedEvent,
                      prefix: String,
                      messageSplitted: MutableList<String> = mutableListOf()){
        var privateUserData = botData.privatUserData.get(event.author.id)
        if (privateUserData == null){
            privateUserData = PrivateUserData(event.author.id)
            botData.privatUserData.put(event.author.id, privateUserData)
        }

        val fieldList: MutableList<MessageEmbed.Field> = mutableListOf()
        fieldList.add(MessageEmbed.Field("Character List from ${event.author.name}", "You have ${privateUserData.characters.size} Characters", true))
        val gd = botData.guildDataClass.get(event.guild.id)!!

        val serverUserCharacterMap: MutableMap<Int, Long> = if (event.channelType.isGuild) {
            gd.userMap.get(event.author.id)!!.characters
        } else {
            mutableMapOf()
        }

        for((i, charLong) in privateUserData.characters.withIndex()){
            val charClass = botData.characters.get(charLong) ?: continue
            val serverMassage = if(!event.isFromGuild){
                ""
            } else if(serverUserCharacterMap.containsValue(charLong)){
                "\nCharacter is on Server: Yes"
            } else{
                "\nCharacter is on Server: No"
            }


            fieldList.add(MessageEmbed.Field(
                "${i + 1}",
                "Name: ${charClass.name}\n" +
                        "Race: ${charClass.race}\n" +
                        "Gender: ${charClass.getGenderText()}\n" +
                        "Sexuality: ${charClass.getSexualityText()}\n" +
                        "Role: ${charClass.getRoleText()}" +
                        serverMassage,
                true))
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

            noCharacterMassage(event)
            return
        }
        val charLong = privateUserData.characters.get(charInt)
        if(charLong == null){
            noCharacterInSlot(event)
            return
        }
        event.channel.sendMessage(botData.characters.get(charLong)!!.getCharacterEmbed()).submit()
    }





    fun convertCharacterInfos(msg: String,
                              characterClass: CharacterClass) {

        val messageLines = msg.split("\n")

        val defaultVariableCharacter = "❥"
        val defaultIgnoreCharacter = "ღ"
        var currentVariable = ""
        var text = ""

        for (l in messageLines) {
            val trim = l.trim()
            if (trim.startsWith(defaultIgnoreCharacter)) {
                continue
            }
            if (trim.startsWith(defaultVariableCharacter)) {
                val stringTemp = trim.replace(defaultVariableCharacter, "")
                val split = stringTemp.split(":")
                if (!split.isNullOrEmpty()) {
                    setVariable(text, characterClass, currentVariable)


                    currentVariable = split.get(0).trim().toLowerCase()
                    var tempString02 = ""
                    for ((i, s) in split.withIndex()) {
                        if (i == 0)
                            continue
                        if (i == 1) {
                            tempString02 = s
                            continue
                        }
                        tempString02 += ":$s"
                    }
                    text = tempString02.trim()
                } else
                    text += trim


            } else {
                text += trim
            }


        }
        setVariable(text, characterClass, currentVariable)
    } //todo one line problem



    private fun setVariable(text: String ,characterClass: CharacterClass, currentVariable: String){
        when {
            currentVariable.contains("name", true) -> characterClass.name = text
            currentVariable.contains("nickname", true) -> characterClass.nickname = text
            currentVariable.contains("age", true) -> characterClass.age = ageString(text)
            currentVariable.contains("race", true) -> characterClass.race = text
            currentVariable.contains("gender", true) -> characterClass.gender = genderString(text)
            currentVariable.contains("Sexuality", true) -> characterClass.sexuality = sexualityString(text)
            currentVariable.contains("role", true) -> characterClass.role = roleString(text)
            currentVariable.contains("height", true) -> heightString(text, characterClass)
            currentVariable.contains("weight", true) -> weightString(text, characterClass)
            currentVariable.contains("build", true) -> characterClass.build = text
            currentVariable.contains("hair color", true) -> characterClass.hairColor = text
            currentVariable.contains("hair style", true) -> characterClass.hairStyle = text
            currentVariable.contains("eye color", true) -> characterClass.eyeColor = text
            currentVariable.contains("nationality", true) -> characterClass.nationality = text
            currentVariable.contains("tattoos:", true) -> characterClass.tattoos = text
            currentVariable.contains("piercings:", true) -> characterClass.piercings = text
            currentVariable.contains("markings", true) -> characterClass.bodyMarkingsOrScars = text
            currentVariable.contains("family:", true) -> characterClass.family = text
            currentVariable.contains("clothes:", true) -> characterClass.clothes = text
            currentVariable.contains("cup size", true) -> characterClass.cupSize = text
            currentVariable.contains("nipples sensitivity", true) -> characterClass.nippleSensitivity = text
            currentVariable.contains("cock length", true) -> lenghtString(text, characterClass)
            currentVariable.contains("cock thickness", true) -> thicknesString(text, characterClass)
            currentVariable.contains("sexual weakness", true) -> characterClass.sexualWeakness = text
            currentVariable.contains("powers", true) -> characterClass.powers = text
            currentVariable.contains("kinks", true) -> characterClass.kinksExtra = text
            currentVariable.contains("limits", true) -> characterClass.limitsExtra = text
            currentVariable.contains("personality", true) -> characterClass.personality = text
            currentVariable.contains("hobbies", true) -> characterClass.hobbies = text
            currentVariable.contains("dislikes", true) -> characterClass.name = text
            currentVariable.contains("likes", true) -> characterClass.likes = text
            currentVariable.contains("backstory", true) -> characterClass.backstory = text
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
            "1" -> return 1
            "man" -> return 2
            "boy" -> return 2
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
                characterClass.height = SizeCm(string.trim().toInt())
                characterClass.heightUS = convertCmInFeetAndInches(characterClass.height)
                return
            }

            val split = string.split("'")
            characterClass.heightUS = SizeFeetAndInches(split.get(0).trim().toInt(), split.get(1).trim().toDouble())
            characterClass.height = convertFeetAndInchesInCm(characterClass.heightUS)
        } catch (e: Exception){}
    }
    private fun weightString(string: String, characterClass: CharacterClass){
        try {
            if (string.toLowerCase().contains("lbs", true)) {
                val replace = string.replace("lbs", "", true)
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
            var replace = string.replace("cm", "")
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
            var replace = string.replace("cm", "")
            characterClass.cockThickness = SizeCm(replace.trim().toInt())
            characterClass.cockThicknessUS = convertCmInFeetAndInches(characterClass.cockThickness)
        }


    }

    fun defaultMassage(event: MessageReceivedEvent,
                       prefix: String){

    }
    fun noCharacterMassage(event: MessageReceivedEvent){
        event.channel.sendMessage("You don't have a character.").submit()
    }
    fun noCharacterInSlot(event: MessageReceivedEvent){
        event.channel.sendMessage("You don't have a character in that Slot.").submit()
    }

}

































