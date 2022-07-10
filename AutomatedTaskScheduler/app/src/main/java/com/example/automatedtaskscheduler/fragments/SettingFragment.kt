package com.example.automatedtaskscheduler.fragments

import android.app.TimePickerDialog
import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.ArrayAdapter
import android.widget.Button
import android.widget.Spinner
import android.widget.Toast
import com.example.automatedtaskscheduler.DataBaseHandler
import com.example.automatedtaskscheduler.R
import com.example.automatedtaskscheduler.User_Setting
import java.util.*

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [SettingFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class SettingFragment : Fragment() {
    // TODO: Rename and change types of parameters
    private var param1: String? = null
    private var param2: String? = null

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        arguments?.let {
            param1 = it.getString(ARG_PARAM1)
            param2 = it.getString(ARG_PARAM2)
        }
    }

    override fun onCreateView(
        inflater: LayoutInflater, container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {

        val context = requireContext()
        val db = DataBaseHandler(context)
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_setting, container, false)

        val schoolRank = rootView.findViewById<View>(R.id.spinnerSchoolSetting) as Spinner
        val familyRank = rootView.findViewById<View>(R.id.spinnerFamilySetting) as Spinner
        val personalRank = rootView.findViewById<View>(R.id.spinnerPersonalSetting) as Spinner

        val schoolHour = rootView.findViewById<View>(R.id.spinnerSchoolHoursSetting) as Spinner
        val familyHour = rootView.findViewById<View>(R.id.spinnerFamilyHoursSetting) as Spinner
        val personalHour = rootView.findViewById<View>(R.id.spinnerPersonalHoursSetting) as Spinner


        val adapter = ArrayAdapter.createFromResource(
            requireActivity().baseContext,
            R.array.ranking,
            android.R.layout.simple_spinner_item
        )
        adapter.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        schoolRank.adapter = adapter
        familyRank.adapter = adapter
        personalRank.adapter = adapter

        val adapterHour = ArrayAdapter.createFromResource(
            requireActivity().baseContext,
            R.array.hours,
            android.R.layout.simple_spinner_item
        )
        adapterHour.setDropDownViewResource(android.R.layout.simple_spinner_dropdown_item)
        schoolHour.adapter = adapter
        familyHour.adapter = adapter
        personalHour.adapter = adapter

        var save = rootView.findViewById<View>(R.id.buttonSettingSave) as Button

        save.setOnClickListener(){

            val schoolRankSelected = schoolRank.selectedItem.toString().toInt()
            val familyRankSelected = familyRank.selectedItem.toString().toInt()
            val personalRankSelected = personalRank.selectedItem.toString().toInt()

            val schoolHourSelected = schoolHour.selectedItem
            val familyHoursSelected = familyHour.selectedItem
            val personalHoursSelected = personalHour.selectedItem


            val result = db.updateData(schoolRankSelected.toString().toInt(), familyRankSelected.toString().toInt(), personalRankSelected.toString().toInt(), schoolHourSelected.toString(), familyHoursSelected.toString(), personalHoursSelected.toString())
            println(result)
            if(result == 1){
                Toast.makeText(requireContext(), "User Profile is Updated", Toast.LENGTH_LONG).show()
            }else{
                Toast.makeText(requireContext(), "Error try again", Toast.LENGTH_LONG).show()
            }
        }


        return rootView
    }

    companion object {
        /**
         * Use this factory method to create a new instance of
         * this fragment using the provided parameters.
         *
         * @param param1 Parameter 1.
         * @param param2 Parameter 2.
         * @return A new instance of fragment SettingFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            SettingFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}