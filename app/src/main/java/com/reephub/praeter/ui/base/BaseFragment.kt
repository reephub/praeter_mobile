package com.reephub.praeter.ui.base

import androidx.fragment.app.Fragment

abstract class BaseFragment : Fragment() {
    abstract fun onConnected()
    abstract fun onDisconnected()
}

inline infix fun Fragment.onConnected(block: () -> Unit): Unit = block()
inline infix fun Fragment.onDisconnected(block: () -> Unit): Unit = block()