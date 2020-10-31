package com.vgdragon.dataclass

import com.vgdragon.convertRichMessage
import net.dv8tion.jda.api.entities.MessageEmbed
import java.math.BigDecimal
import java.math.RoundingMode

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
        return "${sizeCm.size}"
    }
    fun getSizeUSString(sizeFeetAndInches: SizeFeetAndInches): String{
        return "${sizeFeetAndInches.feet}'${BigDecimal(sizeFeetAndInches.inches).setScale(2, RoundingMode.HALF_UP)}"
    }

    fun getImagesString(): String{
        var returnString = ""
        for((i, image) in img.withIndex()){
            if(i != 0){
                returnString += "\n"
            }
            returnString += "${i + 1}: ${image.link}"
        }

        return returnString
    }



    fun getCharacterString(): String{
        var returnString = ""
        returnString = "Name: " + name + "\n" +
                "Nickname: " + nickname + "\n" +
                "Age: " + "$age" + "\n" +
                "Race: " + race + "\n" +
                "Gender: " + getGenderText() + "\n" +
                "Sexuality: " + getSexualityText() + "\n" +
                "Role: " + getRoleText() + "\n" +
                "Height (cm): " + this.getSizeCmString(height) + "\n" +
                "Height (feet and inch): " + this.getSizeUSString(heightUS) + "\n" +
                "Weight (kg): " + "$weight" + "\n" +
                "Weight (pound): " + "$weightUS" + "\n" +
                "Build: " + build + "\n" +
                "Hair Color: " + hairColor + "\n" +
                "Hair Style: " + hairStyle + "\n" +
                "Eye Color: " + eyeColor + "\n" +
                "Nationality: " + nationality + "\n" +
                "Tattoos: " + tattoos + "\n" +
                "Piercings: " + piercings + "\n" +
                "Body Markings, Scars: " + bodyMarkingsOrScars + "\n" +
                "Family: " + family + "\n" +
                "Clothes: " + clothes + "\n" +
                "Cup Size: " + cupSize + "\n" +
                "Nipples Sensitivity: " + nippleSensitivity + "\n" +
                "Cock Length (cm): " + this.getSizeCmString(cockLength) + "\n" +
                "Cock Length (inch): " + this.getSizeUSString(cockLengthUS) + "\n" +
                "Cock Thickness (cm): " + this.getSizeCmString(cockThickness) + "\n" +
                "Cock Thickness (inch): " + this.getSizeUSString(cockThicknessUS) + "\n" +
                "Sexual Weakness: " + sexualWeakness + "\n" +
                "Powers: " + powers + "\n" +
                "Kinks: " + kinksExtra + "\n" +
                "Limits: " + limitsExtra + "\n" +
                "Personality: " + personality + "\n" +
                "Hobbies: " + hobbies + "\n" +
                "Dislikes: " + dislikes + "\n" +
                "Likes: " + likes + "\n" +
                "Backstory: " + backstory + "\n\n" +
                "Images:\n" + getImagesString()
        return returnString
    }
    fun getCharacterEmbedOld(): MessageEmbed{
        val embedFieldList: MutableList<MessageEmbed.Field> = mutableListOf()
        embedFieldList.add(MessageEmbed.Field("Name", name, false))
        embedFieldList.add(MessageEmbed.Field("Nickname", nickname, false))
        embedFieldList.add(MessageEmbed.Field("Age", "$age", false))
        embedFieldList.add(MessageEmbed.Field("Race", race, false))
        embedFieldList.add(MessageEmbed.Field("Gender", getGenderText(), false))
        embedFieldList.add(MessageEmbed.Field("Sexuality", getSexualityText(), false))
        embedFieldList.add(MessageEmbed.Field("Role", getRoleText(), false))
        embedFieldList.add(MessageEmbed.Field("Height (cm)", this.getSizeCmString(height), false))
        embedFieldList.add(MessageEmbed.Field("Height (feet and inch)", this.getSizeUSString(heightUS), false))
        embedFieldList.add(MessageEmbed.Field("Weight (kg)", "$weight", false))
        embedFieldList.add(MessageEmbed.Field("Weight (pound)", "$weightUS", false))
        embedFieldList.add(MessageEmbed.Field("Build", build, false))
        embedFieldList.add(MessageEmbed.Field("Hair Color", hairColor, false))
        embedFieldList.add(MessageEmbed.Field("Hair Style", hairStyle, false))
        embedFieldList.add(MessageEmbed.Field("Eye Color", eyeColor, false))
        embedFieldList.add(MessageEmbed.Field("Nationality", nationality, false))
        embedFieldList.add(MessageEmbed.Field("Tattoos", tattoos, false))
        embedFieldList.add(MessageEmbed.Field("Piercings", piercings, false))
        embedFieldList.add(MessageEmbed.Field("Body Markings, Scars", bodyMarkingsOrScars, false))
        embedFieldList.add(MessageEmbed.Field("Family", family, false))
        embedFieldList.add(MessageEmbed.Field("Clothes", clothes, false))
        embedFieldList.add(MessageEmbed.Field("Cup Size", cupSize, false))
        embedFieldList.add(MessageEmbed.Field("Nipples Sensitivity", nippleSensitivity, false))
        embedFieldList.add(MessageEmbed.Field("Cock Length (cm)", this.getSizeCmString(cockLength), false))
        embedFieldList.add(MessageEmbed.Field("Cock Length (inch)", this.getSizeUSString(cockLengthUS), false))
        embedFieldList.add(MessageEmbed.Field("Cock Thickness (cm)", this.getSizeCmString(cockThickness), false))
        embedFieldList.add(MessageEmbed.Field("Cock Thickness (inch)", this.getSizeUSString(cockThicknessUS), false))
        embedFieldList.add(MessageEmbed.Field("Sexual Weakness", sexualWeakness, false))
        embedFieldList.add(MessageEmbed.Field("Powers", powers, false))
        embedFieldList.add(MessageEmbed.Field("Kinks", kinksExtra, false))
        embedFieldList.add(MessageEmbed.Field("Limits", limitsExtra, false))
        embedFieldList.add(MessageEmbed.Field("Personality", personality, false))
        embedFieldList.add(MessageEmbed.Field("Hobbies", hobbies, false))
        embedFieldList.add(MessageEmbed.Field("Dislikes", dislikes, false))
        embedFieldList.add(MessageEmbed.Field("Likes", likes, false))
        embedFieldList.add(MessageEmbed.Field("Backstory", backstory, false))
        embedFieldList.add(MessageEmbed.Field("Images", getImagesString(), false))
        return convertRichMessage(fields = embedFieldList)
    }

    fun serverLimitsComparing(serverLimits: CharacterLimits): List<String>{
        val returnList: MutableList<String> = mutableListOf()
        val neededChareacterInfo = serverLimits.neededChareacterInfo

        if(neededChareacterInfo.contains("name")){
            if(name.isBlank()){
                returnList.add("name")
            }
        }
        if(neededChareacterInfo.contains("nickname")){
            if(nickname.isBlank()){
                returnList.add("nickname")
            }
        }
        if(neededChareacterInfo.contains("race")){
            if(race.isBlank()){
                returnList.add("race")
            }
        }
        if(neededChareacterInfo.contains("gender")){
            if(gender <= 0){
                returnList.add("gender")
            }
        }
        if(neededChareacterInfo.contains("sexuality")){
            if(sexuality <= 0){
                returnList.add("sexuality")
            }
        }
        if(neededChareacterInfo.contains("role")){
            if(role <= 0){
                returnList.add("role")
            }
        }
        if(neededChareacterInfo.contains("height")){
            if(height.size <= 0){
                returnList.add("height")
            }
        }
        if(neededChareacterInfo.contains("weight")){
            if(height.size <= 0){
                returnList.add("weight")
            }
        }
        if(neededChareacterInfo.contains("build")){
            if(build.isBlank()){
                returnList.add("build")
            }
        }
        if(neededChareacterInfo.contains("hair_color")){
            if(hairColor.isBlank()){
                returnList.add("hair_color")
            }
        }
        if(neededChareacterInfo.contains("hair_style")){
            if(hairStyle.isBlank()){
                returnList.add("hair_style")
            }
        }
        if(neededChareacterInfo.contains("eye_color")){
            if(eyeColor.isBlank()){
                returnList.add("eye_color")
            }
        }
        if(neededChareacterInfo.contains("nationality")){
            if(nationality.isBlank()){
                returnList.add("nationality")
            }
        }
        if(neededChareacterInfo.contains("tattoos")){
            if(tattoos.isBlank()){
                returnList.add("tattoos")
            }
        }
        if(neededChareacterInfo.contains("piercings")){
            if(piercings.isBlank()){
                returnList.add("piercings")
            }
        }
        if(neededChareacterInfo.contains("markings")){
            if(bodyMarkingsOrScars.isBlank()){
                returnList.add("piercings")
            }
        }
        if(neededChareacterInfo.contains("family")){
            if(family.isBlank()){
                returnList.add("family")
            }
        }
        if(neededChareacterInfo.contains("clothes")){
            if(clothes.isBlank()){
                returnList.add("clothes")
            }
        }
        if(neededChareacterInfo.contains("cup_size")){
            if(cupSize.isBlank()){
                returnList.add("cup_size")
            }
        }
        if(neededChareacterInfo.contains("nipples_sensitivity")){
            if(nippleSensitivity.isBlank()){
                returnList.add("nipples_sensitivity")
            }
        }
        if(neededChareacterInfo.contains("cock_length")){
            if(cockLength.size <= 0){
                returnList.add("cock_length")
            }
        }
        if(neededChareacterInfo.contains("cock_thickness")){
            if(cockThickness.size <= 0){
                returnList.add("cock_thickness")
            }
        }
        if(neededChareacterInfo.contains("sexual_weakness")){
            if(sexualWeakness.isBlank()){
                returnList.add("sexual_weakness")
            }
        }
        if(neededChareacterInfo.contains("kinks")){
            if(!kinks.isAdded){
                returnList.add("kinks")
            }
        }
        if(neededChareacterInfo.contains("limits")){
            if(limitsExtra.isBlank()){
                returnList.add("limits")
            }
        }
        if(neededChareacterInfo.contains("powers")){
            if(powers.isBlank()){
                returnList.add("powers")
            }
        }
        if(neededChareacterInfo.contains("personality")){
            if(personality.isBlank()){
                returnList.add("personality")
            }
        }
        if(neededChareacterInfo.contains("hobbies")){
            if(hobbies.isBlank()){
                returnList.add("hobbies")
            }
        }
        if(neededChareacterInfo.contains("dislikes")){
            if(dislikes.isBlank()){
                returnList.add("dislikes")
            }
        }
        if(neededChareacterInfo.contains("likes")){
            if(likes.isBlank()){
                returnList.add("likes")
            }
        }
        if(neededChareacterInfo.contains("backstory")){
            if(backstory.isBlank()){
                returnList.add("backstory")
            }
        }
        val neddedInfoSize = returnList.size

        if(!returnList.contains("age")){
            if(age < serverLimits.minimumAge || age > serverLimits.maximumAge) {
                if(serverLimits.minimumAge > 0){
                    returnList.add("The minimum Age of your Character has to be ${serverLimits.minimumAge} for this server.")
                } else if(serverLimits.maximumAge < Int.MAX_VALUE){
                    returnList.add("The maximum Age of your Character has to be ${serverLimits.maximumAge} for this server.")
                } else {
                    returnList.add("Your Character Age has to be between ${serverLimits.minimumAge} and ${serverLimits.maximumAge} for this server.")
                }

            }
        }

        if(!returnList.contains("height")){

            if(height.size < serverLimits.minimumHeight.size || age > serverLimits.maximumHeight.size) {

                if(serverLimits.minimumHeight.size > 0){
                    returnList.add("The minimum Height of your Character has to be ${serverLimits.minimumHeight}cm (${serverLimits.minimumHeightUS.feet}'${serverLimits.minimumHeightUS.inches}) for this server.")
                } else if(serverLimits.maximumHeight.size < Int.MAX_VALUE){
                    returnList.add("The maximum Height of your Character has to be ${serverLimits.maximumHeight}cm (${serverLimits.maximumHeightUS.feet}'${serverLimits.maximumHeightUS.inches}) for this server.")
                } else {
                    returnList.add("Your Character Height has to be between ${serverLimits.minimumHeight}cm (${serverLimits.minimumHeightUS.feet}'${serverLimits.minimumHeightUS.inches})" +
                            " and ${serverLimits.maximumHeight}cm (${serverLimits.maximumHeightUS.feet}'${serverLimits.maximumHeightUS.inches}) for this server.")
                }

            }
        }
        if(returnList.isNotEmpty()){
            returnList.add(neddedInfoSize, "")
            returnList.add(0, "")
            returnList.add(0, "Your Character need this Infos for this server:")
        }

        return returnList
    }



}