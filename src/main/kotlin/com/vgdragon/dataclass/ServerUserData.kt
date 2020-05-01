package com.vgdragon.dataclass

class ServerUserData (val userID: String){
    var onServer: Boolean = true

    val characters: MutableList<CharacterClass> = mutableListOf()




}