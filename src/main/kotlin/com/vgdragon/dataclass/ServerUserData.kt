package com.vgdragon.dataclass

class ServerUserData (val userID: String){
    var onServer: Boolean = true

    val characters: MutableMap<Int, Long> = mutableMapOf()





}
