package com.example.fragmentdemo.fragments

import android.os.Build
import android.os.Bundle
import android.view.LayoutInflater
import android.view.View
import android.view.ViewGroup
import androidx.annotation.RequiresApi
import androidx.fragment.app.Fragment
import com.example.fragmentdemo.Options
import com.example.fragmentdemo.contract.navigator
import com.example.fragmentdemo.databinding.FragmentMenuBinding

class MenuFragment : Fragment() {
    private lateinit var options : Options

    @RequiresApi(Build.VERSION_CODES.TIRAMISU)
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        options = savedInstanceState?.getParcelable(KEY_OPTIONS, Options::class.java) ?: Options.Default
    }


    override fun onCreateView(
        inflater: LayoutInflater,
        container: ViewGroup?,
        savedInstanceState: Bundle?
    ): View? {
        val binding = FragmentMenuBinding.inflate(inflater, container, false)
        navigator().listenResult(Options::class.java, viewLifecycleOwner){
            this.options = it
        }

        binding.apply {
            openBoxButton.setOnClickListener {onOpenBoxPressed()}
            optionsButton.setOnClickListener {onOptionPressed()}
            aboutButton.setOnClickListener{onAboutPressed()}
            exitButton.setOnClickListener{onExitPressed()}
            return root
        }

    }

    override fun onSaveInstanceState(outState: Bundle) {
        super.onSaveInstanceState(outState)
        outState.putParcelable(KEY_OPTIONS,options)
    }

    private fun onOpenBoxPressed(){
        navigator().showBoxSelectionScreen(options)

    }
    private fun onOptionPressed() {
        navigator().showOptionsScreen(options)
    }

    private fun onAboutPressed() {
        navigator().showAboutScreen()
    }

    private fun onExitPressed() {
        navigator().goBack()
    }



    companion object {
        @JvmStatic
        private val KEY_OPTIONS = "OPTIONS"
    }

}