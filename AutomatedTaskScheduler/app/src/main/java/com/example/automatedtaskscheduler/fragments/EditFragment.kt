package com.example.automatedtaskscheduler.fragments

import android.app.*
import android.content.Context
import android.content.Context.NOTIFICATION_SERVICE
import android.content.Intent
import android.os.Build
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import androidx.recyclerview.widget.LinearLayoutManager
import androidx.recyclerview.widget.RecyclerView
import com.example.automatedtaskscheduler.*
import java.text.SimpleDateFormat
import java.util.*
import kotlin.collections.ArrayList

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [EditFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class EditFragment : Fragment() {
    private var param1: String? = null
    private var param2: String? = null
    var today = Calendar.getInstance()
    var cal = Calendar.getInstance()
    var cal1 = Calendar.getInstance()
    var refresh : ImageButton? = null


    lateinit var viewing : Spinner
    var holder : TextView? = null
    private lateinit var recyclerView: RecyclerView
    private var adapter: SortedTaskAdapter? = null
    private var adapter1: SortedTaskAdapter? = null

    val hourFormat = SimpleDateFormat("HH")
    val minFormat = SimpleDateFormat("mm")
    val timeFormat = SimpleDateFormat("HH:mm")
    val ampmFormat = SimpleDateFormat("MM/dd/yyyy HH:mm aa")
    val dateFormat = SimpleDateFormat("MM/dd/yyyy")
    val datetimeFormat = SimpleDateFormat("MM/dd/yyyy HH:mm")


    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }

        createNotificationChannel()
    }

    @RequiresApi(Build.VERSION_CODES.O)
    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_edit, container, false)
        viewing = rootView.findViewById<View>(R.id.viewing) as Spinner
        viewing.visibility = View.INVISIBLE

        val adapter = ArrayAdapter.createFromResource(
            requireActivity().baseContext,
            R.array.viewing,
            android.R.layout.simple_spinner_item
        )

        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        refresh = rootView.findViewById<View>(R.id.refreshButton) as ImageButton


        viewing.adapter = adapter

        if(viewing.selectedItem.toString().contentEquals("today")){
            println("this is it")
        }

        recyclerView = rootView.findViewById(R.id.recyclerView)

        initRecyclerView()

        checkTime()

        getTask()

        getSortedTask()

        checkTime()

        refresh!!.setOnClickListener(){
            initRecyclerView()

            getTask()

            getSortedTask()

            checkTime()

        }




        return rootView
    }



    @RequiresApi(Build.VERSION_CODES.O)
    private fun getTask(){
        val db = DataBaseHandler(requireContext())
        val taskList = db.getTask()
        Log.e("pppp", "{${taskList.size}}")
        val sortedTask = taskList.sortedWith(compareBy({it.TaskRank}, {it.Priority}))

        insertSortedTask(sortedTask)
    }

    private fun getSortedTask(){
        val db = DataBaseHandler(requireContext())
        val sortedTask = db.getSortedTask()
        Log.e("pppp", "{${sortedTask.size}}")

        allotTime(sortedTask)
    }

    private fun allotTime(tasks: List<SortedTaskList>){
        // responsible for automatically allowing time to each task

        cal.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH))
        cal1.set(Calendar.DAY_OF_MONTH, today.get(Calendar.DAY_OF_MONTH))
        cal.set(Calendar.HOUR_OF_DAY, today.get(Calendar.HOUR_OF_DAY))
        cal1.set(Calendar.HOUR_OF_DAY, today.get(Calendar.HOUR_OF_DAY))
        cal.set(Calendar.MINUTE, today.get(Calendar.MINUTE))
        cal1.set(Calendar.MINUTE, today.get(Calendar.MINUTE))

        val db = DataBaseHandler(requireContext())

        val initialStartTime = "07:00"
        val endTime = "20:00"
        var startTime = ""
        var taskStart = ""
        var taskEnd = ""
        var date = ""
        var start : Date
        var end : Date
        var full = ""

        val taskTime: ArrayList<SortedTaskList> = db.getTaskTime()

        println("Task Size: "+taskTime.size)

        for((i, item) in taskTime.withIndex()){
//       sets the starting task hour if there are task that are scheduled
            if(taskTime.size > 1) {
                if (item.sTime.isNotEmpty()) {
                    startTime = timeFormat.format(datetimeFormat.parse(item.sTime))
                    cal1.set(
                        Calendar.MINUTE,
                        hourFormat.format(timeFormat.parse(startTime)).toInt()
                    )
                    cal1.set(Calendar.MINUTE, minFormat.format(timeFormat.parse(startTime)).toInt())
                    break
                }
            }
        }


        for((i, item) in tasks.withIndex()){
            if(startTime.isNotEmpty()){
                if(timeFormat.parse(startTime).after(timeFormat.parse(endTime))){
                    addDay()
                    startTime = timeFormat.format(cal1.time)
                }
                taskStart = startTime

            }else{
                taskStart = timeFormat.format(cal.time)
            }
            date = dateFormat.format(cal.time)
            start = datetimeFormat.parse("$date $initialStartTime")
            end = datetimeFormat.parse("$date $endTime")


            if(cal.time.after(start)  && cal.time.before(end)){

                cal1.set(Calendar.HOUR_OF_DAY, hourFormat.format(hourFormat.parse(taskStart)).toInt() + item.TaskHour)
                taskEnd = timeFormat.format(cal1.time)
                startTime = taskEnd

//                adds 1 day to set the task if task list for today is full or user started inserting task after scheduled hour
            }else if(cal.time.after(end)){
                addDay()
                taskStart = timeFormat.format(cal.time)
                cal1.set(Calendar.HOUR_OF_DAY, hourFormat.format(hourFormat.parse(taskStart)).toInt() + item.TaskHour)
                taskEnd = timeFormat.format(cal1.time)

                startTime = taskEnd

            }else if(cal.time.before(start)){
                adjustTime()
                taskStart = timeFormat.format(cal.time)
                cal1.set(Calendar.HOUR_OF_DAY, hourFormat.format(hourFormat.parse(taskStart)).toInt() + item.TaskHour)
                taskEnd = timeFormat.format(cal1.time)
                startTime = taskEnd
            }


            val ddate = dateFormat.format(cal1.time)


            val sTime = datetimeFormat.format(datetimeFormat.parse("$ddate $taskStart"))
            val eTime = datetimeFormat.format(datetimeFormat.parse("$ddate $taskEnd"))
            db.updateTaskTime(item.id, ddate.toString(), sTime.toString(), eTime.toString())

            scheduleNotification(item.TaskName,sTime, datetimeFormat.parse("$ddate $taskStart").time)


        }

    }

    private fun addDay(){
        // adds 1 day
        val dateToday = cal.get(Calendar.DAY_OF_MONTH)
        cal.set(Calendar.DAY_OF_MONTH, dateToday + 1)
        cal1.set(Calendar.DAY_OF_MONTH, dateToday + 1)
        cal.set(Calendar.HOUR_OF_DAY, 7)
        cal.set(Calendar.MINUTE, 0)
        cal1.set(Calendar.HOUR_OF_DAY, 7)
        cal1.set(Calendar.MINUTE, 0)

    }
    private fun adjustTime(){
        // sets time to 7am
        cal.set(Calendar.HOUR_OF_DAY, 7)
        cal.set(Calendar.MINUTE, 0)
        cal1.set(Calendar.HOUR_OF_DAY, 7)
        cal1.set(Calendar.MINUTE, 0)
    }

    private fun checkTime(){
        // validates time allotment
        val db = DataBaseHandler(requireContext())
        val sorted: ArrayList<SortedTaskList> = db.getTaskTime()
//        checks if the scheduled time for the task is pass the time today and update the tasks status
        for((i, item) in sorted.withIndex()){
                if(datetimeFormat.parse(item.eTime).before(today.time)){
                    db.updateStatus(item.id, 1)
                }
        }
        val taskTime = db.viewTaskList()
        adapter?.addItems(taskTime)
    }

    @RequiresApi(Build.VERSION_CODES.O)
    private fun insertSortedTask(sorted: List<TaskList>){
        // insert task to another table from initial table
        var order = 0
        var rank = 0
        var status = 0

        val db = DataBaseHandler(requireContext())
        val qr = db.readableDatabase
        val hourFormat = SimpleDateFormat("HH")
        val minFormat = SimpleDateFormat("mm")
        val timeFormat = SimpleDateFormat("hh:mm")
        val dateFormat = SimpleDateFormat("MM/dd/yyyy")
        val datetimeFormat = SimpleDateFormat("MM/dd/yyyy hh:mm")
        val currentHour = hourFormat.format(Date())
        val currentMin = minFormat.format(Date())
        val currentDate = dateFormat.format(Date())

        for((i, item) in sorted.withIndex()){

            val id = item.id

            if(item.DueDate.isNotEmpty()) {
                val dateAndTime = datetimeFormat.parse(item.DueDate + " " + item.DueTime)

//                checks if the time today is after the set due date if yes task should not be seen
                if (dateAndTime.after(today.time)) {
                    val hour = hourFormat.format(hourFormat.parse(item.DueTime)).toInt()
                    val min = minFormat.format(minFormat.parse(item.DueTime)).toInt()
                    val hourDiff = hour - currentHour.toInt()
                    val minDiff = min - currentMin.toInt()

                    if(hourDiff in 0..5) {
                        rank = item.TaskRank
                        order = 0
                        status = 0
                    }else{
                        status = 0
                        order = i + 1
                        rank = item.TaskRank
                    }
                }else if (dateAndTime.equals(today.time) || dateAndTime.before(today.time)){
                    status = 1
                }
//                if no task remains
            }else{
                order = i + 1
                rank = item.TaskRank
                status = 0
            }

            val result = qr.rawQuery("SELECT Task_ID FROM Sorted_Task WHERE Task_ID = $id", null)

            if(result.count > 0){
                db.updateSortedID(item.id, order, status)
            }else{

                val sortedtask = Sorted_Task(item.id, item.TaskName, item.Urgency, item.DueDate, item.DueTime, item.Category, rank, item.TaskHour, item.Priority, "", order)
                db.insertSortedTask(sortedtask)
            }
        }
    }

    private fun scheduleNotification(taskName: String, sTime: String, time: Long) {
        val intent = Intent(requireContext(), AppNotification::class.java)
        intent.putExtra(titleExtra, "Task Name: $taskName")
        intent.putExtra(messageExtra, "Time: $sTime")

        val pendingIntent = PendingIntent.getBroadcast(
            requireContext(),
            notificationID,
            intent,
            PendingIntent.FLAG_IMMUTABLE or PendingIntent.FLAG_UPDATE_CURRENT
        )

        val alarmManager = requireContext().getSystemService(Context.ALARM_SERVICE) as AlarmManager
        val time = time

        alarmManager.setExactAndAllowWhileIdle(
            AlarmManager.RTC_WAKEUP,
            time,
            pendingIntent
        )
    }

    private fun showAlert(time: Long, taskName: String) {
        val date = Date(time)
        val dateFormat = android.text.format.DateFormat.getLongDateFormat(requireContext())
        val timeFormat = android.text.format.DateFormat.getTimeFormat(requireContext())

        AlertDialog.Builder(requireContext())
            .setTitle("Notification Scheduled")
            .setMessage(
                "Title: " + taskName+
            "\nAt: " + dateFormat.format(date) + " " + timeFormat.format(date))
            .setPositiveButton("Okay"){_,_->}
            .show()
    }

    private fun getTime(): Long {
        val minute = today.get(Calendar.MINUTE)
        val hour = today.get(Calendar.HOUR_OF_DAY)
        val day = today.get(Calendar.DAY_OF_MONTH)
        val month = today.get(Calendar.MONTH)
        val year = today.get(Calendar.YEAR)

        val notifCal = Calendar.getInstance()
        notifCal.set(year, month, day, hour, minute)

        return notifCal.timeInMillis
    }


    @RequiresApi(Build.VERSION_CODES.O)
    private fun createNotificationChannel() {
        val name = "Notif Channel"
        val desc = "Task Notification"
        val importance = NotificationManager.IMPORTANCE_DEFAULT
        val channel = NotificationChannel(channelID.toString(), name, importance)
        channel.description = desc
        val notificationManager = requireContext().getSystemService(NOTIFICATION_SERVICE) as NotificationManager
        notificationManager.createNotificationChannel(channel)
    }

    private fun initRecyclerView(){
//        val context = requireContext()
        recyclerView.layoutManager = LinearLayoutManager(requireActivity())
        adapter = SortedTaskAdapter()
        recyclerView.adapter = adapter

        adapter!!.setOnItemClickListener(object: SortedTaskAdapter.onItemClickListener{
            override fun onItemClick(position: Int) {

            }
        })

        adapter?.setOnClickRemoveItem {
            removeItem(it.id)
        }
        adapter?.setOnClickComplete {
            completeTask(it.id)
        }
    }

    private fun completeTask(id: Int) {
        val db = DataBaseHandler(requireContext())
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure the task is complete?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes"){ dialog, _ ->
            db.updateStatus(id, 1)
            db.updateTaskTable(id, 1)

            val qr = db.readableDatabase

            val result = qr.rawQuery("SELECT * FROM User_Settings", null)
            if(result.moveToNext()){
                db.updateDiligent(result.getString(8).toInt() + 1)
            }

            refresh!!.performClick()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }

        val alert = builder.create()
        alert.show()

    }

    private fun removeItem(id: Int){
        val db = DataBaseHandler(requireContext())
        val builder = AlertDialog.Builder(requireContext())
        builder.setMessage("Are you sure you want to remove the task?")
        builder.setCancelable(true)
        builder.setPositiveButton("Yes"){ dialog, _ ->
            db.updateStatus(id, 1)
            db.updateTaskTable(id, 1)
            refresh!!.performClick()
            dialog.dismiss()
        }
        builder.setNegativeButton("No") { dialog, _ ->
            dialog.dismiss()
        }
        val alert = builder.create()
        alert.show()
    }


    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment EditFragment.
         */
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            EditFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}