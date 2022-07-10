package com.example.automatedtaskscheduler.fragments

import android.os.Bundle
import androidx.fragment.app.Fragment
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import android.widget.ImageView
import android.widget.TextView
import android.widget.Toast
import com.example.automatedtaskscheduler.DataBaseHandler
import com.example.automatedtaskscheduler.R

// TODO: Rename parameter arguments, choose names that match
// the fragment initialization parameters, e.g. ARG_ITEM_NUMBER
private const val ARG_PARAM1 = "param1"
private const val ARG_PARAM2 = "param2"

/**
 * A simple [Fragment] subclass.
 * Use the [HomeFragment.newInstance] factory method to
 * create an instance of this fragment.
 */
class HomeFragment : Fragment() {
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
        // Inflate the layout for this fragment
        val rootView = inflater.inflate(R.layout.fragment_home, container, false)

        val compliment = rootView.findViewById(R.id.imageView1) as ImageView
        val text = rootView.findViewById(R.id.compliment) as TextView

        val db = DataBaseHandler(requireContext())
        val qr = db.readableDatabase
        val DILIGENCY = "Diligency"

        val result = qr.rawQuery("SELECT * FROM User_Settings WHERE $DILIGENCY != '' ", null)
        if(result.moveToNext()){
            if(result.getString(8).toInt() in 1..2){
                compliment.setImageResource(R.mipmap.ic_like_foreground)
                text.text = "Keep up the good work"
            }else if(result.getString(8).toInt() in 3..4){
                compliment.setImageResource(R.mipmap.ic_1star_foreground)
                text.text = "You are doing great"
            }else if(result.getString(8).toInt() > 4){
                compliment.setImageResource(R.mipmap.ic_threestar_foreground)
                text.text = "You always submit on time Keep it up and you deserve rest after this tasks"
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
         * @return A new instance of fragment HomeFragment.
         */
        // TODO: Rename and change types and number of parameters
        @JvmStatic
        fun newInstance(param1: String, param2: String) =
            HomeFragment().apply {
                arguments = Bundle().apply {
                    putString(ARG_PARAM1, param1)
                    putString(ARG_PARAM2, param2)
                }
            }
    }
}