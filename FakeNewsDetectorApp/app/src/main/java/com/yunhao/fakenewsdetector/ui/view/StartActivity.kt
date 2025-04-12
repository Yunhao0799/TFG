package com.yunhao.fakenewsdetector.ui.view

import android.os.Bundle
import androidx.navigation.findNavController
import androidx.navigation.ui.AppBarConfiguration
import androidx.navigation.ui.navigateUp
import androidx.navigation.ui.setupActionBarWithNavController
import android.view.Menu
import android.view.MenuItem
import android.view.View
import android.view.ViewGroup
import androidx.activity.viewModels
import androidx.navigation.NavController
import androidx.navigation.ui.NavigationUI.setupWithNavController
import com.yunhao.fakenewsdetector.BuildConfig
import com.yunhao.fakenewsdetector.R
import com.yunhao.fakenewsdetector.databinding.ActivityStartBinding
import com.yunhao.fakenewsdetector.ui.view.common.ActivityBase
import com.yunhao.fakenewsdetector.ui.viewmodel.StartViewModel
import com.yunhao.fakenewsdetector.ui.viewmodel.common.ViewModelBase
import com.yunhao.fakenewsdetector.utils.ReleaseTree
import dagger.hilt.android.AndroidEntryPoint
import androidx.core.view.updateLayoutParams
import com.yunhao.fakenewsdetector.databinding.ContentStartBinding
import com.yunhao.fakenewsdetector.ui.utils.DialogsManager
import timber.log.Timber
import javax.inject.Inject

@AndroidEntryPoint
class StartActivity : ActivityBase<ActivityStartBinding, ViewModelBase>() {

    override val viewModel: StartViewModel by viewModels()

    override fun getLayoutId(): Int {
        return R.layout.activity_start
    }

    @Inject
    lateinit var dialogsManager: DialogsManager

    private lateinit var appBarConfiguration: AppBarConfiguration
    private lateinit var navController: NavController
    private lateinit var contentStartBinding: ContentStartBinding

    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)

        setUpLogger()

        setSupportActionBar(binding!!.toolbar)

        navController = findNavController(R.id.nav_host_fragment_content_main)
        appBarConfiguration = AppBarConfiguration(navController.graph)
        setupActionBarWithNavController(navController, appBarConfiguration)

        supportActionBar?.setDisplayHomeAsUpEnabled(true)
        supportActionBar?.setDisplayShowHomeEnabled(true)

        // Setup navigation bar
        if (binding != null){
            setupWithNavController(binding!!.bottomNavigation, navController)
            contentStartBinding = binding!!.contentLayout
        }

        SetUpListeners()

        dialogsManager.subscribeToEvents(this, this)
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

    private fun setUpLogger() {
        if (BuildConfig.DEBUG) {
            // Plant a Timber DebugTree in debug builds
            Timber.plant(Timber.DebugTree())
        } else {
            // Plant a tree for release builds
            Timber.plant(ReleaseTree())
        }

        // When an unhandled exception happens, log it
        Thread.setDefaultUncaughtExceptionHandler { thread, throwable ->
            Timber.e(throwable, "Uncaught exception in thread: ${thread.name}")
        }
    }

    private fun SetUpListeners() {
        navController.addOnDestinationChangedListener { _, destination, _ ->
            when (destination.id) {
                R.id.WelcomeFragment,
                R.id.homeFragment,
                R.id.favoritesFragment,
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
                R.id.discoverFragment -> {
                    binding?.bottomNavigation?.apply {
                        visibility = View.VISIBLE

                        post {
                            contentStartBinding.layout.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                                bottomMargin =
                                    resources.getDimensionPixelSize(com.google.android.material.R.dimen.design_bottom_navigation_height) +
                                    resources.getDimensionPixelSize(com.google.android.material.R.dimen.m3_bottom_nav_item_padding_bottom)
                            }
                        }
                    }
                }
                else -> {
                    binding?.bottomNavigation?.apply {
                        visibility = View.GONE
                        contentStartBinding.layout.updateLayoutParams<ViewGroup.MarginLayoutParams> {
                            bottomMargin = 0
                        }
                    }
                }
            }
        }
    }
}