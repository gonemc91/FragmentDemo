package com.example.fragmentdemo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fragmentdemo.R
import com.example.fragmentdemo.contract.HasCustomTitle
import com.example.fragmentdemo.contract.navigator
import com.example.fragmentdemo.databinding.FragmentBoxBinding

class BoxFragment : Fragment(), HasCustomTitle {

    override fun onCreateView(inflater: LayoutInflater, container: ViewGroup?, savedInstanceState: Bundle?
    ): View?  = FragmentBoxBinding.inflate(inflater,container, false).apply {
        toMainMenuButton.setOnClickListener{onToMainMenuPressed()}
    }.root

    private fun onToMainMenuPressed(){
        navigator().goToMenu()
    }
    override fun getTitleRes(): Int = R.string.box
}