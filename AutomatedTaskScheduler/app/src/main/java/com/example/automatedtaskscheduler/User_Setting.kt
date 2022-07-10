package com.example.automatedtaskscheduler

class User_Setting {

    var id: Int = 0
    var schoolRank: Int = 0
    var familyRank: Int = 0
    var personalRank: Int = 0

    var schoolHOUR: String = ""
    var familyHOUR: String = ""
    var personalHOUR: String = ""

    constructor(schoolRank: Int, familyRank: Int, personalRank: Int, schoolHOUR: String, familyHOUR: String, personalHOUR: String){
        this.schoolRank = schoolRank
        this.familyRank = familyRank
        this.personalRank = personalRank

        this.schoolHOUR = schoolHOUR
        this.familyHOUR = familyHOUR
        this.personalHOUR = personalHOUR

    }
}