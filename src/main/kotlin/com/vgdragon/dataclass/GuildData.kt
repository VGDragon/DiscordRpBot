package com.vgdragon.dataclass

class GuildData (val guildId: String){
    var isJoined = true
    val userMap: MutableMap<String, ServerUserData> = mutableMapOf()

    var characterWithLimits = false
    var characterModCheck = false
    var characterLimits: CharacterLimits = CharacterLimits()

    var charactersWaitingForModAccepeing: MutableList<Pair<String, Long>> = mutableListOf()

    var modChannel = ""

}