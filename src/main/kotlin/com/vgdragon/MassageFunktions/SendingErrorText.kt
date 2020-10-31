package com.vgdragon.MassageFunktions

import net.dv8tion.jda.api.events.message.MessageReceivedEvent

class SendingErrorText {
    fun noCharacterOwnCharaters(event: MessageReceivedEvent){
        event.channel.sendMessage("You don't have a character.").submit()
    }

    fun noCharacterFromOtherUser(event: MessageReceivedEvent){
        event.channel.sendMessage("This user don't have a character.").submit()
    }


    fun noCharacterInSlotOwnCharaters(event: MessageReceivedEvent){
        event.channel.sendMessage("You don't have a Character with that ID.").submit()
    }

    fun noCharacterInSlotFromOtherUser(event: MessageReceivedEvent){
        event.channel.sendMessage("This user doesn't have a Character with that ID.").submit()
    }

    fun thisCharacterIsOnServer(event: MessageReceivedEvent){
        event.channel.sendMessage("This Character is already on this server.").submit()
    }

    fun thisCharacterIsNotOnServer(event: MessageReceivedEvent){
        event.channel.sendMessage("This Character is not on this server.").submit()
    }

    fun noCharacterOnServerOwn(event: MessageReceivedEvent){
        event.channel.sendMessage("You don't have a Character on this server.").submit()
    }

    fun noCharaterOnServerOtherUser(event: MessageReceivedEvent){
        event.channel.sendMessage("This user don't have a Character on this server.").submit()
    }

    fun noPicture(event: MessageReceivedEvent){
        event.channel.sendMessage("You don't have a picture on that Character.").submit()
    }

}