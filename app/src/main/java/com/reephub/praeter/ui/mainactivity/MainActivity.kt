package com.reephub.praeter.ui.mainactivity

import android.os.Bundle
import androidx.appcompat.app.AppCompatActivity
import com.reephub.praeter.R
import dagger.hilt.android.AndroidEntryPoint

@AndroidEntryPoint
class MainActivity : AppCompatActivity() {
    override fun onCreate(savedInstanceState: Bundle?) {
        super.onCreate(savedInstanceState)
        setContentView(R.layout.activity_main)
    }
}