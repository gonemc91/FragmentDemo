package com.example.fragmentdemo.fragments

import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.fragment.app.Fragment
import com.example.fragmentdemo.BuildConfig
import com.example.fragmentdemo.R
import com.example.fragmentdemo.contract.HasCustomTitle
import com.example.fragmentdemo.contract.navigator
import com.example.fragmentdemo.databinding.FragmentAboutBinding

class AboutFragment : Fragment(),HasCustomTitle {

    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View = FragmentAboutBinding.inflate(inflater, container, false).apply {
        versionNameTextView.text = BuildConfig.VERSION_NAME
        versionCodeTextView.text = BuildConfig.VERSION_CODE.toString()
        okButton.setOnClickListener {onOkPressed()}
    }.root


    override fun getTitleRes() = R.string.about

    private fun onOkPressed() {
        navigator().goBack()
    }


}