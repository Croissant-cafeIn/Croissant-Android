package com.example.ownercafein

import android.content.Intent
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import android.widget.Button
import androidx.appcompat.app.AppCompatActivity
import androidx.fragment.app.Fragment

class SetCongestion : Fragment(){
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.set_congestion, container, false)
        return view
    }
    fun newInstance(): Fragment {
        var fragment = SetCongestion()
        var args = Bundle()
        fragment.arguments = args

        return fragment
    }
}