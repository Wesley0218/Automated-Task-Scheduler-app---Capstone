package com.example.automatedtaskscheduler

class Task_Title{
    var id: Int = 0
    var Taskname: String = ""
    var Urgency: String = ""
    var DueDate: String = ""
    var DueTime: String = ""
    var Category: String = ""
    var TaskRank: Int = 0
    var TaskHour: Int = 0
    var Priority: Int = 0

    constructor(TaskName: String, Urgency: String, DueDate: String, DueTime: String, Category: String, TaskRank: Int, TaskHour: Int, Priority: Int){

        this.Taskname = TaskName
        this.Urgency = Urgency
        this.DueDate = DueDate
        this.DueTime = DueTime
        this.Category = Category
        this.TaskRank = TaskRank
        this.TaskHour = TaskHour
        this.Priority = Priority

    }
}