package com.example.ownercafein

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment

class AlarmFragment : Fragment() {
    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        var view = inflater.inflate(R.layout.alarm, container, false)

        return view
    }

    fun newInstance(): Fragment {
        var fragment = AlarmFragment()
        var args = Bundle()
        fragment.arguments = args
        return fragment
    }

}