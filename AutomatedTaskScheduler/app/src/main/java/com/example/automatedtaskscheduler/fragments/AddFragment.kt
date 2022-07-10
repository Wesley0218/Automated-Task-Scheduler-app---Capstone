package com.example.automatedtaskscheduler.fragments

import android.app.DatePickerDialog
import android.app.TimePickerDialog
import android.os.Bundle
import android.util.Log
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.*
import androidx.fragment.app.Fragment
import com.android.volley.AuthFailureError
import com.android.volley.Response
import com.android.volley.toolbox.JsonObjectRequest
import com.android.volley.toolbox.Volley
import com.example.automatedtaskscheduler.DataBaseHandler
import com.example.automatedtaskscheduler.R
import com.example.automatedtaskscheduler.Task_Title
import org.json.JSONException
import org.json.JSONObject
import java.text.SimpleDateFormat
import java.util.*

// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"


class AddFragment : Fragment() {

    private var param1: String? = null
    private var param2: String? = null

    var cal = Calendar.getInstance()
    var saveButton : ImageButton? = null
    var schoolRank: Int = 0
    var familyRank: Int = 0
    var personalRank: Int = 0

    var schoolHOUR: String = ""
    var familyHOUR: String = ""
    var personalHOUR: String = ""

    lateinit var selectTime : Button
    lateinit var setTask : EditText
    lateinit var urgency : Spinner
    lateinit var category : String
    lateinit var time: String
    lateinit var date: String
    lateinit var datetime: Date


    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
        val context = requireContext()
        val db = DataBaseHandler(context)
        val qr = db.readableDatabase
        val result = qr.rawQuery("SELECT * FROM User_Settings", null)
        if(result.moveToNext()){
            schoolRank = result.getString(1).toInt()
            familyRank = result.getString(2).toInt()
            personalRank = result.getString(3).toInt()

            schoolHOUR = result.getString(4)
            familyHOUR = result.getString(5)
            personalHOUR = result.getString(6)
        }

    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_add, container, false)

        urgency = rootView.findViewById<View>(R.id.urgency) as Spinner
        val adapter = ArrayAdapter.createFromResource(
            requireActivity().baseContext,
            R.array.urgency,
            android.R.layout.simple_spinner_item
        )

