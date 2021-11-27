package com.reephub.praeter.ui.base

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {

    abstract fun onConnected(isConnected: Boolean)
}