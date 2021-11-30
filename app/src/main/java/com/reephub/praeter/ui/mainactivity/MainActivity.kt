package com.reephub.praeter.ui.mainactivity

import android.annotation.SuppressLint
import android.os.Bundle
import android.view.MenuItem
import android.view.View
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.navigation.NavController
import androidx.navigation.NavDestination
import androidx.navigation.findNavController
import androidx.navigation.ui.*
import com.google.android.material.appbar.MaterialToolbar
import com.google.android.material.bottomnavigation.BottomNavigationView
import com.reephub.praeter.R
import com.reephub.praeter.databinding.ActivityMainBinding
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.CoroutineScope
import kotlinx.coroutines.Dispatchers
import kotlinx.coroutines.Job
import timber.log.Timber
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class MainActivity : AppCompatActivity(),
    CoroutineScope,
    NavController.OnDestinationChangedListener,
    BottomNavigationView.OnNavigationItemSelectedListener {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private var _viewBinding: ActivityMainBinding? = null

    // This property is only valid between onCreateView and
    // onDestroyView.
    private val binding get() = _viewBinding!!

    // Views
    private lateinit var toolbar: MaterialToolbar
    private lateinit var bottomNavView: BottomNavigationView

    private lateinit var navController: NavController
    private lateinit var appBarConfiguration: AppBarConfiguration

    private val mViewModel: MainActivityViewModel by viewModels()

    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        _viewBinding = ActivityMainBinding.inflate(layoutInflater)
        setContentView(binding.root)

        bottomNavView = binding.navView

        navController = findNavController(R.id.nav_host_fragment_content_main)

        appBarConfiguration = AppBarConfiguration(
            setOf(
                R.id.navigation_classes,
                R.id.navigation_home,
                R.id.navigation_meet_ancients
            )
        )

        // setupActionBarWithNavController(navController, appBarConfiguration)
        bottomNavView.setupWithNavController(navController)

        setListeners()
    }

    override fun onSupportNavigateUp(): Boolean {
        val navController = findNavController(R.id.nav_host_fragment_content_main)
        return navController.navigateUp(appBarConfiguration)
                || super.onSupportNavigateUp()
    }

    override fun onPause() {
        super.onPause()
    }

    override fun onResume() {
        super.onResume()

    }

    override fun onBackPressed() {
        super.onBackPressed()

        // If current fragment not is home - go to home
        // then if home fragment - leave application
    }

    override fun onDestroy() {
        super.onDestroy()
        _viewBinding = null
    }

    /////////////////////////////////////
    //
    // CLASSES METHODS
    //
    /////////////////////////////////////
    fun goToHome(view: View) {
        if (R.id.navigation_home != navController.currentDestination?.id) {
            navController.navigate(R.id.navigation_home)
        }
    }

    @SuppressLint("ClickableViewAccessibility")
    private fun setListeners() {
        navController.addOnDestinationChangedListener(this)
        bottomNavView.setOnNavigationItemSelectedListener(this)
    }


    /////////////////////////////////////
    //
    // IMPLEMENTS
    //
    /////////////////////////////////////
    override fun onDestinationChanged(
        controller: NavController,
        destination: NavDestination,
        arguments: Bundle?
    ) {
        Timber.d("navController.addOnDestinationChangedListener : $destination")
    }

    override fun onNavigationItemSelected(item: MenuItem): Boolean {
        Timber.d("bottomNavView.setOnNavigationItemSelectedListener()")
        Timber.e("Selected ${item.title}")

        if (item.itemId != bottomNavView.selectedItemId)
            NavigationUI.onNavDestinationSelected(
                item,
                navController
            )
        return true
    }
}