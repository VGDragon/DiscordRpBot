package com.vgdragon.dataclass

class BotData {
    val guildDataClass: MutableMap<String, GuildData> = mutableMapOf()
    val prefixMap: MutableMap<String, String> = mutableMapOf()

    val privatUserData: MutableMap<String, PrivateUserData> = mutableMapOf()

    val characters: MutableMap<Long, CharacterClass> = mutableMapOf()

    var nextCharId = 1L

}