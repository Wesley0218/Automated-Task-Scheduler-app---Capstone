package com.example.automatedtaskscheduler

class Sorted_Task {
    var id: Int = 0
    var TaskID: Int = 0
    var Taskname: String = ""
    var Urgency: String = ""
    var DueDate: String = ""
    var DueTime: String = ""
    var Category: String = ""
    var TaskRank: Int = 0
    var TaskHour: Int = 0
    var Priority: Int = 0
    var DateTime: String = ""
    var Sorted_ID: Int = 0


    constructor(
        TaskID: Int = 0,
        TaskName: String,
        Urgency: String,
        DueDate: String,
        DueTime: String,
        Category: String,
        TaskRank: Int,
        TaskHour: Int,
        Priority: Int,
        DateTime: String,
        Sorted_ID: Int = 0
    ) {
        this.TaskID = TaskID
        this.Taskname = TaskName
        this.Urgency = Urgency
        this.DueDate = DueDate
        this.DueTime = DueTime
        this.Category = Category
        this.TaskRank = TaskRank
        this.TaskHour = TaskHour
        this.Priority = Priority
        this.DateTime = DateTime
        this.Sorted_ID = Sorted_ID
    }
}