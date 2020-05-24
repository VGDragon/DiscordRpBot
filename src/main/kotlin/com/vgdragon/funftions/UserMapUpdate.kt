package com.vgdragon.funftions

import com.vgdragon.dataclass.BotData
import com.vgdragon.dataclass.GuildData
import com.vgdragon.dataclass.PrivateUserData
import com.vgdragon.dataclass.ServerUserData
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member

class UserMapUpdate (val botData: BotData){

    fun updateAll(guildList: List<Guild>){
        if(guildList.isNullOrEmpty())
            return
        val guildDataClass = botData.guildDataClass
        for (guild in guildList){

                var userMap = guildDataClass.get(guild.id)
                if(userMap == null) {
                    userMap = GuildData(guild.id)
                    guildDataClass.put(guild.id, userMap)
                }
                if(guild.members != null)
                    updateUserMap(userMap.userMap, guild.members)

            }

    }

    fun updateUserMap(guildId: String, memberList: List<Member>) {
        if (memberList.isNullOrEmpty())
            return

        val userMap = botData.guildDataClass.get(guildId)
        if (userMap != null)
            updateUserMap(userMap.userMap, memberList)


    }
    private fun updateUserMap(userDataMap: MutableMap<String, ServerUserData>, memberList: List<Member>){
        for (member in memberList){
            if(!userDataMap.containsKey(member.id)){
                userDataMap.put(member.id, ServerUserData(member.id))
            }

            privateUserUpdate(member.id)


        }
        for((id, user) in userDataMap.entries){
            var isOnServer = false
            for (member in memberList){
                if(member.id.equals(id)){
                    isOnServer = true
                    break
                }
            }
            user.onServer = isOnServer
        }
    }

    fun privateUserUpdate(meberId: String){
        val privatUserData = botData.privatUserData
        if (!privatUserData.containsKey(meberId))
            privatUserData.put(meberId, PrivateUserData(meberId))
    }

    fun updateOnUserJoin(guildId: String, userId: String) {
        val guildDataClassMap = botData.guildDataClass
        val guildDataClass = guildDataClassMap.get(guildId) ?: return

        val serverUserData = guildDataClass.userMap.get(userId)
        if (serverUserData == null) {
            guildDataClass.userMap.put(userId, ServerUserData(userId))
            privateUserUpdate(userId)
            return
        }
        serverUserData.onServer = true

    }
    fun updateOnUserLeave(guildId: String, userId: String){
        val guildDataClassMap = botData.guildDataClass
        val guildData = guildDataClassMap.get(guildId) ?: return
            if(guildData.userMap.isNullOrEmpty())
                return
            val userDataClass = guildData.userMap.get(userId)?: return
            userDataClass.onServer = true


    }


}