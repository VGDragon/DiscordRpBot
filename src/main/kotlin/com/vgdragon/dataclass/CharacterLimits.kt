package com.vgdragon.dataclass

import com.vgdragon.convertRichMessage
import net.dv8tion.jda.api.entities.MessageEmbed

class CharacterLimits {

    var minimumAge = 0
    var maximumAge = Int.MAX_VALUE

    var minimumHeight = SizeCm(0)
    var minimumHeightUS = SizeFeetAndInches(0,0.0)
    var maximumHeight = SizeCm(Int.MAX_VALUE)
    var maximumHeightUS = SizeFeetAndInches(0,0.0)

    var neededChareacterInfo: MutableList<String> = mutableListOf()

    fun serverLimitsThatCanBeSetted(): MessageEmbed {
        val embedFieldList: MutableList<MessageEmbed.Field> = mutableListOf()
        embedFieldList.add(
            MessageEmbed.Field(
                "Limits", "You have to activate the Limit function before the Bot use it.\n" +
                        "Example: characterLimits=yes\n\n", false
            )
        )
        embedFieldList.add(
            MessageEmbed.Field(
                "Mod Check", "If you activate that function, a mod need to confirm the character before it can added to the server.\n" +
                        "Example: modCheck=yes\n\n", false
            )
        )

        embedFieldList.add(
            MessageEmbed.Field(
                "Age", "You can only user numbers for that. If you set 0, it is not needed.\n" +
                        "Example: minimumAge=18\n\n" +
                        "Parameters: minimumAge maximumAge", false
            )
        )
        embedFieldList.add(
            MessageEmbed.Field(
                "Height", "You can use cm, or feet and inches for that. If you set 0, it is not needed.\n" +
                        "Example: minimumHeight=10\n" +
                        "Example 2: maximumHeight=9'10.5\n\n" +
                        "Parameters: minimumHeight maximumHeight", false
            )
        )

        embedFieldList.add(
            MessageEmbed.Field(
                "Others", "You can only activate or deactivate this Parameters\n" +
                        "Example: name=yes\n" +
                        "Example 2: name=no\n\n" +
                        "Parameters: " +
                        "name " +
                        "nickname " +
                        "race " +
                        "gender " +
                        "sexuality " +
                        "role " +
                        "height " +
                        "weight " +
                        "build " +
                        "hair_color " +
                        "hair_style " +
                        "eye_color " +
                        "nationality " +
                        "tattoos " +
                        "piercings " +
                        "markings " +
                        "piercings " +
                        "family " +
                        "clothes " +
                        "cup_size " +
                        "nipples_sensitivity " +
                        "cock_length " +
                        "cock_thickness " +
                        "sexual_weakness " +
                        "kinks " +
                        "limits " +
                        "powers " +
                        "personality " +
                        "hobbies " +
                        "dislikes " +
                        "likes " +
                        "backstory"
                , false
            )
        )



        return convertRichMessage(fields = embedFieldList)
    }

}