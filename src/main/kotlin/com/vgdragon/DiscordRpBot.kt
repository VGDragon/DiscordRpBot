package com.vgdragon

import net.dv8tion.jda.api.JDABuilder
import net.dv8tion.jda.api.entities.Activity


    fun main(args: Array<String>) {
        //botStart(args)
        val kinkListDecoder =
            kinkListDecoder("https://cdn.rawgit.com/Goctionni/KinkList/master/v1.0.2.html#OK6J2rDkSbSHIv3ea3KwIgd.3JRDf1+THwQGe5eF+@_JYPWJOsMZ78px+Sz7=OkYPH_U@5Et-31=tpWMaaaWVE=1")
        println()
    }

fun botStart (args: Array<String>){
    if (args.size < 1) {
        println("No Bot token found.")
        return
    }

    val jda = JDABuilder(args[0])

    jda.setActivity(Activity.of(Activity.ActivityType.WATCHING, "Nothing"))
    jda.addEventListeners(EventListener())
    val jdaBot = jda.build()
    jdaBot.awaitReady()
}


