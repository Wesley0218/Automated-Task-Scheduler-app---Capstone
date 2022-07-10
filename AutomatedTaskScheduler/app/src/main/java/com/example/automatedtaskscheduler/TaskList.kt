package com.example.automatedtaskscheduler

data class TaskList(
    var id: Int = 0,
    var TaskName: String = "",
    var DueDate: String = "",
    var DueTime: String = "",
    var TaskRank: Int = 0,
    var TaskHour: Int = 0,
    var Priority: Int = 0,
    var Urgency: String = "",
    var Category: String = ""

)