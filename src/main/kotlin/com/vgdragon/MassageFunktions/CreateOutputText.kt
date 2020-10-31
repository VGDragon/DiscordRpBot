package com.vgdragon.MassageFunktions

import com.vgdragon.dataclass.CharacterClass
import net.dv8tion.jda.api.entities.MessageEmbed

class CreateOutputText {


    fun characterList(fieldList: MutableList<MessageEmbed.Field>, charClass: CharacterClass, fieldname: String, textAfterTheCharacter: String = ""){
        fieldList.add(
            MessageEmbed.Field(
                fieldname,
                "Name: ${charClass.name}\n" +
                        "Race: ${charClass.race}\n" +
                        "Gender: ${charClass.getGenderText()}\n" +
                        "Sexuality: ${charClass.getSexualityText()}\n" +
                        "Role: ${charClass.getRoleText()}" +
                        textAfterTheCharacter,
                false))
    }

}