//        sets drop down values
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)

        urgency.adapter = adapter


        val year = cal.get(Calendar.YEAR)
        val month = cal.get(Calendar.MONTH)
        val day = cal.get(Calendar.DAY_OF_MONTH)


        val selectDate = rootView.findViewById<View>(R.id.selectDate) as Button

        selectTime = rootView.findViewById<View>(R.id.selectTime) as Button

        saveButton = rootView.findViewById<View>(R.id.saveButton) as ImageButton

        setTask = rootView.findViewById<View>(R.id.editText) as EditText


        selectDate.setOnClickListener() {
            val dpd = DatePickerDialog(
                requireContext(),
                DatePickerDialog.OnDateSetListener() { view: DatePicker, mYear: Int, mMonth: Int, mDay: Int ->
                    selectDate.text = "" + mDay + " / " + (mMonth + 1) + " / " + mYear

                    // call to function that is responsible for getting the value of date selected
                    getDate((mMonth + 1), mDay, mYear)

                    cal.set(Calendar.YEAR, mYear)
                    cal.set(Calendar.MONTH, mMonth)
                    cal.set(Calendar.DAY_OF_MONTH, mDay)
                },
                year,
                month,
                day
            )

            dpd.show()
        }

        selectTime.setOnClickListener(){
            val timePickerDialog = TimePickerDialog(requireContext(), TimePickerDialog.OnTimeSetListener { view, hourOfDay, minute ->
                showTime(hourOfDay, minute)
                cal.set(Calendar.HOUR_OF_DAY, hourOfDay)
                cal.set(Calendar.MINUTE, minute)

            },cal.get(Calendar.HOUR_OF_DAY), cal.get(Calendar.MINUTE), false )
            timePickerDialog.show()

        }

        saveButton!!.setOnClickListener(){
            println("test button")
            apiCall()
            selectDate.text = "Select Date"
            selectTime.text = "Set Time"
            setTask.text.clear()
        }



        return rootView

    }

    //function used to save all inputs to database
    private fun saveToDb(TaskTitle: String){


        var Date : String
        var Time : String
        var Title : String
        var Urgency : String
        var Category : String

        var taskRank: Int = 0
        var taskHour: Int = 0
        var priority: Int = 0

            if(this::time.isInitialized && this::date.isInitialized){
                val sdfTime = SimpleDateFormat("HH:mm", Locale.ENGLISH)
                val testTime = sdfTime.format(cal.time)

                Date = date

                Time = testTime

                Title = TaskTitle

                Urgency = urgency.selectedItem.toString()

                Category = category

            }else if(!this::time.isInitialized && this::date.isInitialized){
                Date = date

                Time = "23:59"

                Title = TaskTitle

                Urgency = urgency.selectedItem.toString()

                Category = category
            }else{
                Date = ""

                Time = ""

                Title = TaskTitle

                Urgency = urgency.selectedItem.toString()

                Category = category
            }
        when {
            Category.contentEquals("school") -> {
                taskRank = schoolRank
                taskHour = schoolHOUR.take(1).toInt()
            }
            Category.contentEquals("family") -> {
                taskRank = familyRank
                taskHour = familyHOUR.take(1).toInt()
            }
            Category.contentEquals("personal") -> {
                taskRank = personalRank
                taskHour = personalHOUR.take(1).toInt()
            }
        }
        priority = if(Urgency.contentEquals("urgent")){
            0
        }else{
            1
        }


        val context = requireContext()
        val db = DataBaseHandler(context)

        val taskDetail = Task_Title(Title, Urgency, Date, Time, Category, taskRank, taskHour, priority)
        db.insertTask(taskDetail)
    }

    private fun getDate(mMonth: Int, mDay: Int, mYear: Int){
        date = "$mMonth/$mDay/$mYear"
    }

    private fun showTime(hourOfDay: Int, minute: Int) {
        var hours = hourOfDay
        var minutes = minute
        var Minutes : Any
        var amp = ""
        amp = if(hours >= 12){
            "pm"
        }else{
            "am"
        }
        hours = (hours % 12)

        hours = if(hours == 0){
            12
        }else{
            hours
        }
        Minutes = if (minutes < 10) "0$minutes" else minutes
        var strTime = "$hours:$Minutes $amp"




        selectTime.text = strTime
        time = strTime
    }

    private fun apiCall() {
        Log.d("EXAMPLE", "apiCall()")
        val queue = Volley.newRequestQueue(requireActivity().applicationContext)
        val params: MutableMap<String?, String?> = HashMap()
        params["data"] = setTask.text.toString()
        val o = JSONObject(params as Map<*, *>?)
        println(o.getString("data"))
        val jsonObjectRequest: JsonObjectRequest = object : JsonObjectRequest(
            Method.POST,
            "https://influent-swamps.000webhostapp.com/",  // see http://developer.android.com/tools/devices/emulator.html#networkaddresses "https://influent-swamps.000webhostapp.com/"
            o,
            Response.Listener { response ->
                try {
                    Log.d("EXAMPLE", "Register Response: $response")
                    Toast.makeText(requireContext(), "Task Saved to The List", Toast.LENGTH_LONG).show()
                } catch (e: JSONException) {
                    e.printStackTrace()
                }
                println("correct response")
                category = response.getString("response")
                getResponse(response.getString("response"))
                saveToDb(o.getString("data"))

            },
            Response.ErrorListener { error ->
                error.printStackTrace()
                Toast.makeText(requireContext(), "Task is not Saved to The List", Toast.LENGTH_LONG).show()
                println("incorrect response")
            }) {
            override fun getBodyContentType(): String {
                return "application/json; charset=utf-8"
            }



        }
        queue.add(jsonObjectRequest)
    }

    private fun getResponse(response: String){
        category = response
    }


    companion object {

        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            AddFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}