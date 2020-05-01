package com.vgdragon.funftions

import com.vgdragon.dataclass.GuildData
import com.vgdragon.dataclass.ServerUserData
import net.dv8tion.jda.api.entities.Guild
import net.dv8tion.jda.api.entities.Member

class UserMapUpdate (val guildDataMap: MutableMap<String, GuildData>,
                     val botDataLock: Any){

    fun updateAll(guildList: List<Guild>){
        if(guildList.isNullOrEmpty())
            return

        for (guild in guildList){
            synchronized(botDataLock){
                var userMap = guildDataMap.get(guild.id)
                if(userMap == null) {
                    userMap = GuildData(guild.id)
                    guildDataMap.put(guild.id, userMap)
                }
                if(guild.members != null)
                    updateUserMap(userMap.userMap, guild.members)

            }
        }
    }

    fun updateUserMap(guildId: String, memberList: List<Member>) {
        if (memberList.isNullOrEmpty())
            return

        synchronized(botDataLock) {
            val userMap = guildDataMap.get(guildId)
            if (userMap != null)
                updateUserMap(userMap.userMap, memberList)
        }

    }
    private fun updateUserMap(userDataMap: MutableMap<String, ServerUserData>, memberList: List<Member>){
        for (member in memberList){
            if(!userDataMap.containsKey(member.id)){
                val serverUser = ServerUserData(member.id)
                userDataMap.put(member.id, serverUser)
            }
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

    fun updateOnUserJoin(guildId: String, userId: String){
        synchronized(botDataLock){
            val guildDataClass = guildDataMap.get(guildId) ?: return

            val serverUserData = guildDataClass.userMap.get(userId)
            if(serverUserData == null){
                guildDataClass.userMap.put(userId, ServerUserData(userId))
                return
            }
            serverUserData.onServer = true
        }
    }
    fun updateOnUserLeave(guildId: String, userId: String){
        synchronized(botDataLock){
            val guildData = guildDataMap.get(guildId) ?: return
            if(guildData.userMap.isNullOrEmpty())
                return
            val userDataClass = guildData.userMap.get(userId)?: return
            userDataClass.onServer = true

        }
    }


}