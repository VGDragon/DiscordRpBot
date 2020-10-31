package com.vgdragon.dataclass

import com.vgdragon.convertRichMessage
import net.dv8tion.jda.api.entities.MessageEmbed

class KinkList{
    var link = "https://cdn.rawgit.com/Goctionni/KinkList/master/v1.0.2.html#aaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaaa"
    var picLink = ""
    var isAdded = false

    // 0 = Not Entered
    // 1 = Favorite
    // 2 = Like
    // 3 = Okay
    // 4 = Maybe
    // 5 = No
    val kinkMap: MutableMap<String, Int> = mutableMapOf()




}