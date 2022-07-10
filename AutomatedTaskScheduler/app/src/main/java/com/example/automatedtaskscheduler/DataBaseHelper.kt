package com.example.automatedtaskscheduler

import android.annotation.SuppressLint
import android.content.ContentValues
import android.content.Context
import android.database.Cursor
import android.database.sqlite.SQLiteDatabase
import android.database.sqlite.SQLiteOpenHelper
import androidx.core.database.getIntOrNull
import java.lang.Exception

val DATABASENAME = "EROS"
val TABLENAME = "User_Settings"
val COL_ID = "id"
val COL_SCHOOL_RANK = "schoolRank"
val COL_FAMILY_RANK = "familyRank"
val COL_PERSONAL_RANK = "personalRank"
val COL_SCHOOL_HOUR = "schoolHOUR"
val COL_FAMILY_HOUR = "familyHOUR"
val COL_PERSONAL_HOUR = "personalHOUR"

val TASKTABLE = "Task_Table"
val ID = "id"
val TASKNAME = "Task_Name"
val URGENCY = "Urgency"
val DUE_DATE = "Due_Date"
val DUE_TIME = "Due_Time"
val CATEGORY = "Category"
val TASKRANK = "Task_Rank"
val TASKHOUR = "Task_Hour"
val PRIORITY = "Priority"

val SORTEDTASK = "Sorted_Task"
val DATETIME = "Date_Time"
val TASK_ID = "Task_ID"
val SORTED_ID = "Sorted_ID"
val STATUS = "Status"
val STIME = "Start_Time"
val ETIME = "End_Time"

val DILIGENCY = "Diligency"



