package com.example.fragmentdemo

import android.graphics.Color.WHITE
import android.os.Bundle
import android.os.Parcelable
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.appcompat.R.anim.*
import androidx.appcompat.app.AppCompatActivity
import androidx.core.content.ContextCompat
import androidx.core.graphics.drawable.DrawableCompat
import androidx.core.os.bundleOf
import androidx.fragment.app.Fragment
import androidx.fragment.app.FragmentManager
import androidx.fragment.app.FragmentResultListener
import androidx.lifecycle.LifecycleOwner
import androidx.navigation.NavController
import androidx.navigation.fragment.NavHostFragment
import androidx.navigation.navOptions
import com.example.fragmentdemo.contract.*
import com.example.fragmentdemo.databinding.ActivityMainBinding
import com.example.fragmentdemo.fragments.BoxSelectionFragment
import com.example.fragmentdemo.fragments.OptionsFragment


class MainActivity : AppCompatActivity(), Navigator {

    private  lateinit var binding: ActivityMainBinding

    private lateinit var navController: NavController

    private var currentFragment: Fragment? = null


    private val fragmentListener  = object : FragmentManager.FragmentLifecycleCallbacks(){
        override fun onFragmentViewCreated(fm: FragmentManager, f: Fragment, v: View, savedInstanceState: Bundle?) {
            super.onFragmentViewCreated(fm, f, v, savedInstanceState)
            if (f is NavHostFragment) return
            currentFragment = f
            updateUi()
        }
    }

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        binding = ActivityMainBinding.inflate(layoutInflater).also { it -> setContentView(it.root) }
       /* setSupportActionBar(binding.toolbar)*/

        val navHost = supportFragmentManager.findFragmentById(R.id.fragmentContainer) as NavHostFragment
        navController = navHost.navController


        supportFragmentManager.registerFragmentLifecycleCallbacks(fragmentListener, true)

    }

    override fun onDestroy() {
        super.onDestroy()
        supportFragmentManager.unregisterFragmentLifecycleCallbacks(fragmentListener)
    }

    override fun onCreateOptionsMenu(menu: Menu?): Boolean {
        // we've called setSupportActionBar in onCreate,
        // that's why we need to override this method too
        updateUi()
        return true
    }

    override fun onSupportNavigateUp() = navController.navigateUp() || super.onSupportNavigateUp()



////////////////////////////////////////////////////////////////

    override fun showBoxSelectionScreen(options: Options) {
        launchDestination(R.id.boxSelectionFragment,BoxSelectionFragment.createArgs(options))
    }

    override fun showOptionsScreen(options: Options) {
        launchDestination(R.id.optionsFragment, OptionsFragment.createArgs(options))
    }

    override fun showAboutScreen() {
        launchDestination(R.id.aboutFragment,null)
    }

    override fun showCongratulationScreen() {
        launchDestination(R.id.boxFragment, null)
    }

    override fun goBack() {
        onBackPressedDispatcher.onBackPressed()
    }

    override fun goToMenu(){
        navController.popBackStack(R.id.menuFragment, false)
    }

    //TODO rewrite
    override fun <T : Parcelable> publishResult(result: T) {
        supportFragmentManager.setFragmentResult(result.javaClass.name, bundleOf(KEY_RESULT to result))
    }

    override fun <T : Parcelable> listenResult(clazz: Class<T>, owner: LifecycleOwner, listener: ResultListener<T>
    ) {
        supportFragmentManager.setFragmentResultListener(clazz.name, owner, FragmentResultListener{key,bundle ->
            listener.invoke(bundle.getParcelable(KEY_RESULT)!!)
        }
        )
    }

    ////////////////////////////////////////////////////////
    private fun launchDestination(destinationID: Int, args: Bundle?) {
        navController.navigate(
            destinationID,
            args,
            navOptions {
                anim {
                    enter = abc_slide_in_bottom
                    exit = abc_fade_out
                    popEnter = abc_fade_in
                    popExit = abc_slide_out_bottom
                }
            }

        )

    }


    private fun updateUi() {
        val fragment = currentFragment
        if (fragment is HasCustomTitle) {
            binding.toolbar.title = getString(fragment.getTitleRes())
        } else {
            binding.toolbar.title = getString(R.string.fragment_navigation_example)
        }



        if (navController.currentDestination?.id == navController.graph.startDestinationId) {
            supportActionBar?.setDisplayHomeAsUpEnabled(false)
        } else {
            supportActionBar?.setDisplayHomeAsUpEnabled(true)
        }

        if (fragment is HasCustomAction) {
            createCustomToolbarAction(fragment.getCustomAction())
        } else {
            binding.toolbar.menu.clear()
        }

    }


    private fun createCustomToolbarAction(action: CustomAction){
        binding.toolbar.menu.clear() // clearing old action if it exists before assigning a new one

        val iconDrawable = DrawableCompat.wrap(ContextCompat.getDrawable(this,action.iconRes)!!)
        iconDrawable.setTint(WHITE)

        val menuItem = binding.toolbar.menu.add(action.textRes)
        menuItem.setShowAsAction(MenuItem.SHOW_AS_ACTION_ALWAYS)
        menuItem.icon = iconDrawable
        menuItem.setOnMenuItemClickListener {
            action.onCustomAction.run()
            return@setOnMenuItemClickListener true
        }

    }

    companion object{
         private const val KEY_RESULT = "RESULT"
    }


}