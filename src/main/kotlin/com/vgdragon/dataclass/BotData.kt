package com.vgdragon.dataclass

class BotData {
    val guildDataClass: MutableMap<String, GuildData> = mutableMapOf()

    val prefixMap: MutableMap<String, String> = mutableMapOf()

    val privatUserData: MutableMap<String, ServerUserData> = mutableMapOf()
}