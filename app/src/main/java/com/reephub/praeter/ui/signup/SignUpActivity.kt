package com.reephub.praeter.ui.signup

import android.annotation.SuppressLint
import android.content.Intent
import android.os.Bundle
import androidx.activity.ComponentActivity
import androidx.activity.compose.setContent
import androidx.activity.viewModels
import androidx.appcompat.app.AppCompatActivity
import androidx.compose.foundation.layout.fillMaxSize
import androidx.compose.material3.MaterialTheme
import androidx.compose.material3.Surface
import androidx.compose.ui.Modifier
import androidx.fragment.app.Fragment
import androidx.lifecycle.Lifecycle
import androidx.lifecycle.lifecycleScope
import androidx.lifecycle.repeatOnLifecycle
import androidx.viewpager2.adapter.FragmentStateAdapter
import androidx.viewpager2.widget.ViewPager2
import com.google.android.material.tabs.TabLayoutMediator
import com.reephub.praeter.databinding.ActivitySignUpBinding
import com.reephub.praeter.ui.mainactivity.MainActivity
import com.reephub.praeter.ui.signup.plan.PlanFragment
import com.reephub.praeter.ui.signup.premium.PremiumFragment
import com.reephub.praeter.ui.signup.successfulsignup.SuccessfulSignUpFragment
import com.reephub.praeter.ui.signup.terms.TermsOfServiceFragment
import com.reephub.praeter.ui.signup.userform.UserFormFragment
import com.reephub.praeter.ui.splashscreen.SplashScreenContent
import dagger.hilt.android.AndroidEntryPoint
import kotlinx.coroutines.*
import timber.log.Timber
import java.util.*
import kotlin.coroutines.CoroutineContext

@AndroidEntryPoint
class SignUpActivity : ComponentActivity(), CoroutineScope {

    override val coroutineContext: CoroutineContext
        get() = Dispatchers.Main + Job()

    private val mViewModel: SignUpViewModel by viewModels()

    /////////////////////////////////////
    //
    // OVERRIDE
    //
    /////////////////////////////////////
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        initViewModelsObservers()

        lifecycleScope.launch(coroutineContext) {
            repeatOnLifecycle(Lifecycle.State.STARTED) {
                setContent {
                    // A surface container using the 'background' color from the theme
                    Surface(
                        modifier = Modifier.fillMaxSize(),
                        color = MaterialTheme.colorScheme.background
                    ) {
                        SignUpContent(mViewModel)
                    }
                }
            }
        }
    }


    /////////////////////////////////////
    //
    // CLASSES METHODS
    //
    /////////////////////////////////////
    @SuppressLint("SetTextI18n")
    private fun initViewModelsObservers() {
        Timber.d("initViewModelsObservers()")
    }
}