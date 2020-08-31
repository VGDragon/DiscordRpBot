package com.vgdragon.dataclass

import com.vgdragon.convertRichMessage
import net.dv8tion.jda.api.entities.MessageEmbed

class CharacterClass {
    var msgId = ""
    var charId = 0L

    var img: MutableList<CharacterImg> = mutableListOf()
    var link = ""

    var name = ""
    var nickname = ""
    var age = 0
    var race = ""
    var gender = 0 // 1 = Woman; 2 = Man; 3 = Futa;
    var sexuality = 0 // 1 = Straight; 2 = Gay/Homosexual; 3 = Bi/Pansexual
    var role = 0 // 1 = sub; 2 = switch; 3 = dom
    var height = SizeCm(0)
    var heightUS = SizeFeetAndInches(0,0.0)
    var weight = 0
    var weightUS = 0
    var build = ""
    var hairColor = ""
    var hairStyle = ""
    var eyeColor = ""
    var nationality = ""
    var tattoos = ""
    var piercings = ""
    var bodyMarkingsOrScars = ""
    var family = ""
    var clothes = ""
    var cupSize = ""
    var nippleSensitivity = ""
    var cockLength = SizeCm(0)
    var cockLengthUS = SizeFeetAndInches(0,0.0)
    var cockThickness = SizeCm(0)
    var cockThicknessUS = SizeFeetAndInches(0,0.0)
    var sexualWeakness = "" //
    var kinks = KinkList()
    var kinksExtra = "" //
    var limitsExtra = "" //
    var powers = ""
    var personality = ""
    var hobbies = ""
    var dislikes = ""
    var likes = ""
    var backstory = ""

    fun getGenderText(): String{
        when(gender){
            1 -> return "Woman"
            2 -> return "Man"
            3 -> return "Futa"
            else -> return "N/A"
        }
    }
    fun getSexualityText(): String{
        when(sexuality){
            1 -> return "Straight"
            2 -> return "Gay/Homosexual"
            3 -> return "Bi/Pansexual"
            else -> return "N/A"
        }
    }
    fun getRoleText(): String{
        when(role){
            1 -> return "Sub"
            2 -> return "Switch"
            3 -> return "Dom"
            else -> return "N/A"
        }
    }

    fun getSizeCmString(sizeCm: SizeCm): String{
        return "${sizeCm}"
    }
    fun getSizeUSString(sizeFeetAndInches: SizeFeetAndInches): String{
        if(sizeFeetAndInches.feet < 1){
            return "${sizeFeetAndInches.inches}"
        } else {
         return "${sizeFeetAndInches.feet}'${sizeFeetAndInches.inches}"
        }
    }

    fun getImagesString(): String{
        var returnString = ""
        for((i, image) in img.withIndex()){
            if(i != 0){
                returnString += "\n"
            }
            returnString += image.link
        }

        return returnString
    }

    fun getCharacterEmbed(): MessageEmbed{
        val embedFieldList: MutableList<MessageEmbed.Field> = mutableListOf()

        embedFieldList.add(MessageEmbed.Field("Name", name, true))
        embedFieldList.add(MessageEmbed.Field("Nickname", nickname, true))
        embedFieldList.add(MessageEmbed.Field("Age", "$age", true))
        embedFieldList.add(MessageEmbed.Field("Race", race, true))
        embedFieldList.add(MessageEmbed.Field("Gender", getGenderText(), true))
        embedFieldList.add(MessageEmbed.Field("Sexuality", getSexualityText(), true))
        embedFieldList.add(MessageEmbed.Field("Role", getRoleText(), true))
        embedFieldList.add(MessageEmbed.Field("Height (cm)", this.getSizeCmString(height), true))
        embedFieldList.add(MessageEmbed.Field("Height (feet and inch)", this.getSizeUSString(heightUS), true))
        embedFieldList.add(MessageEmbed.Field("Weight (kg)", "$weight", true))
        embedFieldList.add(MessageEmbed.Field("Weight (pound)", "$weightUS", true))
        embedFieldList.add(MessageEmbed.Field("Build", build, true))
        embedFieldList.add(MessageEmbed.Field("Hair Color", hairColor, true))
        embedFieldList.add(MessageEmbed.Field("Hair Style", hairStyle, true))
        embedFieldList.add(MessageEmbed.Field("Eye Color", eyeColor, true))
        embedFieldList.add(MessageEmbed.Field("Nationality", nationality, true))
        embedFieldList.add(MessageEmbed.Field("Tattoos", tattoos, true))
        embedFieldList.add(MessageEmbed.Field("Piercings", piercings, true))
        embedFieldList.add(MessageEmbed.Field("Body Markings, Scars", bodyMarkingsOrScars, true))
        embedFieldList.add(MessageEmbed.Field("Family", family, true))
        embedFieldList.add(MessageEmbed.Field("Clothes", clothes, true))
        embedFieldList.add(MessageEmbed.Field("Cup Size", cupSize, true))
        embedFieldList.add(MessageEmbed.Field("Nipples Sensitivity", nippleSensitivity, true))
        embedFieldList.add(MessageEmbed.Field("Cock Length (cm)", this.getSizeCmString(cockLength), true))
        embedFieldList.add(MessageEmbed.Field("Cock Length (inch)", this.getSizeUSString(cockLengthUS), true))
        embedFieldList.add(MessageEmbed.Field("Cock Thickness (cm)", this.getSizeCmString(cockThickness), true))
        embedFieldList.add(MessageEmbed.Field("Cock Thickness (inch)", this.getSizeUSString(cockThicknessUS), true))
        embedFieldList.add(MessageEmbed.Field("Sexual Weakness", sexualWeakness, true))
        embedFieldList.add(MessageEmbed.Field("Powers", powers, true))
        embedFieldList.add(MessageEmbed.Field("Kinks", kinksExtra, true))
        embedFieldList.add(MessageEmbed.Field("Limits", limitsExtra, true))
        embedFieldList.add(MessageEmbed.Field("Personality", personality, true))
        embedFieldList.add(MessageEmbed.Field("Hobbies", hobbies, true))
        embedFieldList.add(MessageEmbed.Field("Dislikes", dislikes, true))
        embedFieldList.add(MessageEmbed.Field("Likes", likes, true))
        embedFieldList.add(MessageEmbed.Field("Backstory", backstory, true))

        embedFieldList.add(MessageEmbed.Field("Images", getImagesString(), true))

        return convertRichMessage(fields = embedFieldList)
    }


}