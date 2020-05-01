package com.vgdragon.dataclass

class CharacterClass {
    var msgId = ""

    var img: MutableList<CharacterImg> = mutableListOf()
    var link = ""

    var name = ""
    var nickname = ""
    var age = 0
    var Race = ""
    var gender = 0 // 1 = Woman; 2 = Man; 3 = Futa;
    var sexuality = 0 // 1 = Straight; 2 = Gay/Homosexual; 3 = Bi/Pansexual
    var role = 0 // 1 = sub; 2 = switch; 3 = dom
    var height = SizeCm(0)
    var heightUS = SizeFeetAndInches(0,0.0)
    var build = ""
    var hairColor = ""
    var hairStyle = ""
    var eyeColor = ""
    var nationality = ""
    var tattoos = ""
    var piercings = ""
    var bodyMarkingsOrScars = ""
    var family = ""
    var clothes = ""
    var cockLength = SizeCm(0)
    var cockLengthUS = SizeFeetAndInches(0,0.0)
    var cockThickness = SizeCm(0)
    var cockThicknessUS = SizeFeetAndInches(0,0.0)
    var sexualWeakness = "" //
    var kinks = KinkList()
    var kinksExtra = "" //
    var limitsExtra = "" //
    var powers = ""
    var personality = ""
    var hobbies = ""
    var dislikes = ""
    var likes = ""
    var backstory = ""





}