class DataBaseHandler(var context: Context) : SQLiteOpenHelper(context, DATABASENAME, null,
    1){

    override fun onCreate(db: SQLiteDatabase?) {
        val createTable =
            "CREATE TABLE $TABLENAME ($COL_ID INTEGER PRIMARY KEY AUTOINCREMENT,$COL_SCHOOL_RANK INTEGER,$COL_FAMILY_RANK INTEGER,$COL_PERSONAL_RANK INTEGER,$COL_SCHOOL_HOUR VARCHAR(256),$COL_FAMILY_HOUR VARCHAR(256),$COL_PERSONAL_HOUR VARCHAR(256),$STATUS INTEGER, $DILIGENCY INTEGER DEFAULT 0)"
        db?.execSQL(createTable)

        val createTableTask =
        "CREATE TABLE $TASKTABLE ($ID INTEGER PRIMARY KEY AUTOINCREMENT, $TASKNAME VARCHAR(256), $URGENCY VARCHAR(256),$DUE_DATE VARCHAR(256),$DUE_TIME VARCHAR(256),$CATEGORY VARCHAR(256),$TASKRANK INTEGER, $TASKHOUR INTEGER, $PRIORITY INTEGER, $STATUS INTEGER DEFAULT 0)"
        db?.execSQL(createTableTask)

        val createSortedTask =
            "CREATE TABLE $SORTEDTASK ($ID INTEGER PRIMARY KEY AUTOINCREMENT,$TASK_ID INTEGER, $TASKNAME VARCHAR(256), $URGENCY VARCHAR(256),$DUE_DATE VARCHAR(256),$DUE_TIME VARCHAR(256),$CATEGORY VARCHAR(256),$TASKRANK INTEGER, $TASKHOUR INTEGER, $PRIORITY INTEGER , $DATETIME VARCHAR(256), $SORTED_ID INTEGER, $STIME VARCHAR(256), $STATUS INTEGER, $ETIME VARCHAR(256))"
        db?.execSQL(createSortedTask)

    }

    override fun onUpgrade(db: SQLiteDatabase?, oldVersion: Int, newVersion: Int) {
        TODO("Not yet implemented")
    }


    fun insertData(user_setting: User_Setting) {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        contentValues.put(COL_SCHOOL_RANK, user_setting.schoolRank)
        contentValues.put(COL_FAMILY_RANK, user_setting.familyRank)
        contentValues.put(COL_PERSONAL_RANK, user_setting.personalRank)

        contentValues.put(COL_SCHOOL_HOUR, user_setting.schoolHOUR)
        contentValues.put(COL_FAMILY_HOUR, user_setting.familyHOUR)
        contentValues.put(COL_PERSONAL_HOUR, user_setting.personalHOUR)

        val result = database.insert(TABLENAME, null, contentValues)
        if(result == (0).toLong()){
            println("FAILED")
        }
        else{
            println("SUCCESS")
            database?.close()
        }
    }
    fun insertTask(taskDetail: Task_Title){
        val database = this.writableDatabase
        val contentValues = ContentValues()

        println("Success Test: database")

        contentValues.put(TASKNAME, taskDetail.Taskname)
        contentValues.put(URGENCY, taskDetail.Urgency)
        contentValues.put(DUE_DATE, taskDetail.DueDate)
        contentValues.put(DUE_TIME, taskDetail.DueTime)
        contentValues.put(CATEGORY, taskDetail.Category)
        contentValues.put(TASKRANK, taskDetail.TaskRank)
        contentValues.put(TASKHOUR, taskDetail.TaskHour)
        contentValues.put(PRIORITY, taskDetail.Priority)


        println(taskDetail.Taskname + taskDetail.Urgency + taskDetail.DueDate + taskDetail.DueTime + taskDetail.Category)

        val result = database.insert("Task_Table", null, contentValues)
        if(result == (0).toLong()){
            println("FAILED")
        }
        else{
            println("SUCCESS")

        }

    }

    fun insertSortedTask(sortedTask: Sorted_Task){
        val database = this.writableDatabase
        val contentValues = ContentValues()

        println("Success Test: ")

        contentValues.put(TASK_ID, sortedTask.TaskID)
        contentValues.put(TASKNAME, sortedTask.Taskname)
        contentValues.put(URGENCY, sortedTask.Urgency)
        contentValues.put(DUE_DATE, sortedTask.DueDate)
        contentValues.put(DUE_TIME, sortedTask.DueTime)
        contentValues.put(CATEGORY, sortedTask.Category)
        contentValues.put(TASKRANK, sortedTask.TaskRank)
        contentValues.put(TASKHOUR, sortedTask.TaskHour)
        contentValues.put(PRIORITY, sortedTask.Priority)
        contentValues.put(DATETIME, sortedTask.DateTime)
        contentValues.put(SORTED_ID, sortedTask.Sorted_ID)

        val result = database.insertWithOnConflict(SORTEDTASK, null, contentValues, SQLiteDatabase.CONFLICT_REPLACE)
        if(result == (0).toLong()){
            println("FAILED")
        }
        else{
            println("SUCCESS")

        }
    }

    @SuppressLint("Range")
    fun getTask(): ArrayList<TaskList>{
        val taskList: ArrayList<TaskList> = ArrayList()

        val selectQuery = "SELECT * FROM $TASKTABLE WHERE $STATUS != 1"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: Exception){
            db.execSQL(selectQuery)
            e.printStackTrace()
            return ArrayList()
        }

        var id: Int
        var TaskName: String
        var DueDate: String
        var DueTime: String
        var TaskRank: Int
        var TaskHour: Int
        var Priority: Int
        var Urgency: String
        var Category: String


        if(cursor.moveToNext()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("id"))
                TaskName = cursor.getString(cursor.getColumnIndex("Task_Name"))
                DueDate = cursor.getString(cursor.getColumnIndex("Due_Date"))
                DueTime = cursor.getString(cursor.getColumnIndex("Due_Time"))
                TaskRank = cursor.getInt(cursor.getColumnIndex("Task_Rank"))
                TaskHour = cursor.getInt(cursor.getColumnIndex("Task_Hour"))
                Priority = cursor.getInt(cursor.getColumnIndex("Priority"))
                Urgency = cursor.getString(cursor.getColumnIndex("Urgency"))
                Category = cursor.getString(cursor.getColumnIndex("Category"))


                val task = TaskList(id = id, TaskName = TaskName, DueDate = DueDate, DueTime = DueTime, TaskRank = TaskRank, TaskHour = TaskHour, Priority = Priority, Urgency = Urgency, Category = Category)
                taskList.add(task)
            }while (cursor.moveToNext())
        }

        return taskList
        println("Test Success")
    }

    @SuppressLint("Range")
    fun getSortedTask(): ArrayList<SortedTaskList>{
        val sortedTaskList: ArrayList<SortedTaskList> = ArrayList()

        val selectQuery = "SELECT * FROM $SORTEDTASK WHERE $STATUS != 1 ORDER BY $SORTED_ID , $DUE_DATE, $DUE_TIME , $TASKRANK"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: Exception){
            db.execSQL(selectQuery)
            e.printStackTrace()
            return ArrayList()
        }

        var id: Int
        var Task_ID: Int
        var TaskName: String
        var DueDate: String
        var DueTime: String
        var TaskRank: Int
        var TaskHour: Int
        var Priority: Int
        var Urgency: String
        var Category: String
        var Date_Time: String
        var Sorted_ID: Int
        var Start_Time: String
        var End_Time: String


        if(cursor.moveToNext()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("id"))
                Task_ID = cursor.getInt(cursor.getColumnIndex("Task_ID"))
                TaskName = cursor.getString(cursor.getColumnIndex("Task_Name"))
                DueDate = cursor.getString(cursor.getColumnIndex("Due_Date"))
                DueTime = cursor.getString(cursor.getColumnIndex("Due_Time"))
                TaskRank = cursor.getInt(cursor.getColumnIndex("Task_Rank"))
                TaskHour = cursor.getInt(cursor.getColumnIndex("Task_Hour"))
                Priority = cursor.getInt(cursor.getColumnIndex("Priority"))
                Urgency = cursor.getString(cursor.getColumnIndex("Urgency"))
                Category = cursor.getString(cursor.getColumnIndex("Category"))
                Date_Time = cursor.getString(cursor.getColumnIndex("Date_Time"))
                Sorted_ID = cursor.getInt(cursor.getColumnIndex("Sorted_ID"))

                val sortedTask = SortedTaskList(id = id, Task_ID = Task_ID, TaskName = TaskName, DueDate = DueDate, DueTime = DueTime, TaskRank = TaskRank, TaskHour = TaskHour, Priority = Priority, Urgency = Urgency, Category = Category, Date_Time = Date_Time, Sorted_ID = Sorted_ID)
                sortedTaskList.add(sortedTask)
            }while (cursor.moveToNext())
        }

        return sortedTaskList
        println("Test Success")
    }

    @SuppressLint("Range")
    fun updateSortedID(id: Int, SortedID: Int, status: Int){
        val database = this.writableDatabase
        val query = "Select * from $SORTEDTASK WHERE $ID = $id"
        val result = database.rawQuery(query, null)
        if(result.moveToFirst()){
            do {
                var cv = ContentValues()
                cv.put(SORTED_ID, SortedID)
                cv.put(STATUS, status)
                println(status)
                val a = database.update(SORTEDTASK, cv, "$ID=$id", null)
            }while (result.moveToNext())
        }
        result.close()
    }


    fun updateStatus(id: Int, status: Int){
        println("connection success")
        val database = this.writableDatabase
        val query = "Select * from $SORTEDTASK WHERE $ID = $id"
        val result = database.rawQuery(query, null)
        if(result.moveToFirst()){
            do {
                var cv = ContentValues()
                cv.put(STATUS, status)
                println(status)
                val a = database.update(SORTEDTASK, cv, "$ID=$id", null)
                println(a)
            }while (result.moveToNext())
        }
        result.close()
    }

    fun updateTaskTable(id: Int, status: Int){
        println("connection success")
        val database = this.writableDatabase
        val query = "Select * from $TASKTABLE WHERE $ID = $id"
        val result = database.rawQuery(query, null)
        if(result.moveToFirst()){
            do {
                var cv = ContentValues()
                cv.put(STATUS, status)
                println(status)
                val a = database.update(TASKTABLE, cv, "$ID=$id", null)
                println(a)
            }while (result.moveToNext())
        }
        result.close()
    }

    fun updateTaskTime(id: Int, date: String, start: String, end: String){
        val database = this.writableDatabase
        val query = "Select * from $SORTEDTASK WHERE $ID = $id"
        val result = database.rawQuery(query, null)
        if(result.moveToFirst()){
            do {
                var cv = ContentValues()
                cv.put(DATETIME, date)
                cv.put(STIME, start)
                cv.put(ETIME, end)
                val a = database.update(SORTEDTASK, cv, "$ID=$id", null)
            }while (result.moveToNext())

        }
        result.close()
    }


    @SuppressLint("Range")
    fun getTaskTime(): ArrayList<SortedTaskList>{
        val sortedTaskList: ArrayList<SortedTaskList> = ArrayList()

        val selectQuery = "SELECT * FROM $SORTEDTASK WHERE $STATUS != 1 AND $STIME IS NOT NULL ORDER BY $STIME"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: Exception){
            db.execSQL(selectQuery)
            e.printStackTrace()
            return ArrayList()
        }

        var id: Int
        var Task_ID: Int
        var TaskName: String
        var DueDate: String
        var DueTime: String
        var TaskRank: Int
        var TaskHour: Int
        var Priority: Int
        var Urgency: String
        var Category: String
        var Date_Time: String
        var Sorted_ID: Int
        var Start_Time: String
        var End_Time: String


        if(cursor.moveToNext()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("id"))
                Task_ID = cursor.getInt(cursor.getColumnIndex("Task_ID"))
                TaskName = cursor.getString(cursor.getColumnIndex("Task_Name"))
                DueDate = cursor.getString(cursor.getColumnIndex("Due_Date"))
                DueTime = cursor.getString(cursor.getColumnIndex("Due_Time"))
                TaskRank = cursor.getInt(cursor.getColumnIndex("Task_Rank"))
                TaskHour = cursor.getInt(cursor.getColumnIndex("Task_Hour"))
                Priority = cursor.getInt(cursor.getColumnIndex("Priority"))
                Urgency = cursor.getString(cursor.getColumnIndex("Urgency"))
                Category = cursor.getString(cursor.getColumnIndex("Category"))
                Date_Time = cursor.getString(cursor.getColumnIndex("Date_Time"))
                Sorted_ID = cursor.getInt(cursor.getColumnIndex("Sorted_ID"))
                Start_Time = cursor.getString(cursor.getColumnIndex("Start_Time"))
                End_Time = cursor.getString(cursor.getColumnIndex("End_Time"))

                val sortedTask = SortedTaskList(id = id, Task_ID = Task_ID, TaskName = TaskName, DueDate = DueDate, DueTime = DueTime, TaskRank = TaskRank, TaskHour = TaskHour, Priority = Priority, Urgency = Urgency, Category = Category, Date_Time = Date_Time, sTime = Start_Time, Sorted_ID = Sorted_ID, eTime = End_Time)
                sortedTaskList.add(sortedTask)
            }while (cursor.moveToNext())
        }

        return sortedTaskList
        println("Test Success")
    }

    @SuppressLint("Range")
    fun viewTaskList(): ArrayList<SortedTaskList>{
        val sortedTaskList: ArrayList<SortedTaskList> = ArrayList()

        val selectQuery = "SELECT * FROM $SORTEDTASK WHERE $STATUS != 1 ORDER BY $STIME"
        val db = this.readableDatabase

        val cursor: Cursor?

        try {
            cursor = db.rawQuery(selectQuery, null)
        }catch (e: Exception){
            db.execSQL(selectQuery)
            e.printStackTrace()
            return ArrayList()
        }

        var id: Int
        var Task_ID: Int
        var TaskName: String
        var DueDate: String
        var DueTime: String
        var TaskRank: Int
        var TaskHour: Int
        var Priority: Int
        var Urgency: String
        var Category: String
        var Date_Time: String
        var Sorted_ID: Int
        var Start_Time: String
        var End_Time: String


        if(cursor.moveToNext()){
            do{
                id = cursor.getInt(cursor.getColumnIndex("id"))
                Task_ID = cursor.getInt(cursor.getColumnIndex("Task_ID"))
                TaskName = cursor.getString(cursor.getColumnIndex("Task_Name"))
                DueDate = cursor.getString(cursor.getColumnIndex("Due_Date"))
                DueTime = cursor.getString(cursor.getColumnIndex("Due_Time"))
                TaskRank = cursor.getInt(cursor.getColumnIndex("Task_Rank"))
                TaskHour = cursor.getInt(cursor.getColumnIndex("Task_Hour"))
                Priority = cursor.getInt(cursor.getColumnIndex("Priority"))
                Urgency = cursor.getString(cursor.getColumnIndex("Urgency"))
                Category = cursor.getString(cursor.getColumnIndex("Category"))
                Date_Time = cursor.getString(cursor.getColumnIndex("Date_Time"))
                Sorted_ID = cursor.getInt(cursor.getColumnIndex("Sorted_ID"))
                Start_Time = cursor.getString(cursor.getColumnIndex("Start_Time"))
                End_Time = cursor.getString(cursor.getColumnIndex("End_Time"))

                val sortedTask = SortedTaskList(id = id, Task_ID = Task_ID, TaskName = TaskName, DueDate = DueDate, DueTime = DueTime, TaskRank = TaskRank, TaskHour = TaskHour, Priority = Priority, Urgency = Urgency, Category = Category, Date_Time = Date_Time, sTime = Start_Time, Sorted_ID = Sorted_ID, eTime = End_Time)
                sortedTaskList.add(sortedTask)
            }while (cursor.moveToNext())
        }

        return sortedTaskList
        println("Test Success")
    }

    fun updateData(schoolRank: Int, familyRank: Int, personalRank: Int, schoolHOUR: String, familyHOUR: String, personalHOUR: String ): Int {
        val database = this.writableDatabase
        val contentValues = ContentValues()
        val id = 1
        var res = 0
        val query = "Select * from $TABLENAME WHERE $ID = $id"
        val result = database.rawQuery(query, null)
        if(result.moveToFirst()){
            do {
                contentValues.put(COL_SCHOOL_RANK, schoolRank)
                contentValues.put(COL_FAMILY_RANK, familyRank)
                contentValues.put(COL_PERSONAL_RANK, personalRank)

                contentValues.put(COL_SCHOOL_HOUR, schoolHOUR)
                contentValues.put(COL_FAMILY_HOUR, familyHOUR)
                contentValues.put(COL_PERSONAL_HOUR, personalHOUR)
                val a = database.update(TABLENAME, contentValues, "$ID=$id", null)
                println(a)
                res = a
            }while (result.moveToNext())
        }

        return res
        result.close()
    }

    fun updateDiligent(diligency: Int){
        println("connection success")
        val id = 1
        val database = this.writableDatabase
        val query = "Select * from $TABLENAME WHERE $COL_ID = $id"
        val result = database.rawQuery(query, null)
        if(result.moveToFirst()){
            do {
                var cv = ContentValues()
                cv.put(DILIGENCY, diligency)
                println(diligency)
                val a = database.update(TABLENAME, cv, "$COL_ID=$id", null)
                println(a)
            }while (result.moveToNext())
        }
        result.close()
    }
}



