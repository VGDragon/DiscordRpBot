package com.vgdragon

import com.vgdragon.dataclass.KinkList
import com.vgdragon.dataclass.SizeCm
import com.vgdragon.dataclass.SizeFeetAndInches
import net.dv8tion.jda.api.entities.EmbedType
import net.dv8tion.jda.api.entities.MessageEmbed
import java.net.URLDecoder
import java.time.OffsetDateTime

fun convertCmInFeetAndInches(sizeCm: SizeCm): SizeFeetAndInches{
    var feet: Double = sizeCm.size / 30.48
    val rest = "$feet".split(".").get(1)
    val inches = "0.$rest".toDouble() * 12

    return SizeFeetAndInches("$feet".split(".")[0].toInt(), inches)
}
fun convertFeetAndInchesInCm(sizeFeetAndInches: SizeFeetAndInches): SizeCm{
    var cm = sizeFeetAndInches.feet * 30.48
    cm += sizeFeetAndInches.inches * 2.54
    return SizeCm(cm.toInt())
}
fun convertLbsToKg(lbs: Int): Int{
    return (lbs / 2.205).toInt()
}
fun convertKgToLbs(kg: Int): Int{
return (kg * 2.205).toInt()
}

fun kinkListDecoder(kinkListUrl: String, para: String = ""): KinkList{
    val url = URLDecoder.decode(kinkListUrl, "UTF-8")
    val code = if (url.contains("#")) {
        url.split("#")[1].replace(" ", "+")
    } else {
        url.replace(" ", "+")
    }
    val codeVariables = "abcdefghijklmnopqrstuvwxyzABCDEFGHIJKLMNOPQRSTUVWXYZ0123456789-_.=+*^!@".split("").toMutableList()

    val codeMap: MutableMap<String, Int> = mutableMapOf()
    codeVariables.remove("")
    codeVariables.remove("")
    for((i, s) in codeVariables.withIndex()){
        codeMap.put(s, i)
    }
    val codeIntList: MutableList<Int> = mutableListOf()
    var restCode = code
    while (restCode.length > 0){
        var i = 7
        if (restCode.length < 8)
            i = restCode.length - 2
        val tempCode = restCode.substring(0, i + 1)

        var codeNumber = 0L

        while (i >= 0){
            val c = tempCode[i].toString()
            if(codeMap.get(c) == null){
                println()
            }
            codeNumber += Math.pow(codeVariables.size.toDouble() ,(tempCode.length - i - 1).toDouble()).toLong() * codeMap.get(c)!!

            i--
        }
        val bin5Decode = bin5Decode(codeNumber)
        var ff = bin5Decode.size - 1
        while (ff >=0){
            codeIntList.add(bin5Decode.get(ff))
            ff--
        }
        //var ff = 0
        //while (ff < bin5Decode.size){
        //    codeIntList.add(bin5Decode.get(ff))
        //    ff++
        //}

        if(restCode.length < 8){
            break
        }
        restCode = restCode.substring(i + 9, restCode.length)
    }

    val kinkListClass = codeIntListToKinkList(codeIntList, para)
    kinkListClass.link = kinkListUrl

    return kinkListClass
}
fun defaultParaString(): String{
    return "#Bodies\n" +
            "(General)\n" +
            "* Skinny\n" +
            "* Chubby\n" +
            "* Small breasts\n" +
            "* Large breasts\n" +
            "* Small cocks\n" +
            "* Large cocks\n" +
            "\n" +
            "#Clothing\n" +
            "(Self, Partner)\n" +
            "* Clothed sex\n" +
            "* Lingerie\n" +
            "* Stockings\n" +
            "* Heels\n" +
            "* Leather\n" +
            "* Latex\n" +
            "* Uniform / costume\n" +
            "* Cross-dressing\n" +
            "\n" +
            "#Groupings\n" +
            "(General)\n" +
            "* You and 1 male\n" +
            "* You and 1 female\n" +
            "* You and MtF trans\n" +
            "* You and FtM trans\n" +
            "* You and 1 male, 1 female\n" +
            "* You and 2 males\n" +
            "* You and 2 females\n" +
            "* Orgy\n" +
            "\n" +
            "#General\n" +
            "(Giving, Receiving)\n" +
            "* Romance / Affection\n" +
            "* Handjob / fingering\n" +
            "* Blowjob\n" +
            "* Deep throat\n" +
            "* Swallowing\n" +
            "* Facials\n" +
            "* Cunnilingus\n" +
            "* Face-sitting\n" +
            "* Edging\n" +
            "* Teasing\n" +
            "* JOI, SI\n" +
            "\n" +
            "#Ass play\n" +
            "(Giving, Receiving)\n" +
            "* Anal toys\n" +
            "* Anal sex, pegging\n" +
            "* Rimming\n" +
            "* Double penetration\n" +
            "* Anal fisting\n" +
            "\n" +
            "#Restrictive\n" +
            "(Self, Partner)\n" +
            "* Gag\n" +
            "* Collar\n" +
            "* Leash\n" +
            "* Chastity\n" +
            "* Bondage (Light)\n" +
            "* Bondage (Heavy)\n" +
            "* Encasement\n" +
            "\n" +
            "#Toys\n" +
            "(Self, Partner)\n" +
            "* Dildos\n" +
            "* Plugs\n" +
            "* Vibrators\n" +
            "* Sounding\n" +
            "\n" +
            "#Domination\n" +
            "(Dominant, Submissive)\n" +
            "* Dominant / Submissive\n" +
            "* Domestic servitude\n" +
            "* Slavery\n" +
            "* Pet play\n" +
            "* DD/lg, MD/lb\n" +
            "* Discipline\n" +
            "* Begging\n" +
            "* Forced orgasm\n" +
            "* Orgasm control\n" +
            "* Orgasm denial\n" +
            "* Power exchange\n" +
            "\n" +
            "#No consent\n" +
            "(Aggressor, Target)\n" +
            "* Non-con / rape\n" +
            "* Blackmail / coercion\n" +
            "* Kidnapping\n" +
            "* Drugs / alcohol\n" +
            "* Sleep play\n" +
            "\n" +
            "#Taboo\n" +
            "(General)\n" +
            "* Incest\n" +
            "* Ageplay\n" +
            "* Interracial / Raceplay\n" +
            "* Bestiality\n" +
            "* Necrophilia\n" +
            "* Cheating\n" +
            "* Exhibitionism\n" +
            "* Voyeurism\n" +
            "\n" +
            "#Surrealism\n" +
            "(Self, Partner)\n" +
            "* Futanari\n" +
            "* Furry\n" +
            "* Vore\n" +
            "* Transformation\n" +
            "* Tentacles\n" +
            "* Monster or Alien\n" +
            "\n" +
            "#Fluids\n" +
            "(General)\n" +
            "* Blood\n" +
            "* Watersports\n" +
            "* Scat\n" +
            "* Lactation\n" +
            "* Diapers\n" +
            "* Cum play\n" +
            "\n" +
            "#Degradation\n" +
            "(Giving, Receiving)\n" +
            "* Glory hole\n" +
            "* Name calling\n" +
            "* Humiliation\n" +
            "\n" +
            "#Touch & Stimulation\n" +
            "(Actor, Subject)\n" +
            "* Cock/Pussy worship\n" +
            "* Ass worship\n" +
            "* Foot play\n" +
            "* Tickling\n" +
            "* Sensation play\n" +
            "* Electro stimulation\n" +
            "\n" +
            "#Misc. Fetish\n" +
            "(Giving, Receiving)\n" +
            "* Fisting\n" +
            "* Gangbang\n" +
            "* Breath play\n" +
            "* Impregnation\n" +
            "* Pregnancy\n" +
            "* Feminization\n" +
            "* Cuckold / Cuckquean\n" +
            "\n" +
            "#Pain\n" +
            "(Giving, Receiving)\n" +
            "* Light pain\n" +
            "* Heavy pain\n" +
            "* Nipple clamps\n" +
            "* Clothes pins\n" +
            "* Caning\n" +
            "* Flogging\n" +
            "* Beating\n" +
            "* Spanking\n" +
            "* Cock/Pussy slapping\n" +
            "* Cock/Pussy torture\n" +
            "* Hot Wax\n" +
            "* Scratching\n" +
            "* Biting\n" +
            "* Cutting"
}
fun bin5Decode(long: Long):MutableList<Int>{
    var restLong = long
    val returnList: MutableList<Int> = mutableListOf()
    var i = 19
    while (i > 0){
        var ii = 5
        while (true){


            if(restLong - ((Math.pow(6.0, i.toDouble() - 1)) * (ii + 1)) >= 0){
                returnList.add(ii + 1)
                restLong -= ((Math.pow(6.0, i.toDouble() - 1)) * (ii + 1)).toLong()
                break
            }


            if(ii == 0){
                returnList.add(0)
                break
            }
            ii--
        }
        i--
    }


    return returnList
}
fun codeIntListToKinkList(codeIntList: MutableList<Int>, para: String): KinkList{
    val kinkList = KinkList()

    val split = if(para.isNullOrBlank()){
        defaultParaString().split("\n")
    } else {
        para.split("\n")
    }
    var category = ""
    var rowList: MutableList<String> = mutableListOf()

    for(s in split){

        if(s.startsWith("*")){
            val trim = s.substring(1).trim()
            for(r in rowList) {
                kinkList.kinkMap.put("${category.trim()}\n$r\n$trim", codeIntList.get(kinkList.kinkMap.keys.size))
            }

            continue
        }
        if(s.isNullOrBlank())
            continue

        if(s.startsWith("#")) {
            category = s.substring(1)
            continue
        }
        if(s.startsWith("(")){
            val substring = s.substring(1, s.length - 1)
            val tempSplit = substring.split(",")
            rowList = mutableListOf()
            rowList.addAll(tempSplit)
            continue
        }

    }


    return kinkList
}


