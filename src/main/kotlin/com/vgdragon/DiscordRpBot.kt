package com.vgdragon

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity


    fun main(args: Array<String>) {

        botStart(args)

    }

fun botStart (args: Array<String>){
    if (args.size < 1) {
        println("No Bot token found.")
        return
    }

    val jda = JDABuilder(args[0])

    jda.setActivity(Activity.of(Activity.ActivityType.WATCHING, "rp!"))
    jda.addEventListeners(EventListener())
    val jdaBot = jda.build()
    jdaBot.awaitReady()
}


