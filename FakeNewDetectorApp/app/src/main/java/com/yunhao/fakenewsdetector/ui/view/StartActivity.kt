package com.yunhao.fakenewsdetector.ui.view

import android.opengl.Visibility
import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.databinding.ActivityStartBinding
import com.yunhao.fakenewsdetector.ui.view.common.ActivityBase
import com.yunhao.fakenewsdetector.ui.viewmodel.StartViewModel
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class StartActivity : ActivityBase<ActivityStartBinding, ViewModelBase>() {

    override val viewModel: StartViewModel by viewModels()

    override fun getLayoutId(): Int {
        return R.layout.activity_start
    }

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setSupportActionBar(binding!!.toolbar)

        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Setup navigation bar
        if (binding != null){
            setupWithNavController(binding!!.bottomNavigation, navController)
        }

        SetUpListeners()
    }

    override fun onCreateOptionsMenu(menu: Menu): Boolean {
        // Inflate the menu; this adds items to the action bar if it is present.
//        menuInflater.inflate(R.menu.menu_main, menu)
        return true
    }

    override fun onOptionsItemSelected(item: MenuItem): Boolean {
        // Handle action bar item clicks here. The action bar will
        // automatically handle clicks on the Home/Up button, so long
        // as you specify a parent activity in AndroidManifest.xml.
        return when (item.itemId) {
//            R.id.action_settings -> true
            else -> super.onOptionsItemSelected(item)
        }
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }


    private fun SetUpListeners() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.WelcomeFragment,
                R.id.homeFragment,
                R.id.favoritesFragment,
                R.id.historyFragment,
                R.id.discoverFragment-> {
                    supportActionBar?.hide() // to hide
                }
                else -> {
                    supportActionBar?.show() // to show
                }
            }
        }

        // Decide where to show the bottom navigation component
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.homeFragment,
                R.id.favoritesFragment,
                R.id.historyFragment,
                R.id.discoverFragment -> {
                    binding?.bottomNavigation?.visibility = View.VISIBLE
                }
                else -> {
                    binding?.bottomNavigation?.visibility = View.GONE
                }
            }
        }
    }
}