fun convertRichMessage(url: String = "",
                       title: String = "",
                       description: String = "",
                       embedType: EmbedType = EmbedType.RICH,
                       timestamp: OffsetDateTime = OffsetDateTime.now(),
                       color: Int = 0xff1808,
                       thumbnail: MessageEmbed.Thumbnail = convertMessageThunbnail(),
                       siteProvider: MessageEmbed.Provider = convertMessageProvider(),
                       author: MessageEmbed.AuthorInfo = convertMessageAutorInfo(),
                       videoInfo: MessageEmbed.VideoInfo = convertMessageVideoInfo(),
                       footer: MessageEmbed.Footer = convertMessageFooter(),
                       image: MessageEmbed.ImageInfo = convertMessageImageInfo(),
                       fields: List<MessageEmbed.Field> = listOf()
                    ): MessageEmbed {
    return MessageEmbed(url,title, description, embedType, timestamp, color, thumbnail, siteProvider, author, videoInfo, footer, image, fields)
}
fun convertMessageThunbnail(url: String = "", proxyUrl: String = "", width: Int = 0, height: Int = 0): MessageEmbed.Thumbnail {
    return MessageEmbed.Thumbnail(url, proxyUrl, width, height)
}
fun convertMessageProvider(name: String = "", url: String = ""): MessageEmbed.Provider {
    return MessageEmbed.Provider(name, url)
}
fun convertMessageAutorInfo(name: String = "", url: String = "", iconUrl: String = "", proxyIconUrl: String = ""): MessageEmbed.AuthorInfo {
return MessageEmbed.AuthorInfo(name, url, iconUrl, proxyIconUrl)
}
fun convertMessageVideoInfo(url: String = "", width: Int = 0, height: Int = 0): MessageEmbed.VideoInfo {
    return MessageEmbed.VideoInfo(url, width, height)
}
fun convertMessageFooter(text: String = "",iconUrl: String = "", proxyIconUrl: String = ""): MessageEmbed.Footer {
    return MessageEmbed.Footer(text, iconUrl, proxyIconUrl)
}
fun convertMessageImageInfo(url: String = "", proxyUrl: String = "", width: Int = 0, height: Int = 0): MessageEmbed.ImageInfo {
    return MessageEmbed.ImageInfo(url, proxyUrl, width, height)

}


