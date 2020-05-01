package com.vgdragon.dataclass

class GuildData (val guildId: String){
    var isJoined = true
    val userMap: MutableMap<String, ServerUserData> = mutableMapOf()

}